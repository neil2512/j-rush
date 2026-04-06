package jrush.app.model.util;

import jrush.app.util.Position;
import util.Contract;

/**
 * Cette classe représente le placement d'un véhicule sur un plateau, défini par
 * une position et une orientation.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          – Position position
 *          – boolean horizontal
 *      Préconditions :
 *          – position != null
 *      Postconditions :
 *          – getPosition().equals(position)
 *          – isHorizontal() == horizontal
 * </pre>
 */
public record Placement(Position position, boolean horizontal) {

    // CONSTRUCTEURS

    public Placement {
        Contract.checkCondition(position != null, "position == null");
    }

}
