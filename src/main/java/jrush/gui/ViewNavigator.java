package jrush.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jrush.view.GameView;
import jrush.view.HomeView;
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
    private Scene scene;

    // CONSTRUCTEURS

    public ViewNavigator(Stage stage) {
        Contract.checkCondition(stage != null, "stage == null");

        // MODÈLE

        // VUE
        this.stage = stage;
        stage.setTitle("JRush");

        // CONTRÔLEUR

    }

    // REQUÊTES


    // COMMANDES

    /**
     * Affiche la vue d'accueil de l'application.
     */
    public void showHome() {
        HomeView homeView = new HomeView(this);
        Scene scene = new Scene(homeView, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Affiche la vue de jeu de l'application.
     */
    public void showGame() {
        GameView gameView = new GameView(this);
        Scene scene = new Scene(gameView, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    // OUTILS
}
