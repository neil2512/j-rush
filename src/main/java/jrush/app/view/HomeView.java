package jrush.app.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import jrush.app.gui.ViewNavigator;
import jrush.app.model.GameEngine;
import util.Contract;

import java.io.File;

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

    private static final double SPACING = 20;

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
        setSpacing(SPACING);
    }

    /**
     * Place les composants graphiques dans la vue.
     */
    private void placeComponents() {
        Label title = new Label("JRUSH");
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
                navigator.showGame();
            }
        });

        importButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Charger un niveau");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Fichiers Texte",
                                                        "*.txt")
                );

                Window stage = importButton.getScene().getWindow();
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    try {
                        gameEngine.loadBoard(selectedFile.getAbsolutePath());
                        navigator.showGame();
                    } catch (java.io.IOException e) {
                        System.err.println("Erreur lors du chargement : " +
                                           e.getMessage());
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
