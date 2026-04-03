package jrush.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import jrush.gui.ViewNavigator;
import jrush.model.BuildEngine;
import jrush.model.Vehicle;
import jrush.model.components.StdVehicle;
import jrush.model.components.VehicleType;
import jrush.model.logic.StdBuildEngine;
import jrush.util.Position;
import util.Contract;

import java.io.File;
import java.io.IOException;

/**
 * Vue de construction de niveau, affiche le plateau de jeu, les vehicule,
 * ainsi que la liste des véhicules déposable
 */
public class BuildView extends BorderPane {

    // ATTRIBUTS

    private final BuildEngine engine;

    private final ViewNavigator navigator;
    private final BoardGraphic boardGraphic;

    private File currentFile;
    private final Button loadButton;
    private final Button saveButton;
    private final Button resetButton;
    private final Button newButton;

    // CONSTRUCTEUR

    public BuildView(ViewNavigator navigator) {
        Contract.checkCondition(navigator != null);

        // MODÈLE
        this.engine = new StdBuildEngine();
        this.currentFile = null;

        // VUE
        this.navigator = navigator;
        this.boardGraphic = new BoardGraphic();
        this.loadButton = new Button("Charger");
        this.saveButton = new Button("Sauvegarder");
        this.resetButton = new Button("Reset");
        this.newButton = new Button("Nouveau");

        placeComponents();
        updateControls();

        // CONTRÔLEUR
        connectControllers();
        connectBoardDrop();

        }

        //OUTILS

    private void updateControls() {
        boolean loaded = engine.isLoaded();
        saveButton.setDisable(!loaded || !engine.isValid());
        resetButton.setDisable(!loaded);
        boardGraphic.setDisable(!loaded);
    }

        private void placeComponents() {
            Label title = new Label("Construire une carte");
            BorderPane.setAlignment(title, Pos.CENTER);
            this.setTop(title);


            // GAUCHE : board et boutons
            BorderPane leftPane = new BorderPane();
            {
                BorderPane.setAlignment(boardGraphic, Pos.CENTER);
                leftPane.setCenter(boardGraphic);

                HBox buttons = new HBox(10);
                buttons.setAlignment((Pos.CENTER_LEFT));
                buttons.setPadding(new Insets(15));
                buttons.getChildren().addAll(newButton,loadButton,saveButton,resetButton);
                leftPane.setBottom(buttons);
            }
            this.setCenter((leftPane));

            // DROITE : liste des véhicules avec scroll
            FlowPane vehicleList = new FlowPane();
            vehicleList.setPadding(new Insets(10));
            vehicleList.setHgap(10);
            vehicleList.setVgap(10);
            vehicleList.setPrefWrapLength(3 * (BoardGraphic.CELL_SIZE + 10));

            for (VehicleType type : VehicleType.values()) {
                VehiclePreview preview = new VehiclePreview(engine, type, boardGraphic);
                vehicleList.getChildren().add(preview);
            }

            ScrollPane scrollPane = new ScrollPane(vehicleList);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefWidth(3 * BoardGraphic.CELL_SIZE + 60);
            this.setRight(scrollPane);
        }

    private void connectControllers() {
        engine.addPropertyChangeListener(evt -> {
            updateControls();
        });

        // New Button
        newButton.setOnAction(e -> {
            engine.newBoard();
            boardGraphic.refresh();
            updateControls();
        });

        // Chargement de fichier
        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Charger un niveau");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Fichiers texte", "*.txt")
            );
            File file = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (file != null) {
                currentFile = file;
                try {
                    engine.loadBoard(file.getAbsolutePath());
                    refreshBoard();
                    updateControls();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Sauvegarde de fichier
        saveButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarder un niveau");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Fichiers texte", "*.txt")
            );
            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (file != null) {
                try {
                    engine.saveBoard(file.getAbsolutePath());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //Reset Button
        resetButton.setOnAction(e -> {
            if (currentFile != null) {
                // Recharger le fichier
                try {
                    engine.loadBoard(currentFile.getAbsolutePath());
                    refreshBoard();
                    updateControls();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                // Vider le board
                engine.newBoard();
                boardGraphic.refresh();
                updateControls();
            }
        });
    }

    private void connectBoardDrop() {
        //Mettre un board au chargement
        engine.newBoard();
        updateControls();
        boardGraphic.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof VehiclePreview
                    && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        // Dépot d'un véhicule sur le board
        boardGraphic.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                VehicleType droppedType = VehicleType.valueOf(db.getString());

                int x = (int) (event.getX() / BoardGraphic.CELL_SIZE);
                int y = (int) (event.getY() / BoardGraphic.CELL_SIZE);

                StdVehicle vehicle = new StdVehicle(droppedType, true, new Position(x, y));

                try {
                    engine.addVehicleOnBoard(vehicle);

                    VehicleGraphicEditor vge = new VehicleGraphicEditor(
                            engine, vehicle, boardGraphic
                    );
                    boardGraphic.getChildren().add(vge);

                    event.setDropCompleted(true);
                } catch (IllegalArgumentException e) {
                    event.setDropCompleted(false);
                }
            }
            event.consume();
        });
    }

    private void refreshBoard() {
        boardGraphic.refresh();
        for (Vehicle v : engine.getBoard().getVehicles()) {
            VehicleGraphicEditor vge = new VehicleGraphicEditor(engine, v,boardGraphic);
            boardGraphic.getChildren().add(vge);
        }
    }
}
