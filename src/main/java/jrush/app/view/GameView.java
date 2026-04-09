package jrush.app.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import jrush.app.gui.ViewNavigator;
import jrush.app.model.Board;
import jrush.app.model.GameEngine;
import jrush.app.model.Vehicle;
import jrush.app.model.components.VehicleType;
import jrush.app.model.util.Move;
import jrush.app.util.GuiUtils;
import jrush.app.view.board.BoardGraphic;
import jrush.app.view.vehicle.VehicleGraphic;
import jrush.app.view.vehicle.VehicleGraphicPlayer;
import jrush.app.util.Contract;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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

    // CONSTANTES

    private static final int MAX_SCORE = 10000;

    // ATTRIBUTS

    private final GameEngine gameEngine;

    private final ViewNavigator navigator;
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
        this.navigator.updateTitle("En jeu");

        this.boardGraphic = new BoardGraphic();

        this.moveLabel = new Label();
        this.moveLabel.setText("Coups : " + gameEngine.getMoveCount());

        this.timerLabel = new Label();
        int secondsElapsed = gameEngine.getTime();
        this.timerLabel.setText(formatTime(secondsElapsed));

        this.menuButton = new Button("MENU");
        this.saveButton = new Button("SAUVEGARDER");
        this.redoButton = new Button("RETABLIR");
        this.undoButton = new Button("ANNULER");
        this.resetButton = new Button("RECOMMENCER");
        this.solveButton = new Button("RESOUDRE");

        setProperties();
        placeComponents();
        updateControls();

        // CONTRÔLEUR
        connectControllers();

    }

    // OUTILS

    /**
     * Applique les styles CSS aux composants graphiques de la vue pour assurer
     * une cohérence visuelle avec le reste de l'application.
     */
    private void setProperties() {
        getStyleClass().add("root");

        menuButton.getStyleClass().add("button-secondary");
        saveButton.getStyleClass().add("button-secondary");

        undoButton.getStyleClass().add("button");
        redoButton.getStyleClass().add("button");

        resetButton.getStyleClass().add("button-accent");
        solveButton.getStyleClass().add("button-secondary");

        moveLabel.getStyleClass().add("text-hud");
        timerLabel.getStyleClass().add("text-hud");

        Button[] bottomButtons =
                {undoButton, redoButton, resetButton, solveButton};
        for (Button b : bottomButtons) {
            b.setMinWidth(Region.USE_PREF_SIZE);
        }
    }

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

        VBox vb1 = new VBox(15);
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

            StackPane sp2 = new StackPane();
            {
                sp2.getStyleClass().add("card");
                sp2.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                sp2.setPadding(new Insets(10));

                { // -----
                    for (Vehicle v : gameEngine.getBoard().getVehicles()) {
                        VehicleGraphic vg =
                                new VehicleGraphicPlayer(gameEngine, v);
                        boardGraphic.getChildren().add(vg);
                    }
                } // -----
                sp2.getChildren().add(boardGraphic);
            }
            vb1.getChildren().add(sp2);

            hb2 = new HBox();
            { // -----
                hb2.setMaxWidth(BoardGraphic.CELL_SIZE * Board.GRID_SIZE);
                hb2.setAlignment(Pos.CENTER);

                VBox vb3 = new VBox(10);
                { // -----
                    vb3.setAlignment(Pos.CENTER);
                    resetButton.setPrefWidth(140);
                    solveButton.setPrefWidth(140);
                    vb3.getChildren().addAll(resetButton, solveButton);
                } // -----

                Region leftSpacer = new Region();
                Region rightSpacer = new Region();
                HBox.setHgrow(leftSpacer, Priority.ALWAYS);
                HBox.setHgrow(rightSpacer, Priority.ALWAYS);

                undoButton.setPrefWidth(110);
                redoButton.setPrefWidth(110);

                hb2.getChildren()
                   .addAll(undoButton, leftSpacer, vb3, rightSpacer,
                           redoButton);
            } // -----
            VBox.setMargin(hb2, new Insets(0, 0, 15, 0));
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
                    timerLabel.setText(formatTime(secondsElapsed));
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
                    } catch (IOException e) {
                        GuiUtils.showError(
                                "Erreur de sauvegarde",
                                "Impossible de sauvegarder le niveau : " +
                                e.getMessage());
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
                    GuiUtils.showError(
                            "Erreur d'action",
                            "Impossible d'annuler le mouvement : " +
                            e.getMessage());
                }
            }
        });

        redoButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    gameEngine.redoBoardMove();
                } catch (PropertyVetoException e) {
                    GuiUtils.showError(
                            "Erreur d'action",
                            "Impossible de rétablir le mouvement : " +
                            e.getMessage());
                }
            }
        });

        resetButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                gameEngine.resetBoard();
            }
        });

        solveButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                List<Move> rawSolution = gameEngine.getCachedSolution();

                gameEngine.resetBoard();

                List<Move> realHistory = new ArrayList<>();
                Board realBoard = gameEngine.getBoard();

                for (Move move : rawSolution) {
                    Vehicle realVehicle = realBoard.findVehicle(
                            VehicleType.fromId(move.getVehicle().getId()));
                    realHistory.add(new Move(realVehicle, move.getDelta()));
                }

                realBoard.setHistory(realHistory);
                realBoard.setHistoryCursor(-1);

                updateControls();

                GuiUtils.showInfo("Solution prête",
                                  "Le puzzle a été réinitialisé à " +
                                  "son état d'origine.",
                                  "Utilisez le bouton 'Rétablir' pour " +
                                  "parcourir les " +
                                  realHistory.size() +
                                  " coups de la solution.");
            }
        });
    }

    /**
     * Formate les secondes en affichage MM:SS
     *
     * @param totalSeconds Le nombre total de secondes à formater.
     *
     * @return Une chaîne de caractères représentant le temps formaté en minutes
     * et secondes.
     */
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
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
        int minMoves = gameEngine.getOptimalMoves();
        int time = gameEngine.getTime();

        int score = MAX_SCORE - ((moves - minMoves) * 150) - (time * 10);
        score = Math.max(0, score);

        String stats = String.format(
                "Statistiques de la partie :\n" +
                "- Coups : %d (Record optimal : %d)\n" +
                "- Temps : %s\n\n" +
                "Score final : %d points",
                moves, minMoves, timerLabel.getText(), score);

        GuiUtils.showInfo("Félicitations !", "Sortie débloquée !", stats);

        gameEngine.removePropertyChangeListener(gameListener);
        navigator.showHome();
    }
}
