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
import jrush.app.model.GameEngine;
import jrush.app.model.Vehicle;
import jrush.app.model.logic.LevelHandler;
import jrush.app.view.board.BoardGraphic;
import jrush.app.view.vehicle.VehicleGraphic;
import util.Contract;

import java.io.IOException;
import java.io.InputStream;

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
        this.homeButton = new Button("MENU");
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
        setPadding(new Insets(20));
        setPrefSize(800, 600);

        homeButton.setPrefWidth(120);

        grid.setPrefColumns(4);
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setMinWidth(600);
        grid.setPrefWidth(600);
        grid.setMaxWidth(600);
        grid.setAlignment(Pos.TOP_CENTER);
    }

    /**
     * Place les composants graphiques dans la vue.
     */
    private void placeComponents() {
        HBox hb1 = new HBox();
        { // HAUT
            hb1.setAlignment(Pos.CENTER_LEFT);

            Label title = new Label("NIVEAUX CLASSIQUES");
            title.setStyle("-fx-font-weight: bold; -fx-font-size: 1.5em;");
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
            sp1.setStyle("-fx-background-color:transparent;");

            StackPane sp2 = new StackPane();
            { // -----
                sp2.setAlignment(Pos.TOP_CENTER);
                sp2.getChildren().add(grid);
            } // -----

            sp1.setContent(sp2);
        } // -----
        this.setCenter(sp1);
    }

    private void createLevelGrid() {
        for (int i = 1; i <= CLASSIC_LEVEL_NB; i++) {
            String fileName = "level" + i + LevelHandler.EXTENSION;
            String path = "/jrush/app/levels/" + fileName;
            grid.getChildren().add(buildButton(i, path));
        }
    }

    private VBox createLevelCard(int index, String path) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);

        card.setMinWidth(135);
        card.setPrefWidth(135);
        card.setMaxWidth(135);

        card.setStyle(
                "-fx-background-color: #ffffff; " +
                "-fx-border-color: #dcdde1; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8;"
        );

        try (InputStream inputStream = getClass().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException();
            }

            LevelHandler.LoadResult res = LevelHandler.loadBoard(inputStream);

            StackPane previewContainer = new StackPane();
            BoardGraphic miniBoard = new BoardGraphic();
            for (Vehicle v : res.board().getVehicles()) {
                miniBoard.getChildren().add(new VehicleGraphic(v));
            }

            miniBoard.setScaleX(0.21);
            miniBoard.setScaleY(0.21);
            miniBoard.setDisable(true);

            previewContainer.setPrefSize(120, 120);
            previewContainer.getChildren().add(miniBoard);

            Button playButton = buildButton(index, path);

            card.getChildren().addAll(previewContainer, playButton);
        } catch (IOException e) {
            card.getChildren().add(new Label("Erreur"));
        }
        return card;
    }

    private Button buildButton(int index, String path) {
        Button playButton = new Button("NIVEAU " + index);
        playButton.setPrefWidth(120);

        playButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    gameEngine.loadBoard(
                            getClass().getResourceAsStream(path));
                    navigator.showGame();
                } catch (IOException ignored) {
                }
            }
        });
        return playButton;
    }

    private void connectControllers() {
        homeButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                navigator.showHome();
            }
        });
    }
}
