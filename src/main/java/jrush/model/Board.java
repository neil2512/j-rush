package jrush.model;

import jrush.util.Move;
import jrush.util.Position;

import java.beans.PropertyVetoException;
import java.util.List;
import java.util.Map;

/**
 * Un plateau est une grille de GRID_SIZE*GRID_SIZE cases. Elle contient une
 * liste de véhicules positionnés sur celle-ci.
 * <pre>
 * Constructeur :
 *      Postconditions :
 *          getVehicles().size() == 0
 * Constructeur :
 *      Entrée :
 *              Board other
 *      Préconditions :
 *              other != null
 *      Postconditions :
 *              getVehicles() contient une copie des vehicules contenus dans
 *                  other.getVehicles()
 *
 * </pre>
 */
public interface Board {

    // CONSTANTES

    int GRID_SIZE = 6;
    Position EXIT_POSITION = new Position(GRID_SIZE - 1, (GRID_SIZE / 2) - 1);

    // REQUÊTES

    /**
     * Retourne la liste des véhicules présents sur le plateau.
     *
     * @return Une copie de la liste des véhicules actuellement sur la grille.
     */
    List<Vehicle> getVehicles();

    /**
     * Retourne une map associant les identifiants des véhicules à leurs
     * positions initiales sur le plateau.
     *
     * @return Une map associant les identifiants des véhicules à leurs
     * positions initiales sur le plateau.
     */
    Map<String, Position> getInitialPositions();

    /**
     * Retourne la liste des mouvements effectués sur le plateau.
     *
     * @return Une copie de la liste des mouvements effectués sur le plateau,
     * dans l'ordre chronologique.
     */
    List<Move> getHistory();

    /**
     * Définit la liste des mouvements effectués sur le plateau.
     *
     * @param history La liste des mouvements à définir, dans l'ordre chronologique.
     */
    void setHistory(List<Move> history);

    /**
     * Retourne true si un mouvement peut être annulé, false sinon.
     *
     * @return true si un mouvement peut être annulé, false sinon.
     */
    boolean canUndo();

    /**
     * Retourne true si un mouvement peut être refait, false sinon.
     *
     * @return true si un mouvement peut être refait, false sinon.
     */
    boolean canRedo();

    /**
     * Vérifie si un véhicule peut se déplacer d'une position à une autre selon
     * les règles du jeu.
     *
     * @param vehicle Le véhicule à déplacer
     * @param oldPos La position actuelle du véhicule
     * @param newPos La position proposée pour le véhicule
     *
     * <pre>
     * Préconditions :
     *      vehicle != null
     *      oldPos != null
     *      newPos != null
     * </pre>
     *
     * @return true si le déplacement est valide, false sinon
     */
    boolean canVehicleMove(
            Vehicle vehicle, Position oldPos,
            Position newPos
    );

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

    /**
     * Enregistre un mouvement dans l'historique du plateau.
     *
     * <pre>
     * Préconditions :
     *      move != null
     * </pre>
     *
     * @param move le mouvement à enregistrer
     */
    void record(Move move);

    /**
     * Annule le dernier mouvement effectué. Le mouvement annulé doit être
     * valide selon les règles du jeu, sinon une exception est levée.
     *
     * <pre>
     * Préconditions :
     *      canUndo()
     * </pre>
     *
     * @throws PropertyVetoException Si le mouvement annulé n'est pas valide
     * selon les règles du jeu.
     */
    void undo() throws PropertyVetoException;

    /**
     * Refait le dernier mouvement annulé. Le mouvement refait doit être valide
     * selon les règles du jeu, sinon une exception est levée.
     *
     * <pre>
     * Préconditions :
     *      canRedo()
     * </pre>
     *
     * @throws PropertyVetoException Si le mouvement refait n'est pas valide
     * selon les règles du jeu.
     */
    void redo() throws PropertyVetoException;

    /**
     * Réinitialise le plateau de jeu en remettant tous les véhicules à leur
     * position initiale.
     */
    void reset();
}
