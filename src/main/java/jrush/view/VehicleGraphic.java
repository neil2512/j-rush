package jrush.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jrush.model.Vehicle;
import util.Contract;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.module.Configuration;

/**
 * Classe qui représente graphiquement un véhicule sur le plateau de jeu.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          Vehicle vehicle
 *      Préconditions :
 *          vehicle != null
 * </pre>
 */
public class VehicleGraphic extends Rectangle {

    // ATTRIBUTS

    private final Vehicle vehicle;

    // CONSTRUCTEURS

    public VehicleGraphic(Vehicle vehicle) {
        Contract.checkCondition(vehicle != null, "vehicle == null");
        this.vehicle = vehicle;
        paint();
        connectControllers();
    }

    // REQUÊTES

    // COMMANDES

    // OUTILS

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

    private void updatePosition() {
        setX(vehicle.getPosition().getX() * BoardGraphic.CELL_SIZE + 2);
        setY(vehicle.getPosition().getY() * BoardGraphic.CELL_SIZE + 2);
    }

    private void connectControllers() {
        vehicle.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(
                    PropertyChangeEvent propertyChangeEvent
            ) {
                updatePosition();
            }
        });
    }
}
