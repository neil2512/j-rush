package jrush.app.view.vehicle;


import javafx.scene.shape.Rectangle;
import jrush.app.model.Vehicle;
import jrush.app.view.board.BoardGraphic;
import jrush.app.util.Contract;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Classe qui représente graphiquement un véhicule sur le plateau de jeu. Ne
 * connecte pas ses contrôleurs.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          – Vehicle vehicle
 *      Préconditions :
 *          – vehicle != null
 * </pre>
 */
public class VehicleGraphic extends Rectangle {

    // CONSTANTES

    private static final double MARGIN = 6.0;

    // ATTRIBUTS

    protected final Vehicle vehicle;

    // CONSTRUCTEURS

    public VehicleGraphic(Vehicle vehicle) {
        Contract.checkCondition(vehicle != null, "vehicle == null");
        this.vehicle = vehicle;

        this.getStyleClass().add("vehicle-graphic");

        this.setArcWidth(16);
        this.setArcHeight(16);

        paint();
    }

    // OUTILS

    /**
     * Configure l'apparence du rectangle en fonction des propriétés du véhicule
     * et qui positionne le rectangle à la bonne place sur le plateau de jeu.
     */
    protected void paint() {
        if (vehicle.isHorizontal()) {
            setWidth((vehicle.getSize() * BoardGraphic.CELL_SIZE) -
                     (2 * MARGIN));
            setHeight(BoardGraphic.CELL_SIZE - (2 * MARGIN));
        } else {
            setWidth(BoardGraphic.CELL_SIZE - (2 * MARGIN));
            setHeight((vehicle.getSize() * BoardGraphic.CELL_SIZE) -
                      (2 * MARGIN));
        }

        setFill(vehicle.getColor());
        updatePosition();
    }

    /**
     * Met à jour la position du rectangle en fonction de la position du
     * véhicule sur le plateau de jeu.
     */
    protected void updatePosition() {
        setX((vehicle.getPosition().getX() * BoardGraphic.CELL_SIZE) + MARGIN);
        setY((vehicle.getPosition().getY() * BoardGraphic.CELL_SIZE) + MARGIN);
    }

    /**
     * Connecte les contrôleurs de base de l'élément graphique.
     */
    protected void connectBaseControllers() {
        vehicle.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(Vehicle.PROP_POSITION)) {
                    updatePosition();
                }
                if (evt.getPropertyName().equals(Vehicle.PROP_PLACEMENT)) {
                    paint();
                }
            }
        });
    }
}