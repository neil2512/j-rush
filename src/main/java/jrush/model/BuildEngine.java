package jrush.model;


import jrush.util.Position;

import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
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

    String PROP_BOARD = "board";

    // REQUÊTES

    /**
     * Retourne le plateau de jeu actuellement chargé. Si aucun plateau de jeu
     * n'est chargé, retourne null.
     *
     * @return Le plateau de jeu actuellement chargé, ou null si aucun plateau
     * de jeu n'est chargé.
     */
    Board getBoard();

    /**
     * Retourne true si un plateau de jeu est chargé, false sinon.
     *
     * @return true si un plateau de jeu est chargé, false sinon.
     */
    boolean isLoaded();

    PropertyChangeListener[] getPropertyChangeListeners();

    // COMMANDES

    /**
     * Crée un nouveau plateau vide.
     */
    void newBoard();


    /**
     * Ajoute un véhicule sur le plateau.
     * <pre>
     * Préconditions :
     *      isLoaded()
     *      vehicle != null
     * </pre>
     */
    void addVehicleOnBoard(Vehicle vehicle);

    /**
     * Supprime un véhicule du plateau.
     * <pre>
     * Préconditions :
     *      isLoaded()
     *      vehicle != null
     *      getBoard.getVehicles().contains(vehicle)
     * </pre>
     */
    void removeVehicleFromBoard(Vehicle vehicle);

    /**
     * Déplace et/ou tourne un véhicule atomiquement.
     * Si l'opération est invalide, rien n'est modifié.
     * <pre>
     * Préconditions :
     *      isLoaded()
     *      vehicle != null
     *      position != null
     * </pre>
     */
    void rotateAndMove(Vehicle vehicle, Position position, boolean horizontal)
            throws PropertyVetoException;

    /**
     * Déplace un véhicule vers une position libre du plateau.
     * <pre>
     * Préconditions :
     *      isLoaded()
     *      vehicle != null
     *      position != null
     *      getBoard.getVehicles().contains(vehicle)
     * </pre>
     */
    void moveVehicleOnBoard(Vehicle vehicle, Position position)  throws PropertyVetoException;

    /**
     * Charge un plateau depuis un fichier.
     * <pre>
     * Préconditions :
     *      filename != null
     * </pre>
     */
    void loadBoard(String filename) throws IOException;

    /**
     * Sauvegarde le plateau dans un fichier.
     * <pre>
     * Préconditions :
     *      filename != null
     *      isLoaded()
     * </pre>
     */
    void saveBoard(String filename) throws IOException;

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);
}
