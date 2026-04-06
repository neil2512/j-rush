package jrush.app.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import jrush.app.gui.ViewNavigator;
import jrush.app.model.Board;
import jrush.app.model.GameEngine;
import jrush.app.model.Vehicle;
import jrush.app.util.GuiUtils;
import jrush.app.view.board.BoardGraphic;
import jrush.app.view.vehicle.VehicleGraphic;
import jrush.app.view.vehicle.VehicleGraphicPlayer;
import util.Contract;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;


/**
 * Vue principale du jeu, qui affiche le plateau de jeu et les véhicules. Elle
 * contient également les boutons de contrôle pour annuler, refaire, recommencer
 * et résoudre le niveau, ainsi que les étiquettes pour afficher le nombre de
 * coups et le temps écoulé. La vue est connectée au moteur de jeu pour recevoir
 * des mises à jour sur l'état du jeu et mettre à jour l'affichage en
 * conséquence.
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
    private final Label timerLabel;

    private final Button saveButton;
    private final Button menuButton;
    private final Button redoButton;
    private final Button undoButton;
    private final Button resetButton;
    private final Button solveButton;

    private PropertyChangeListener gameListener;

    // CONSTRUCTEURS

    public GameView(ViewNavigator navigator, GameEngine gameEngine) {
        Contract.checkCondition(navigator != null, "navigator == null");
        Contract.checkCondition(gameEngine != null, "gameEngine == null");
        Contract.checkCondition(gameEngine.isLoaded(),
                                "!gameEngine.isLoaded" + "()");

        // MODÈLE
        this.gameEngine = gameEngine;

        // VUE
        this.navigator = navigator;
        this.boardGraphic = new BoardGraphic();

        this.moveLabel = new Label();
        this.moveLabel.setText("Coups : " + gameEngine.getMoveCount());
        this.timerLabel = new Label();
        int secondsElapsed = gameEngine.getTime();
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        this.timerLabel.setText(String.format("%02d:%02d", minutes, seconds));

        this.menuButton = new Button("MENU");
        this.saveButton = new Button("SAUVEGARDER");
        this.redoButton = new Button("RETABLIR");
        this.undoButton = new Button("ANNULER");
        this.resetButton = new Button("RECOMMENCER");
        this.solveButton = new Button("RESOUDRE");

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
        solveButton.setDisable(gameEngine.checkWinCondition());
        boardGraphic.setDisable(gameEngine.checkWinCondition());
    }

    /**
     * Place les composants graphiques dans la vue.
     */
    private void placeComponents() {
        HBox hb1 = new HBox();
        { // HAUT
            hb1.setAlignment(Pos.TOP_LEFT);
            hb1.setPadding(new Insets(15));

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            hb1.getChildren().addAll(menuButton, spacer, saveButton);
        } // -----
        this.setTop(hb1);

        VBox vb1 = new VBox(20);
        { // CENTRE
            vb1.setAlignment(Pos.CENTER);

            HBox hb2 = new HBox();
            { // -----
                hb2.setMaxWidth(BoardGraphic.CELL_SIZE * Board.GRID_SIZE);
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hb2.getChildren().addAll(timerLabel, spacer, moveLabel);
            } // -----
            vb1.getChildren().add(hb2);

            { // -----
                for (Vehicle v : gameEngine.getBoard().getVehicles()) {
                    VehicleGraphic vg = new VehicleGraphicPlayer(gameEngine, v);
                    boardGraphic.getChildren().add(vg);
                }
            } // -----
            vb1.getChildren().add(boardGraphic);

            hb2 = new HBox();
            { // -----
                hb2.setMaxWidth(BoardGraphic.CELL_SIZE * Board.GRID_SIZE);
                hb2.setAlignment(Pos.CENTER);

                VBox vb3 = new VBox(8);
                { // -----
                    vb3.setAlignment(Pos.CENTER);
                    vb3.getChildren().addAll(resetButton, solveButton);
                } // -----

                Region leftSpacer = new Region();
                Region rightSpacer = new Region();
                HBox.setHgrow(leftSpacer, Priority.ALWAYS);
                HBox.setHgrow(rightSpacer, Priority.ALWAYS);
                hb2.getChildren()
                   .addAll(undoButton, leftSpacer, vb3, rightSpacer,
                           redoButton);
            } // -----
            vb1.getChildren().add(hb2);
        } // -----
        this.setCenter(vb1);
    }

    /**
     * Connecte les contrôleurs aux composants graphiques de la vue.
     */
    private void connectControllers() {
        gameEngine.startTimer();

        this.gameListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(GameEngine.PROP_MOVECOUNT)) {
                    moveLabel.setText("Coups : " + gameEngine.getMoveCount());

                    if (gameEngine.checkWinCondition()) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                showVictoryPopup();
                            }
                        });
                    }

                    updateControls();
                }
                if (evt.getPropertyName().equals(GameEngine.PROP_TIMER)) {
                    int secondsElapsed = (Integer) evt.getNewValue();
                    int minutes = secondsElapsed / 60;
                    int seconds = secondsElapsed % 60;

                    String time = String.format("%02d:%02d", minutes, seconds);
                    timerLabel.setText(time);
                }
            }
        };

        gameEngine.addPropertyChangeListener(gameListener);

        menuButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                gameEngine.removePropertyChangeListener(gameListener);
                navigator.showHome();
            }
        });

        saveButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                File file = showFileChooser();
                if (file != null) {
                    try {
                        gameEngine.saveBoard(file.getAbsolutePath());
                    } catch (IOException ex) {
                        GuiUtils.showError(
                                "Erreur de sauvegarde",
                                "Impossible de sauvegarder le niveau : " +
                                ex.getMessage());
                    }
                }
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

    /**
     * Affiche une boîte de dialogue de sélection de fichier pour sauvegarder un
     * niveau.
     *
     * @return Le fichier sélectionné par l'utilisateur, ou null si
     * l'utilisateur a annulé la sélection.
     */
    private File showFileChooser() {
        return GuiUtils.showFileChooser(this.getScene().getWindow(), false);
    }


    /**
     * Affiche une boîte de dialogue de victoire lorsque les conditions de
     * victoire sont remplies, avec les statistiques de la partie et le score
     * final du joueur. Après que l'utilisateur ait fermé la boîte de dialogue,
     * il est redirigé vers le menu principal.
     */
    private void showVictoryPopup() {
        int moves = gameEngine.getMoveCount();
        int score = Math.max(0, 5000 - (moves * 100));

        String stats = String.format(
                "Statistiques de la partie :\n" + "- Nombre de coups : %d\n" +
                "- Temps : %s\n\n" + "Votre score final : %d points", moves,
                timerLabel.getText(), score);

        GuiUtils.showInfo("Félicitations !",
                          "Vous avez libéré la voiture rouge ", stats);
        gameEngine.removePropertyChangeListener(gameListener);
        navigator.showHome();
    }
}
