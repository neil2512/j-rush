package jrush.app.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import jrush.app.gui.ViewNavigator;
import jrush.app.model.GameEngine;
import jrush.app.model.Vehicle;
import jrush.app.view.board.BoardGraphic;
import jrush.app.view.vehicle.VehicleGraphic;
import jrush.app.view.vehicle.VehicleGraphicPlayer;
import util.Contract;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;


/**
 * Vue principale du jeu, qui affiche le plateau de jeu et les véhicules.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          – ViewNavigator navigator
 *          – GameEngine gameEngine
 *      Préconditions :
 *          – navigator != null
 *          – gameEngine != null && gameEngine.isLoaded()
 * </pre>
 */
public class GameView extends BorderPane {

    // ATTRIBUTS

    private final ViewNavigator navigator;
    private final GameEngine gameEngine;

    private final BoardGraphic boardGraphic;

    private final Label moveLabel;
    private final Button saveButton;
    private final Button leaveButton;

    private final Button redoButton;
    private final Button undoButton;
    private final Button resetButton;

    // CONSTRUCTEURS

    public GameView(ViewNavigator navigator, GameEngine gameEngine) {
        Contract.checkCondition(navigator != null, "navigator == null");
        Contract.checkCondition(gameEngine != null, "gameEngine == null");
        Contract.checkCondition(gameEngine.isLoaded(), "!gameEngine.isLoaded" +
                                                       "()");

        // MODÈLE
        this.gameEngine = gameEngine;

        // VUE
        this.navigator = navigator;
        this.boardGraphic = new BoardGraphic();
        this.moveLabel = new Label("0");
        this.saveButton = new Button("Save");
        this.leaveButton = new Button("Leave");
        this.redoButton = new Button("Redo");
        this.undoButton = new Button("Undo");
        this.resetButton = new Button("Reset");

        placeComponents();
        updateControls();

        // CONTRÔLEUR
        connectControllers();

    }

    // OUTILS

    /**
     * Met à jour l'état des boutons de contrôle en fonction de l'état du moteur
     * de jeu.
     */
    private void updateControls() {
        undoButton.setDisable(!gameEngine.canUndoBoardMove());
        redoButton.setDisable(!gameEngine.canRedoBoardMove());
        resetButton.setDisable(!gameEngine.canUndoBoardMove());
        boardGraphic.setDisable(gameEngine.checkWinCondition());
    }

    /**
     * Place les composants graphiques dans la vue.
     */
    private void placeComponents() {
        BorderPane.setAlignment(moveLabel, Pos.CENTER);
        this.setTop(moveLabel);

        for (Vehicle v : gameEngine.getBoard().getVehicles()) {
            VehicleGraphic vg = new VehicleGraphicPlayer(gameEngine, v);
            boardGraphic.getChildren().add(vg);
        }
        BorderPane.setAlignment(boardGraphic, Pos.CENTER);
        this.setCenter(boardGraphic);

        HBox p = new HBox(10);
        {//--
            p.setAlignment(Pos.CENTER);
            p.setPadding(new Insets(15));

            p.getChildren().addAll(undoButton, redoButton, resetButton);
        }//--
        this.setBottom(p);
    }

    /**
     * Connecte les contrôleurs aux composants graphiques de la vue.
     */
    private void connectControllers() {
        gameEngine.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(GameEngine.PROP_MOVECOUNT)) {
                    if (gameEngine.checkWinCondition()) {
                        moveLabel.setText("VICTOIRE");
                    } else {
                        String count =
                                String.valueOf(gameEngine.getMoveCount());
                        moveLabel.setText(count);
                    }

                    updateControls();
                }
            }
        });

        saveButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        leaveButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                navigator.showHome();
            }
        });

        undoButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    gameEngine.undoBoardMove();
                } catch (PropertyVetoException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        redoButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    gameEngine.redoBoardMove();
                } catch (PropertyVetoException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        resetButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                gameEngine.resetBoard();
            }
        });
    }
}
