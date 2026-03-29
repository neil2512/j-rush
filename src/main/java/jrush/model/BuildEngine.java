package jrush.model;


import java.io.IOException;

/**
 * ...
 * <pre>
 * Invariant :
 *
 * Constructeur :
 *      Entrée :
 *          ...
 *      Préconditions :
 *          ...
 *      Postconditions :
 *          ...
 * </pre>
 */
public interface BuildEngine {

    // REQUÊTES


    // COMMANDES

    void addVehicleOnBoard(Vehicle vehicle);

    void removeVehicleFromBoard(Vehicle vehicle);

    void loadBoard(String filename) throws IOException;

    void saveBoard(String filename) throws IOException;

}
