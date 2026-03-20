package jrush.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jrush.gui.ViewNavigator;
import util.Contract;


/**
 * Vue d'accueil de l'application.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          ViewNavigator navigator
 *      Préconditions :
 *          navigator != null
 * </pre>
 */
public class HomeView extends VBox {

    // CONSTANTES

    private static final double SPACING = 20;

    // ATTRIBUTS

    private final ViewNavigator navigator;
    private final Button playButton;

    // CONSTRUCTEURS

    public HomeView(ViewNavigator navigator) {
        Contract.checkCondition(navigator != null, "navigator == null");

        // MODÈLE

        // VUE
        this.navigator = navigator;
        playButton = new Button("JOUER");

        setProperties();
        placeComponents();

        // CONTRÔLEUR
        connectControllers();

    }

    // REQUÊTES

    // COMMANDES

    // OUTILS

    private void setProperties() {
        setAlignment(Pos.CENTER);
        setSpacing(SPACING);
    }

    private void placeComponents() {
        Label title = new Label("JRUSH");
        getChildren().addAll(title, playButton);
    }

    private void connectControllers() {
        playButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                navigator.showGame();
            }
        });
    }
}
