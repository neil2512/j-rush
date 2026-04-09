package jrush.app.view;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import jrush.app.gui.ViewNavigator;
import jrush.app.model.Board;
import jrush.app.model.GameEngine;
import jrush.app.model.logic.LevelGenerator;
import jrush.app.util.GuiUtils;
import jrush.app.util.Contract;

import java.io.File;
import java.io.IOException;

/**
 * Vue d'accueil de l'application.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          – ViewNavigator navigator
 *          – GameEngine gameEngine
 *      Préconditions :
 *          – navigator != null
 *          – gameEngine != null
 * </pre>
 */
public class HomeView extends VBox {

    // CONSTANTES

    private static final int GENERATION_MIN = 8;

    // ATTRIBUTS

    private final GameEngine gameEngine;

    private final ViewNavigator navigator;
    private final Button playButton;
    private final Button importButton;
    private final Button generateButton;
    private final Button buildButton;

    // CONSTRUCTEURS

    public HomeView(ViewNavigator navigator, GameEngine gameEngine) {
        Contract.checkCondition(navigator != null, "navigator == null");
        Contract.checkCondition(gameEngine != null, "gameEngine == null");

        // MODÈLE
        this.gameEngine = gameEngine;

        // VUE
        this.navigator = navigator;
        this.navigator.updateTitle("Accueil");
        this.playButton = new Button("NIVEAUX CLASSIQUES");
        this.generateButton = new Button("DEFI ALÉATOIRE");
        this.importButton = new Button("CHARGER UN NIVEAU");
        this.buildButton = new Button("ÉDITEUR DE NIVEAU");

        setProperties();
        placeComponents();

        // CONTRÔLEUR
        connectControllers();
    }

    // OUTILS

    /**
     * Configure les propriétés de la vue.
     */
    private void setProperties() {
        setAlignment(Pos.CENTER);
        setSpacing(25);
        setPadding(new Insets(40));

        getStyleClass().add("root");

        Button[] buttons =
                {playButton, generateButton, importButton, buildButton};
        for (Button b : buttons) {
            b.setPrefWidth(220);
            b.setPrefHeight(40);
            b.getStyleClass().add("button");
        }

        buildButton.getStyleClass().remove("button");
        buildButton.getStyleClass().add("button-secondary");
    }

    /**
     * Place les composants graphiques dans la vue.
     */
    private void placeComponents() {
        Label title = new Label("JRUSH");
        title.getStyleClass().add("title-main");
        VBox.setMargin(title, new Insets(0, 0, 80, 0));
        getChildren().addAll(title, playButton, generateButton, importButton,
                             buildButton);
    }

    /**
     * Connecte les contrôleurs aux composants graphiques de la vue.
     */
    private void connectControllers() {
        playButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                navigator.showLevels();
            }
        });


        generateButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                handleGenerationAction();
            }
        });

        importButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                Window window = importButton.getScene().getWindow();
                File selectedFile = GuiUtils.showFileChooser(window, true);
                if (selectedFile != null) {
                    try {
                        gameEngine.loadBoard(selectedFile.getAbsolutePath());
                        navigator.showGame();
                    } catch (IOException e) {
                        GuiUtils.showError("Erreur de lecture", e.getMessage());
                    } catch (Exception e) {
                        GuiUtils.showError("Fichier invalide", e.getMessage());
                    }
                }
            }
        });

        buildButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                navigator.showBuild();
            }
        });
    }

    // OUTILS

    /**
     * Gère l'action de génération d'un niveau aléatoire en lançant une tâche
     * de génération dans un thread séparé, tout en affichant une alerte de
     * chargement pour informer l'utilisateur du processus en cours. La méthode
     * configure également les événements de fin de tâche pour gérer les
     * différentes issues possibles (succès, échec, annulation) et assurer une
     * expérience utilisateur fluide et réactive pendant la génération du
     * niveau.
     */
    private void handleGenerationAction() {
        final Task<Board> generationTask = new Task<>() {
            @Override
            protected Board call() {
                return LevelGenerator.generateLevel(GENERATION_MIN, 100);
            }
        };

        final Alert loadingAlert = createAndShowLoadingAlert(generationTask);

        startGenerationTask(generationTask, loadingAlert);
    }

    /**
     * Crée et affiche une alerte de chargement pendant la génération d'un
     * niveau.
     *
     * @param task La tâche de génération de niveau en cours d'exécution,
     * utilisée pour permettre l'annulation de la génération via l'alerte.
     *
     * @return L'instance de l'alerte de chargement affichée, qui peut être
     * fermée à la fin de la tâche.
     */
    private Alert createAndShowLoadingAlert(Task<Board> task) {
        Alert loadingAlert = new Alert(Alert.AlertType.INFORMATION);

        loadingAlert.setTitle("Génération en cours");
        loadingAlert.setHeaderText("Création de votre défi");
        loadingAlert.setContentText("L'algorithme assemble et vérifie des " +
                                    "plateaux...");

        loadingAlert.getDialogPane().getButtonTypes().clear();
        loadingAlert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Button cancelButton = (Button)
                loadingAlert.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("ANNULER");

        cancelButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                task.cancel(true);
            }
        });

        if (!getScene().getStylesheets().isEmpty()) {
            loadingAlert.getDialogPane().getStylesheets()
                        .add(getScene().getStylesheets().getFirst());
            loadingAlert.getDialogPane().getStyleClass().add("root");
        }

        loadingAlert.show();
        return loadingAlert;
    }

    /**
     * Ferme l'alerte de chargement en lui assignant un résultat de type OK, ce
     * qui permet de s'assurer que les événements de fin de tâche (succès,
     * échec, annulation) sont correctement gérés.
     *
     * @param alert L'alerte de chargement à fermer.
     */
    private void closeLoadingAlert(Alert alert) {
        alert.setResult(ButtonType.OK);
        alert.close();
    }

    /**
     * Démarre la tâche de génération de niveau et configure les événements de
     * fin de tâche pour gérer les différentes issues possibles (succès, échec,
     * annulation).
     *
     * @param generationTask La tâche de génération de niveau à exécuter.
     * @param loadingAlert L'alerte de chargement à fermer à la fin de la
     * tâche.
     */
    private void startGenerationTask(
            Task<Board> generationTask,
            final Alert loadingAlert
    ) {

        generationTask.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {
                closeLoadingAlert(loadingAlert);
                Board generatedBoard = generationTask.getValue();
                if (generatedBoard != null) {
                    try {
                        gameEngine.loadGeneratedBoard(generatedBoard);
                        navigator.showGame();
                    } catch (Exception e) {
                        GuiUtils.showError("Erreur", "Impossible de charger " +
                                                     "le niveau.");
                    }
                } else {
                    GuiUtils.showError("Erreur de génération",
                                       "L'algorithme n'a pas trouvé de " +
                                       "niveau dans le temps imparti.");
                }
            }
        });

        generationTask.setOnFailed(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {
                closeLoadingAlert(loadingAlert);
                GuiUtils.showError("Erreur critique",
                                   "La génération a échoué suite à une " +
                                   "erreur interne.");
            }
        });

        generationTask.setOnCancelled(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {
                closeLoadingAlert(loadingAlert);
            }
        });

        new Thread(generationTask).start();
    }
}
