package jrush.app.model;

import jrush.app.model.components.VehicleType;
import jrush.app.model.util.Move;
import jrush.app.util.Position;

import java.beans.PropertyVetoException;
import java.util.List;
import java.util.Map;

/**
 * Un plateau est une grille de GRID_SIZE * GRID_SIZE cases. Elle contient une
 * liste de véhicules positionnés sur celle-ci.
 *
 * <pre>
 * Constructeur :
 *      Postconditions :
 *          – getVehicles().size() == 0
 *          – findVehicle(any) == null
 *          – getInitialPositions().size() == 0
 *          – getHistory().size() == 0
 *          – canUndo() == canRedo() == false
 *          – checkWinCondition() == false
 * Constructeur :
 *      Entrée :
 *          – Board other
 *      Préconditions :
 *          – other != null
 *      Postconditions :
 *          – getVehicles().size() == other.getVehicles().size()
 *          – getInitialPositions().equals(other.getInitialPositions())
 *          – getHistory().size() == 0
 *          – canUndo() == canRedo() == false
 *          – forall v dans getVehicles(),
 *              v != other.findVehicle(v.getId())
 *          – forall v dans getVehicles(),
 *              v.getPosition().equals(
 *                  other.findVehicle(v.getId()).getPosition())
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
     * Retourne le véhicule correspondant au type donné, ou null si aucun
     * véhicule de ce type n'est présent sur le plateau.
     *
     * @param type Le type de véhicule à rechercher
     *
     * @return Le véhicule correspondant au type donné, ou null si aucun
     * véhicule de ce type n'est présent sur le plateau.
     */
    Vehicle findVehicle(VehicleType type);

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
     * @param oldPosition La position actuelle du véhicule
     * @param newPosition La position proposée pour le véhicule
     *
     * <pre>
     * Préconditions :
     *      vehicle != null
     *      oldPosition != null
     *      newPosition != null
     * </pre>
     *
     * @return true si le déplacement est valide, false sinon.
     */
    boolean canVehicleMove(
            Vehicle vehicle, Position oldPosition, Position newPosition);

    /**
     * Vérifie si les conditions de victoire sont remplies, c'est-à-dire si le
     * véhicule rouge est positionné à la sortie.
     *
     * @return true si les conditions de victoire sont remplies, false sinon.
     */
    boolean checkWinCondition();

    // COMMANDES

    /**
     * Tente d'ajouter un véhicule au plateau. Le véhicule doit être positionné
     * à une position valide selon les règles du jeu, sinon une exception est
     * levée.
     *
     * <pre>
     * Préconditions :
     *      vehicle != null
     * </pre>
     *
     * @param vehicle le véhicule à ajouter
     *
     * @throws IllegalArgumentException Si la position du véhicule est
     * interdite.
     */
    void addVehicle(Vehicle vehicle);

    /**
     * Supprime un véhicule du plateau.
     *
     * <pre>
     * Préconditions :
     *      vehicle != null
     * </pre>
     *
     * @param vehicle le véhicule à supprimer
     */
    void removeVehicle(Vehicle vehicle);

    /**
     * Définit la liste des mouvements effectués sur le plateau.
     *
     * @param history La liste des mouvements à définir, dans l'ordre
     * chronologique
     */
    void setHistory(List<Move> history);

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
