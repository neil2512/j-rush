package jrush.model;

import jrush.util.Position;

import java.util.List;

/**
 * Un plateau est une grille de GRID_SIZE*GRID_SIZE cases. Elle contient une
 * liste de véhicules positionnés sur celle-ci.
 * <pre>
 * Constructeur :
 *      Postconditions :
 *          getVehicles().size() == 0
 * </pre>
 */
public interface Board {

    // CONSTANTES

    int GRID_SIZE = 6;
    Position EXIT_POSITION = new Position(GRID_SIZE - 1, GRID_SIZE / 2);

    // REQUÊTES

    /**
     * Retourne la liste des véhicules présents sur le plateau.
     *
     * @return Une copie de la liste des véhicules actuellement sur la grille.
     */
    List<Vehicle> getVehicles();

    // COMMANDES

    /**
     * Ajoute un véhicule au plateau
     *
     * <pre>
     * Préconditions :
     *      vehicle != null
     * </pre>
     *
     * @param vehicle le véhicule à ajouter
     */
    void addVehicle(Vehicle vehicle);
}
