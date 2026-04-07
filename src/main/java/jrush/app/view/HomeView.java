package jrush.app.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import jrush.app.gui.ViewNavigator;
import jrush.app.model.GameEngine;
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
                GuiUtils.showInfo("Information", "Génération indisponible",
                                  "La génération est actuellement " +
                                  "indisponible. Ajoutée à la prochaine mise " +
                                  "à jour.");
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
}
