package jrush.app.gui;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import jrush.app.model.BuildEngine;
import jrush.app.model.GameEngine;
import jrush.app.model.logic.StdBuildEngine;
import jrush.app.model.logic.StdGameEngine;
import jrush.app.view.BuildView;
import jrush.app.view.GameView;
import jrush.app.view.HomeView;
import jrush.app.view.LevelsView;
import jrush.app.util.Contract;

/**
 * Classe qui gère la navigation entre les différentes vues de l'application.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          – Stage stage
 *      Préconditions :
 *          – stage != null
 * </pre>
 */
public class ViewNavigator {

    // CONSTANTES

    private static final String APPLICATION_NAME = "JRush";

    public static final String CSS_PATH = "/jrush/app/css/style.css";
    private static final String FONT_PATH =
            "/jrush/app/fonts/Inter-Regular" + ".ttc";

    private static final double WIDTH = 1024;
    private static final double HEIGHT = 768;

    // ATTRIBUTS

    private final Stage stage;

    private final GameEngine gameEngine;
    private final BuildEngine buildEngine;

    // CONSTRUCTEURS

    public ViewNavigator(Stage stage) {
        Contract.checkCondition(stage != null, "stage == null");

        // MODÈLE
        this.gameEngine = new StdGameEngine();
        this.buildEngine = new StdBuildEngine();

        // VUE
        this.stage = stage;
        setupStage();
    }

    // COMMANDES

    /**
     * Affiche la vue d'accueil de l'application.
     */
    public void showHome() {
        displayView(new HomeView(this, gameEngine));
    }

    /**
     * Affiche la vue de jeu de l'application.
     *
     * <pre>
     * Préconditions :
     *      gameEngine.isLoaded()
     * </pre>
     */
    public void showGame() {
        Contract.checkCondition(gameEngine.isLoaded(),
                                "!gameEngine.isLoaded" + "()");
        displayView(new GameView(this, gameEngine));
    }

    /**
     * Affiche la vue de construction de niveau de l'application.
     */
    public void showLevels() {
        displayView(new LevelsView(this, gameEngine));
    }

    /**
     * Affiche la vue de construction de niveau de l'application.
     */
    public void showBuild() {
        buildEngine.newBoard();
        displayView(new BuildView(this, buildEngine));
    }

    /**
     * Permet de changer le titre de la fenêtre selon le contexte.
     *
     * @param subTitle le sous-titre à afficher après le nom de l'application
     */
    public void updateTitle(String subTitle) {
        stage.setTitle(APPLICATION_NAME + " - " + subTitle);
    }

    // OUTILS

    /**
     * Configure les propriétés de la fenêtre principale de l'application.
     */
    private void setupStage() {
        stage.setTitle(APPLICATION_NAME);
        stage.setResizable(false);

        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Font.loadFont(getClass().getResourceAsStream(FONT_PATH), 14);
        scene.getStylesheets()
             .add(getClass().getResource(CSS_PATH).toExternalForm());
        stage.setScene(scene);
    }

    /**
     * Affiche la vue spécifiée dans la fenêtre principale de l'application.
     *
     * @param view la vue à afficher
     */
    private void displayView(final Pane view) {
        view.setPrefSize(WIDTH, HEIGHT);
        view.setOpacity(0);
        stage.getScene().setRoot(view);

        stage.sizeToScene();
        stage.centerOnScreen();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.sizeToScene();
                stage.centerOnScreen();

                FadeTransition ft =
                        new FadeTransition(Duration.millis(400), view);
                ft.setFromValue(0.0);
                ft.setToValue(1.0);
                ft.play();
            }
        });

        if (!stage.isShowing()) {
            stage.show();
        }
    }
}
