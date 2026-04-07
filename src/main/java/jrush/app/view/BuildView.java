package jrush.app.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import jrush.app.gui.ViewNavigator;
import jrush.app.model.BuildEngine;
import jrush.app.model.Vehicle;
import jrush.app.model.components.VehicleType;
import jrush.app.util.GuiUtils;
import jrush.app.view.board.BoardGraphic;
import jrush.app.view.vehicle.VehicleGraphicEditor;
import jrush.app.view.vehicle.VehiclePaletteItem;
import jrush.app.util.Contract;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

/**
 * Vue principale de l'éditeur de niveau. Affiche le plateau de jeu, les boutons
 * de contrôle et la palette de véhicules. Se connecte au BuildEngine pour
 * afficher l'état du niveau en cours d'édition et pour réagir aux actions de
 * l'utilisateur.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          – ViewNavigator navigator
 *          – BuildEngine buildEngine
 *      Préconditions :
 *          – navigator != null
 *          – buildEngine != null && buildEngine.isLoaded()
 * </pre>
 */
public class BuildView extends BorderPane {

    // ATTRIBUTS

    private final BuildEngine buildEngine;

    private final ViewNavigator navigator;
    private final BoardGraphic boardGraphic;

    private final Button menuButton;
    private final Button newButton;
    private final Button loadButton;
    private final Button saveButton;
    private final Button resetButton;

    private final Label statusLabel;

    private PropertyChangeListener buildListener;

    // CONSTRUCTEURS

    public BuildView(ViewNavigator navigator, BuildEngine buildEngine) {
        Contract.checkCondition(navigator != null, "navigator == null");
        Contract.checkCondition(buildEngine != null, "buildEngine == null");
        Contract.checkCondition(buildEngine.isLoaded(),
                                "!buildEngine.isLoaded()");

        // MODÈLE
        this.buildEngine = buildEngine;

        // VUE
        this.navigator = navigator;
        this.navigator.updateTitle("Éditeur de niveau");
        this.boardGraphic = new BoardGraphic();

        this.menuButton = new Button("MENU");
        this.newButton = new Button("NOUVEAU");
        this.loadButton = new Button("CHARGER");
        this.saveButton = new Button("SAUVEGARDER");
        this.resetButton = new Button("REINITIALISER");
        this.statusLabel = new Label();

        setProperties();
        placeComponents();
        refreshBoard();
        updateControls();

        // CONTRÔLEUR
        connectControllers();
    }

    // OUTILS

    /**
     * Configure les propriétés de la vue.
     */
    private void setProperties() {
        getStyleClass().add("root");
        setPadding(new Insets(20));

        menuButton.getStyleClass().add("button-secondary");
        newButton.getStyleClass().add("button");
        loadButton.getStyleClass().add("button");
        saveButton.getStyleClass().add("button");
        resetButton.getStyleClass().add("button-accent");

        Button[] buttons = {newButton, loadButton, saveButton, resetButton};
        for (Button b : buttons) {
            b.setMinWidth(Region.USE_PREF_SIZE);
        }
    }

    /**
     * Place les composants graphiques dans la vue.
     */
    private void placeComponents() {
        HBox hb1 = new HBox();
        { // HAUT
            hb1.setAlignment(Pos.CENTER_LEFT);
            hb1.setPadding(new Insets(0, 0, 30, 0));

            Label title = new Label("ÉDITEUR DE NIVEAU");
            title.getStyleClass().add("title-section");

            Region leftSpacer = new Region();
            HBox.setHgrow(leftSpacer, Priority.ALWAYS);

            Region rightSpacer = new Region();
            HBox.setHgrow(rightSpacer, Priority.ALWAYS);
            rightSpacer.setMinWidth(120);

            hb1.getChildren()
               .addAll(menuButton, leftSpacer, title, rightSpacer);
        } // -----
        this.setTop(hb1);

        VBox vb1 = new VBox(20);
        { // CENTRE
            vb1.setAlignment(Pos.CENTER);

            vb1.getChildren().add(statusLabel);

            StackPane sp2 = new StackPane();
            { // -----
                sp2.getStyleClass().add("card");
                sp2.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                sp2.setPadding(new Insets(15));
                sp2.getChildren().add(boardGraphic);
            } // -----
            vb1.getChildren().add(sp2);

            HBox hb2 = new HBox(15);
            { // -----
                hb2.setAlignment(Pos.CENTER);
                hb2.getChildren()
                   .addAll(newButton, loadButton, saveButton, resetButton);
            } // -----
            vb1.getChildren().add(hb2);
        } // -----
        this.setCenter(vb1);

        vb1 = new VBox();
        { // DROITE
            vb1.setPadding(new Insets(0, 0, 0, 30));

            StackPane sp2 = new StackPane();
            { // -----
                sp2.getStyleClass().add("card");
                sp2.setAlignment(Pos.TOP_CENTER);

                ScrollPane sp3 = new ScrollPane();
                { // -----
                    sp3.setFitToWidth(true);
                    sp3.setPrefWidth(180);
                    sp3.setStyle(
                            "-fx-background: transparent; -fx-background-color: transparent;");
                    sp3.setBorder(Border.EMPTY);

                    VBox vb4 = new VBox(15);
                    { // -----
                        vb4.setPadding(new Insets(10));
                        vb4.setAlignment(Pos.TOP_CENTER);

                        for (VehicleType type : VehicleType.values()) {
                            VehiclePaletteItem item =
                                    new VehiclePaletteItem(buildEngine, type,
                                                           boardGraphic);
                            vb4.getChildren().add(item);
                        }
                    } // -----
                    sp3.setContent(vb4);
                } // -----
                sp2.getChildren().add(sp3);
            } // -----
            vb1.getChildren().add(sp2);
        } // -----
        this.setRight(vb1);
    }

    /**
     * Met à jour l'état des boutons de contrôle en fonction de l'état du moteur
     * de construction.
     */
    private void updateControls() {
        boolean loaded = buildEngine.isLoaded();
        boolean valid = loaded && buildEngine.isValid();
        boolean solvable = valid && buildEngine.isSolvable();

        saveButton.setDisable(!solvable);
        resetButton.setDisable(!loaded);

        if (!loaded) {
            statusLabel.setText("EN ATTENTE");
            statusLabel.getStyleClass()
                       .setAll("status-label", "status-waiting");
        } else if (!valid) {
            statusLabel.setText("CONFIGURATION INVALIDE");
            statusLabel.getStyleClass()
                       .setAll("status-label", "status-unsolvable");
        } else if (solvable) {
            statusLabel.setText("NIVEAU SOLVABLE (PRÊT)");
            statusLabel.getStyleClass()
                       .setAll("status-label", "status-solvable");
        } else {
            statusLabel.setText("NIVEAU INSOLUBLE");
            statusLabel.getStyleClass()
                       .setAll("status-label", "status-unsolvable");
        }
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
                    } catch (IOException e) {
                        GuiUtils.showError("Erreur de chargement",
                                           "Impossible de charger le fichier " +
                                           ":\n" +
                                           e.getMessage());
                    } catch (Exception e) {
                        GuiUtils.showError("Erreur de chargement",
                                           "Le format du fichier est " +
                                           "invalide.");
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
                    } catch (IOException e) {
                        GuiUtils.showError("Erreur de sauvegarde",
                                           "Impossible de sauvegarder le " +
                                           "fichier :\n" +
                                           e.getMessage());
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

    /**
     * Rafraîchit l'affichage du plateau de jeu en fonction de l'état actuel du
     * moteur de construction. Supprime tous les éléments graphiques existants
     * du plateau, puis recrée les éléments graphiques pour chaque véhicule
     * présent sur le plateau. Met à jour également les contrôles de la vue en
     * fonction de l'état du moteur de construction.
     */
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
