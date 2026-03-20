package jrush.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import jrush.gui.ViewNavigator;
import jrush.model.GameEngine;
import jrush.model.StdGameEngine;
import jrush.model.Vehicle;
import util.Contract;

import java.io.IOException;


/**
 * Vue principale du jeu, qui affiche le plateau de jeu et les véhicules.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          ViewNavigator navigator
 *      Préconditions :
 *          navigator != null
 * </pre>
 */
public class GameView extends Pane {

    // ATTRIBUTS

    private final GameEngine engine;

    private final ViewNavigator navigator;
    private final BoardGraphic boardGraphic;

    // CONSTRUCTEURS

    public GameView(ViewNavigator navigator) {
        Contract.checkCondition(navigator != null, "navigator == null");

        // MODÈLE
        this.engine = new StdGameEngine();

        // temporaire
        try {
            this.engine.loadBoard("/home/neil/Téléchargements/test.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // VUE
        this.navigator = navigator;
        this.boardGraphic = new BoardGraphic();

        placeComponents();

        // CONTRÔLEUR
        connectControllers();

    }

    // REQUÊTES

    // COMMANDES

    // OUTILS

    private void placeComponents() {
        this.getChildren().add(boardGraphic);
        for (Vehicle v : engine.getBoard().getVehicles()) {
            VehicleGraphic vg = new VehicleGraphic(v);
            this.getChildren().add(vg);
        }
    }

    private void connectControllers() {

    }


}
