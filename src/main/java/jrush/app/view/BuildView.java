package jrush.app.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import jrush.app.gui.ViewNavigator;
import jrush.app.model.BuildEngine;
import jrush.app.model.Vehicle;
import jrush.app.model.components.VehicleType;
import jrush.app.view.board.BoardGraphic;
import jrush.app.view.vehicle.VehicleGraphicEditor;
import jrush.app.view.vehicle.VehiclePaletteItem;
import util.Contract;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

public class BuildView extends BorderPane {

    // ATTRIBUTS

    private final ViewNavigator navigator;
    private final BuildEngine buildEngine;

    private final BoardGraphic boardGraphic;

    private final Button newButton = new Button("Nouveau");
    private final Button loadButton = new Button("Charger");
    private final Button saveButton = new Button("Sauvegarder");
    private final Button resetButton = new Button("Reset");
    private final Button leaveButton = new Button("Retour");

    // CONSTRUCTEURS

    public BuildView(ViewNavigator navigator, BuildEngine buildEngine) {
        Contract.checkCondition(navigator != null, "navigator == null");
        Contract.checkCondition(buildEngine != null, "gameEngine == null");
        Contract.checkCondition(buildEngine.isLoaded(),
                                "!buildEngine.isLoaded()");

        // MODÈLE
        this.buildEngine = buildEngine;

        // VUE
        this.navigator = navigator;
        this.boardGraphic = new BoardGraphic();

        refreshBoard();
        updateControls();
        placeComponents();

        // CONTRÔLEUR
        connectControllers();
    }

    // OUTILS

    /**
     * Met à jour l'état des boutons de contrôle en fonction de l'état du moteur
     * de construction.
     */
    private void updateControls() {
        boolean loaded = buildEngine.isLoaded();
        saveButton.setDisable(!loaded || !buildEngine.isValid());
        resetButton.setDisable(!loaded);
    }

    /**
     * Place les composants graphiques dans la vue.
     */
    private void placeComponents() {
        Label title = new Label("ÉDITEUR DE NIVEAU");
        { // HAUT
            BorderPane.setAlignment(title, Pos.CENTER);
        } // -----
        this.setTop(title);

        BorderPane bp = new BorderPane();
        { // CENTRE
            bp.setCenter(boardGraphic);
            HBox topButtons =
                    new HBox(10, newButton, loadButton, saveButton, resetButton,
                             leaveButton);
            topButtons.setPadding(new Insets(15));
            topButtons.setAlignment(Pos.CENTER);
            bp.setBottom(topButtons);
        } // -----
        this.setCenter(bp);

        ScrollPane sp = new ScrollPane();
        { // DROITE
            sp.setFitToWidth(true);
            sp.setPrefWidth(280);

            VBox vb = new VBox(5);
            { // -----
                vb.setPadding(new Insets(10, 0, 10, 0));
                vb.setAlignment(Pos.TOP_CENTER);

                for (VehicleType type : VehicleType.values()) {
                    VehiclePaletteItem item =
                            new VehiclePaletteItem(buildEngine, type,
                                                   boardGraphic);
                    vb.getChildren().add(item);
                }
            } // -----

            sp.setContent(vb);
        } // -----
        this.setRight(sp);
    }

    /**
     * Connecte les contrôleurs aux composants graphiques de la vue.
     */
    private void connectControllers() {
        buildEngine.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getPropertyName().equals(BuildEngine.PROP_BOARD)) {
                    refreshBoard();
                    updateControls();
                }
            }
        });

        newButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                buildEngine.newBoard();
            }
        });

        loadButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                File file = showFileChooser(true);
                if (file != null) {
                    try {
                        buildEngine.loadBoard(file.getAbsolutePath());
                    } catch (IOException ex) {
                        //showError("Erreur de chargement")
                    }
                }
            }
        });

        saveButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                File file = showFileChooser(false);
                if (file != null) {
                    try {
                        buildEngine.saveBoard(file.getAbsolutePath());
                    } catch (IOException ex) {
                        //showError("Erreur de sauvegarde")
                    }
                }
            }
        });

        resetButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                buildEngine.resetBoard();
            }
        });

        leaveButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                navigator.showHome();
            }
        });
    }

    private void refreshBoard() {
        boardGraphic.refresh();
        if (buildEngine.isLoaded()) {
            for (Vehicle v : buildEngine.getBoard().getVehicles()) {
                VehicleGraphicEditor vge =
                        new VehicleGraphicEditor(buildEngine, boardGraphic, v);
                boardGraphic.getChildren().add(vge);
            }
        }
    }

    private File showFileChooser(boolean load) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters()
               .add(new FileChooser.ExtensionFilter("Niveaux Rush Hour (*.txt)",
                                                    "*.txt"));
        return load ? chooser.showOpenDialog(this.getScene().getWindow())
                    : chooser.showSaveDialog(this.getScene().getWindow());
    }
}
