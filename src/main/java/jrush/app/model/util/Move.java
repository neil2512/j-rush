package jrush.app.model.util;


import jrush.app.model.Vehicle;
import util.Contract;

import java.beans.PropertyVetoException;

/**
 * Cette classe représente un déplacement d'un véhicule dans le jeu.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          Vehicle vehicle
 *          int delta
 *      Préconditions :
 *          vehicle != null
 *          delta != 0
 * </pre>
 */
public class Move {

    // ATTRIBUTS

    private final Vehicle vehicle;
    private final int delta;

    // CONSTRUCTEURS

    public Move(Vehicle vehicle, int delta) {
        Contract.checkCondition(vehicle != null, "vehicle == null");
        Contract.checkCondition(delta != 0, "delta == 0");
        this.vehicle = vehicle;
        this.delta = delta;
    }

    // REQUÊTES

    public String toString() {
        return vehicle + ";" + delta;
    }

    // COMMANDES

    /**
     * Tente d'effectuer le déplacement du véhicule.
     *
     * @throws PropertyVetoException si le déplacement est invalide.
     */
    public void redo() throws PropertyVetoException {
        vehicle.move(delta);
    }

    /**
     * Tente d'annuler le déplacement du véhicule.
     *
     * @throws PropertyVetoException si l'annulation du déplacement est
     * invalide.
     */
    public void undo() throws PropertyVetoException {
        vehicle.move(-delta);
    }
}
