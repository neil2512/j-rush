package jrush.app.view.vehicle;


import javafx.scene.shape.Rectangle;
import jrush.app.model.Vehicle;
import jrush.app.view.board.BoardGraphic;
import util.Contract;

/**
 * Classe qui représente graphiquement un véhicule sur le plateau de jeu.
 * Ne connecte pas ses contrôleurs.
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

    protected final Vehicle vehicle;

    // CONSTRUCTEURS

    public VehicleGraphic(Vehicle vehicle) {
        Contract.checkCondition(vehicle != null, "vehicle == null");
        this.vehicle = vehicle;
        paint();
    }

    // OUTILS

    /**
     * Configure l'apparence du rectangle en fonction des propriétés du véhicule
     * et qui positionne le rectangle à la bonne place sur le plateau de jeu.
     */
    protected void paint() {
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
    protected void updatePosition() {
        setX(vehicle.getPosition().getX() * BoardGraphic.CELL_SIZE + 2);
        setY(vehicle.getPosition().getY() * BoardGraphic.CELL_SIZE + 2);
    }

    /**
     * Connecte les contrôleurs de base de l'élément graphique.
     */
    protected void connectBaseControllers() {
        vehicle.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals(Vehicle.PROP_POSITION)) {
                updatePosition();
            }
            if (evt.getPropertyName().equals(Vehicle.PROP_PLACEMENT)) {
                paint();
            }
        });
    }
}