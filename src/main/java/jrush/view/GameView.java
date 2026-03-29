package jrush.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import jrush.gui.ViewNavigator;
import jrush.model.GameEngine;
import jrush.model.logic.StdGameEngine;
import jrush.model.Vehicle;
import util.Contract;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.IOException;


/**
 * Vue principale du jeu, qui affiche le plateau de jeu et les véhicules.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          ViewNavigator navigator
 *      Préconditions :
 *          navigator != null
 * </pre>
 */
public class GameView extends BorderPane {

    // ATTRIBUTS

    private final GameEngine engine;

    private final ViewNavigator navigator;
    private final BoardGraphic boardGraphic;
    private final Label moveLabel;

    private final Button redoBtn;
    private final Button undoBtn;
    private final Button resetBtn;

    // CONSTRUCTEURS

    public GameView(ViewNavigator navigator) {
        Contract.checkCondition(navigator != null, "navigator == null");

        // MODÈLE
        this.engine = new StdGameEngine();

        // temporaire
        try {
            this.engine.loadBoard("/home/neil/Téléchargements/test.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // VUE
        this.navigator = navigator;
        this.boardGraphic = new BoardGraphic();
        this.moveLabel = new Label(String.valueOf(engine.getMoveCount()));
        this.redoBtn = new Button("Redo");
        this.undoBtn = new Button("Undo");
        this.resetBtn = new Button("Reset");

        placeComponents();
        updateControls();

        // CONTRÔLEUR
        connectControllers();

    }

    // REQUÊTES

    // COMMANDES

    // OUTILS

    private void updateControls() {
        undoBtn.setDisable(!engine.canUndoBoardMove());
        redoBtn.setDisable(!engine.canRedoBoardMove());
        resetBtn.setDisable(!engine.canUndoBoardMove());
        boardGraphic.setDisable(engine.checkWinCondition());
    }

    private void placeComponents() {
        BorderPane.setAlignment(moveLabel, Pos.CENTER);
        this.setTop(moveLabel);

        for (Vehicle v : engine.getBoard().getVehicles()) {
            VehicleGraphic vg = new VehicleGraphic(engine, v);
            boardGraphic.getChildren().add(vg);
        }
        BorderPane.setAlignment(boardGraphic, Pos.CENTER);
        this.setCenter(boardGraphic);

        HBox p = new HBox(10);
        {//--
            p.setAlignment(Pos.CENTER);
            p.setPadding(new Insets(15));

            p.getChildren().addAll(undoBtn, redoBtn, resetBtn);
        }//--
        this.setBottom(p);
    }

    private void connectControllers() {
        engine.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(GameEngine.PROP_MOVECOUNT)) {
                    if (engine.checkWinCondition()) {
                        moveLabel.setText("VICTOIRE");
                    } else {
                        String count = String.valueOf(engine.getMoveCount());
                        moveLabel.setText(count);
                    }

                    updateControls();
                }
            }
        });

        undoBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    engine.undoBoardMove();
                } catch (PropertyVetoException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        redoBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    engine.redoBoardMove();
                } catch (PropertyVetoException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        resetBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                engine.resetBoard();
            }
        });
    }
}
