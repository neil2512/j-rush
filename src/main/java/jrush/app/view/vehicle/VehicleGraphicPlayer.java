package jrush.app.view.vehicle;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import jrush.app.model.GameEngine;
import jrush.app.model.Vehicle;
import jrush.app.model.util.Move;
import jrush.app.view.board.BoardGraphic;
import util.Contract;

/**
 * Classe qui représente graphiquement un véhicule sur le plateau de jeu et
 * qui permet de le déplacer en mode joueur.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          GameEngine engine
 *          Vehicle vehicle
 *      Préconditions :
 *          engine != null
 *          vehicle != null
 * </pre>
 */
public class VehicleGraphicPlayer extends VehicleGraphic {

    // ATTRIBUTS

    private final GameEngine engine;

    private double anchorX;
    private double anchorY;
    private int gridAnchor;

    // CONSTRUCTEURS

    public VehicleGraphicPlayer(GameEngine engine, Vehicle vehicle) {
        super(vehicle);
        Contract.checkCondition(engine != null, "engine == null");
        this.engine = engine;
        connectControllers();
    }

    // OUTILS

    /**
     * Connecte les contrôleurs de l'élément graphique.
     */
    private void connectControllers() {
        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                anchorX = event.getSceneX();
                anchorY = event.getSceneY();

                gridAnchor =
                        vehicle.isHorizontal() ? vehicle.getPosition().getX()
                                               : vehicle.getPosition().getY();
            }
        });

        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double dragDistance =
                        vehicle.isHorizontal() ? (event.getSceneX() - anchorX)
                                               : (event.getSceneY() - anchorY);
                int gridOffset =
                        (int) Math.round(dragDistance / BoardGraphic.CELL_SIZE);

                int targetCoord = gridAnchor + gridOffset;
                int currentCoord =
                        vehicle.isHorizontal() ? vehicle.getPosition().getX()
                                               : vehicle.getPosition().getY();

                int delta = targetCoord - currentCoord;

                if (delta != 0) {
                    try {
                        engine.moveVehicle(vehicle, delta);
                    } catch (java.beans.PropertyVetoException ignored) {
                    }
                }
            }
        });

        this.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int finalGridPos =
                        vehicle.isHorizontal() ? vehicle.getPosition().getX()
                                               : vehicle.getPosition().getY();

                int totalDelta = finalGridPos - gridAnchor;
                if (totalDelta != 0) {
                    engine.recordBoardMove(new Move(vehicle, totalDelta));
                }
            }
        });
    }
}
