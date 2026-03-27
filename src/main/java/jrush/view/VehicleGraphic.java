package jrush.view;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import jrush.model.GameEngine;
import jrush.model.Vehicle;
import jrush.util.Move;
import util.Contract;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Classe qui représente graphiquement un véhicule sur le plateau de jeu.
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
public class VehicleGraphic extends Rectangle {

    // ATTRIBUTS

    private final GameEngine engine;
    private final Vehicle vehicle;

    private double anchorX;
    private double anchorY;
    private int gridAnchor;

    // CONSTRUCTEURS

    public VehicleGraphic(GameEngine engine, Vehicle vehicle) {
        Contract.checkCondition(engine != null, "engine == null");
        Contract.checkCondition(vehicle != null, "vehicle == null");
        this.engine = engine;
        this.vehicle = vehicle;
        paint();
        connectControllers();
    }

    // REQUÊTES

    // COMMANDES

    // OUTILS

    /**
     * Configure l'apparence du rectangle en fonction des propriétés du véhicule
     * et qui positionne le rectangle à la bonne place sur le plateau de jeu.
     */
    private void paint() {
        if (vehicle.isHorizontal()) {
            setWidth(vehicle.getSize() * BoardGraphic.CELL_SIZE - 4);
            setHeight(BoardGraphic.CELL_SIZE - 4);
        } else {
            setWidth(BoardGraphic.CELL_SIZE - 4);
            setHeight(vehicle.getSize() * BoardGraphic.CELL_SIZE - 4);
        }

        setFill(vehicle.getColor());

        updatePosition();
    }

    /**
     * Met à jour la position du rectangle en fonction de la position du
     * véhicule sur le plateau de jeu.
     */
    private void updatePosition() {
        setX(vehicle.getPosition().getX() * BoardGraphic.CELL_SIZE + 2);
        setY(vehicle.getPosition().getY() * BoardGraphic.CELL_SIZE + 2);
    }

    /**
     * Connecte les contrôleurs de l'élément graphique.
     */
    private void connectControllers() {
        vehicle.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getPropertyName().equals(Vehicle.PROP_POSITION)) {
                    updatePosition();
                }
            }
        });

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
                    engine.addMove(new Move(vehicle, totalDelta));
                }
            }
        });
    }
}
