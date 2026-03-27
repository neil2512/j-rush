package jrush.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jrush.model.GameEngine;
import jrush.model.logic.StdGameEngine;

import java.io.File;

public class TestView {

    // ATTRIBUTS

    private final Stage stage;
    private final StackPane root;
    private final GameEngine model;

    private final Button loadButton;

    // CONSTRUCTEUR

    public TestView(Stage stage) {

        // MODÈLE
        this.model = new StdGameEngine();

        // VUE
        this.stage = stage;
        stage.setTitle("JRush");
        this.root = new StackPane();
        loadButton = new Button("Charger");

        placeComponents();

        // CONTRÔLEUR
        connectControllers();

    }

    // REQUÊTES

    // COMMANDES

    public void display() {
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    // OUTILS

    private void placeComponents() {
        root.getChildren().add(loadButton);
    }

    private void connectControllers() {

        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choisir un fichier de niveau");
                fileChooser.getExtensionFilters()
                           .add(new FileChooser.ExtensionFilter(
                                   "Fichiers Niveau", "*.txt"));
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    System.out.println(
                            "Fichier sélectionné : " +
                            selectedFile.getAbsolutePath());
                    // TRANSMETTRE ICI
                }
            }
        });
    }

}
