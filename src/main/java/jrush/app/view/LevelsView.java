package jrush.app.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import jrush.app.gui.ViewNavigator;
import jrush.app.model.GameEngine;
import jrush.app.model.Vehicle;
import jrush.app.model.logic.LevelHandler;
import jrush.app.util.GuiUtils;
import jrush.app.view.board.BoardGraphic;
import jrush.app.view.vehicle.VehicleGraphic;
import jrush.app.util.Contract;

import java.io.IOException;
import java.io.InputStream;

/**
 * Vue affichant la liste des niveaux disponibles. Permet de lancer un niveau en
 * cliquant sur sa carte.
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
public class LevelsView extends BorderPane {

    // CONSTANTES

    private static final int CLASSIC_LEVEL_NB = 40;

    // ATTRIBUTS

    private final GameEngine gameEngine;

    private final ViewNavigator navigator;
    private final TilePane grid;

    private final Button homeButton;


    // CONSTRUCTEURS

    public LevelsView(ViewNavigator navigator, GameEngine gameEngine) {
        Contract.checkCondition(navigator != null, "navigator == null");
        Contract.checkCondition(gameEngine != null, "gameEngine == null");

        // MODELE
        this.gameEngine = gameEngine;

        // VUE
        this.navigator = navigator;
        this.homeButton = new Button("RETOUR");
        this.grid = new TilePane();

        setProperties();
        placeComponents();

        // CONTROLEUR
        connectControllers();
    }

    // OUTILS

    /**
     * Configure les propriétés de la vue.
     */
    private void setProperties() {
        setPadding(new Insets(30));

        getStyleClass().add("root");

        homeButton.setPrefWidth(120);
        homeButton.getStyleClass().add("button-secondary");

        grid.setPrefColumns(4);
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setMaxWidth(Region.USE_PREF_SIZE);
    }

    /**
     * Place les composants graphiques dans la vue.
     */
    private void placeComponents() {
        HBox hb1 = new HBox();
        { // HAUT
            hb1.setAlignment(Pos.CENTER_LEFT);
            hb1.setPadding(new Insets(0, 0, 40, 0));

            Label title = new Label("NIVEAUX CLASSIQUES");
            title.getStyleClass().add("title-section");

            Region leftSpacer = new Region();
            HBox.setHgrow(leftSpacer, Priority.ALWAYS);
            Region rightSpacer = new Region();
            HBox.setHgrow(rightSpacer, Priority.ALWAYS);
            rightSpacer.setMinWidth(120);

            hb1.getChildren()
               .addAll(homeButton, leftSpacer, title, rightSpacer);
        } // -----
        this.setTop(hb1);

        createLevelGrid();

        ScrollPane sp1 = new ScrollPane();
        { // CENTRE
            sp1.setFitToWidth(true);
            sp1.setStyle(
                    "-fx-background: transparent; -fx-background-color: transparent;");

            StackPane sp2 = new StackPane();
            { // -----
                sp2.setAlignment(Pos.TOP_CENTER);
                sp2.getChildren().add(grid);
            } // -----

            sp1.setContent(sp2);
        } // -----
        this.setCenter(sp1);
    }

    /**
     * Crée les cartes de niveaux et les ajoute à la grille. Chaque carte
     * affiche une miniature du niveau et un bouton pour le lancer. En cas
     * d'erreur de lecture du niveau, une carte d'erreur est affichée à la
     * place.
     */
    private void createLevelGrid() {
        for (int i = 1; i <= CLASSIC_LEVEL_NB; i++) {
            String fileName = "level" + i + LevelHandler.EXTENSION;
            String path = "/jrush/app/levels/" + fileName;
            grid.getChildren().add(createLevelCard(i, path));
        }
    }

    /**
     * Crée une carte de niveau. La carte affiche une miniature du niveau et un
     * bouton pour le lancer. En cas d'erreur de lecture du niveau, une carte
     * d'erreur est affichée à la place.
     *
     * @param index Le numéro du niveau
     * @param path Le chemin vers le fichier du niveau
     *
     * @return Une VBox représentant la carte du niveau
     */
    private VBox createLevelCard(int index, String path) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);

        card.getStyleClass().add("card");
        card.setPrefWidth(160);
        card.setMaxWidth(160);

        try (InputStream inputStream = getClass().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException();
            }

            LevelHandler.LoadResult res = LevelHandler.loadBoard(inputStream);

            BoardGraphic miniBoard = new BoardGraphic();
            for (Vehicle v : res.board().getVehicles()) {
                miniBoard.getChildren().add(new VehicleGraphic(v));
            }

            miniBoard.setScaleX(0.22);
            miniBoard.setScaleY(0.22);
            miniBoard.setDisable(true);

            Group scaleGroup = new Group(miniBoard);

            StackPane previewContainer = new StackPane();
            previewContainer.setPrefSize(120, 120);
            previewContainer.getChildren().add(scaleGroup);

            Button playButton = buildButton(index, path);
            card.getChildren().addAll(previewContainer, playButton);
        } catch (IOException e) {
            Label errorLabel = new Label("Niv. " + index + "\nErreur");
            errorLabel.setStyle("-fx-text-fill: red; -fx-alignment: center;");
            card.getChildren().add(errorLabel);
        }
        return card;
    }

    /**
     * Construit un bouton pour lancer un niveau. Lorsque le bouton est cliqué,
     * le niveau correspondant est chargé dans le moteur de jeu et la vue de jeu
     * est affichée. En cas d'erreur de lecture du niveau, un message d'erreur
     * est affiché.
     *
     * @param index Le numéro du niveau
     * @param path Le chemin vers le fichier du niveau
     *
     * @return Un Button configuré pour lancer le niveau
     */
    private Button buildButton(int index, String path) {
        Button playButton = new Button("NIVEAU " + index);
        playButton.getStyleClass().add("button");
        playButton.setMaxWidth(Double.MAX_VALUE);

        playButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    InputStream is = getClass().getResourceAsStream(path);
                    if (is == null) {
                        throw new IOException("Stream null");
                    }
                    gameEngine.loadBoard(is);
                    navigator.showGame();
                } catch (IOException e) {
                    GuiUtils.showError("Erreur de lecture", e.getMessage());
                }
            }
        });
        return playButton;
    }

    /**
     * Connecte les contrôleurs aux composants graphiques.
     */
    private void connectControllers() {
        homeButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                navigator.showHome();
            }
        });
    }
}
