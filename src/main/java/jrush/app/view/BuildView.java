package jrush.app.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import jrush.app.gui.ViewNavigator;
import jrush.app.model.BuildEngine;
import jrush.app.model.Vehicle;
import jrush.app.model.components.VehicleType;
import jrush.app.util.GuiUtils;
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

    private final Button menuButton;
    private final Button newButton;
    private final Button loadButton;
    private final Button saveButton;
    private final Button resetButton;

    private PropertyChangeListener buildListener;

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

        this.menuButton = new Button("MENU");
        this.newButton = new Button("NOUVEAU");
        this.loadButton = new Button("CHARGER");
        this.saveButton = new Button("SAUVEGARDER");
        this.resetButton = new Button("REINITIALISER");

        refreshBoard();
        updateControls();
        setProperties();
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
     * Configure les propriétés de la vue.
     */
    private void setProperties() {
        setPadding(new Insets(20));
        setPrefSize(800, 800);

        Button[] buttons = {newButton, loadButton, saveButton, resetButton};
        for (Button b : buttons) {
            b.setPrefWidth(120);
        }
    }

    /**
     * Place les composants graphiques dans la vue.
     */
    private void placeComponents() {
        HBox hb1 = new HBox();
        { // HAUT
            hb1.setAlignment(Pos.CENTER_LEFT);
            hb1.setPadding(new Insets(0, 0, 15, 0));

            Label title = new Label("ÉDITEUR DE NIVEAU");
            title.setStyle("-fx-font-weight: bold; -fx-font-size: 1.2em;");

            Region leftSpacer = new Region();
            HBox.setHgrow(leftSpacer, Priority.ALWAYS);

            Region rightSpacer = new Region();
            HBox.setHgrow(rightSpacer, Priority.ALWAYS);
            rightSpacer.setMinWidth(120);

            hb1.getChildren()
               .addAll(menuButton, leftSpacer, title, rightSpacer);
        } // -----
        this.setTop(hb1);

        BorderPane bp1 = new BorderPane();
        { // CENTRE
            bp1.setCenter(boardGraphic);

            HBox hb2 = new HBox(10);
            { // -----
                hb2.setAlignment(Pos.CENTER);
                hb2.setPadding(new Insets(15));


                hb2.getChildren()
                   .addAll(newButton, loadButton, saveButton, resetButton);
            } // -----

            bp1.setBottom(hb2);
        } // -----
        this.setCenter(bp1);

        ScrollPane sp1 = new ScrollPane();
        { // DROITE
            sp1.setFitToWidth(true);
            sp1.setPrefWidth(280);

            VBox vb2 = new VBox(5);
            { // -----
                vb2.setPadding(new Insets(10, 0, 10, 0));
                vb2.setAlignment(Pos.TOP_CENTER);

                for (VehicleType type : VehicleType.values()) {
                    VehiclePaletteItem item =
                            new VehiclePaletteItem(buildEngine, type,
                                                   boardGraphic);
                    vb2.getChildren().add(item);
                }
            } // -----

            sp1.setContent(vb2);
        } // -----
        this.setRight(sp1);
    }

    /**
     * Connecte les contrôleurs aux composants graphiques de la vue.
     */
    private void connectControllers() {
        this.buildListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getPropertyName().equals(BuildEngine.PROP_BOARD)) {
                    refreshBoard();
                    updateControls();
                }
            }
        };

        buildEngine.addPropertyChangeListener(buildListener);

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

        menuButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                buildEngine.removePropertyChangeListener(buildListener);
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

    /**
     * Affiche une boîte de dialogue de sélection de fichier pour charger ou
     * sauvegarder un niveau, selon la valeur de isLoad.
     *
     * @param isLoad true pour charger un niveau, false pour en sauvegarder un.
     *
     * @return Le fichier sélectionné par l'utilisateur, ou null si
     * l'utilisateur a annulé la sélection.
     */
    private File showFileChooser(boolean isLoad) {
        return GuiUtils.showFileChooser(this.getScene().getWindow(), isLoad);
    }
}
