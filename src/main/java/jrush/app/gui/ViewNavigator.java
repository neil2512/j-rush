package jrush.app.gui;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jrush.app.model.BuildEngine;
import jrush.app.model.GameEngine;
import jrush.app.model.logic.StdBuildEngine;
import jrush.app.model.logic.StdGameEngine;
import jrush.app.view.BuildView;
import jrush.app.view.GameView;
import jrush.app.view.HomeView;
import jrush.app.view.LevelsView;
import util.Contract;

/**
 * Classe qui gère la navigation entre les différentes vues de l'application.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          Stage stage
 *      Préconditions :
 *          stage != null
 * </pre>
 */
public class ViewNavigator {

    // ATTRIBUTS

    private final Stage stage;

    private final GameEngine gameEngine;
    private final BuildEngine buildEngine;

    // CONSTRUCTEURS

    public ViewNavigator(Stage stage) {
        Contract.checkCondition(stage != null, "stage == null");

        // MODÈLE
        gameEngine = new StdGameEngine();
        buildEngine = new StdBuildEngine();

        // VUE
        this.stage = stage;
        stage.setTitle("JRush");

        Scene scene = new Scene(new Pane());
        stage.setScene(scene);

        stage.setMinWidth(800);
        stage.setMinHeight(600);

        stage.setResizable(true);
        stage.show();
    }

    // COMMANDES

    /**
     * Affiche la vue d'accueil de l'application.
     */
    public void showHome() {
        HomeView homeView = new HomeView(this, gameEngine);
        stage.getScene().setRoot(homeView);
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    /**
     * Affiche la vue de jeu de l'application.
     *
     */
    public void showGame() {
        if (!gameEngine.isLoaded()) {
            System.err.println("ERREUR : le moteur de jeu n'est pas chargé !");
            showHome();
            return;
        }

        GameView gameView = new GameView(this, gameEngine);
        stage.getScene().setRoot(gameView);
        stage.sizeToScene();
        stage.centerOnScreen();

    }

    /**
     * Affiche la vue de construction de niveau de l'application.
     */
    public void showLevels() {
        LevelsView levelView = new LevelsView(this, gameEngine);
        stage.getScene().setRoot(levelView);
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    /**
     * Affiche la vue de construction de niveau de l'application.
     */
    public void showBuild() {
        buildEngine.newBoard();
        BuildView buildView = new BuildView(this, buildEngine);
        stage.getScene().setRoot(buildView);
        stage.sizeToScene();
        stage.centerOnScreen();
    }

}
