package jrush.model;

import jrush.util.Move;

import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.List;

/**
 * Le moteur de jeu est l'interface principale du modèle. Il permet de charger
 * un plateau de jeu, de sélectionner un véhicule et de le déplacer. Il fournit
 * également des méthodes pour vérifier l'état du jeu, comme le nombre de
 * mouvements effectués et si un véhicule est sélectionné. Le moteur de jeu est
 * responsable de la logique du jeu et de la validation des mouvements, en
 * s'assurant que les règles du jeu sont respectées.
 *
 * <pre>
 *     Invariant :
 *          isLoaded() <==> getBoard() != null
 * </pre>
 */
public interface GameEngine {

    // CONSTANTES

    String PROP_MOVECOUNT = "move_count";

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
     * Retourne une liste des mouvements effectués depuis le chargement du
     * plateau de jeu. Si aucun plateau de jeu n'est chargé, retourne une liste
     * vide.
     *
     * @return Une liste des mouvements effectués depuis le chargement du
     * plateau de jeu, ou une liste vide si aucun plateau de jeu n'est chargé.
     */
    List<Move> getHistory();

    /**
     * Retourne true si un plateau de jeu est chargé, false sinon.
     *
     * @return true si un plateau de jeu est chargé, false sinon.
     */
    boolean isLoaded();

    /**
     * Retourne le nombre de mouvements effectués depuis le chargement du
     * plateau de jeu. Un mouvement est défini comme un déplacement valide d'un
     * véhicule.
     *
     * <pre>
     * Préconditions :
     *      isLoaded()
     * </pre>
     *
     * @return Le nombre de mouvements effectués depuis le chargement du plateau
     * de jeu.
     */
    int getMoveCount();

    /**
     * Retourne un booléen décrivant l'état de victoire de la partie
     * actuellement en cours.
     *
     * <pre>
     * Préconditions :
     *      isLoaded()
     * </pre>
     *
     * @return Le booléen qui décrit l'état de victoire
     */
    boolean checkWinCondition();

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
     * Retourne un tableau des écouteurs de changements de propriété
     * actuellement enregistrés auprès du moteur de jeu.
     *
     * @return Un tableau des écouteurs de changements de propriété actuellement
     * enregistrés auprès du moteur de jeu.
     */
    PropertyChangeListener[] getPropertyChangeListeners();

    // COMMANDES

    /**
     * Charge un plateau de jeu à partir d'un fichier.
     *
     * <pre>
     * Préconditions :
     *      filename != null
     * </pre>
     *
     * @param filename Le nom du fichier à partir duquel charger le plateau de
     * jeu.
     */
    void loadBoard(String filename) throws IOException;

    /**
     * Réinitialise le plateau de jeu en remettant tous les véhicules à leur
     * position initiale.
     *
     * <pre>
     * Préconditions :
     *      isLoaded()
     * </pre>
     */
    void resetBoard();

    /**
     * Déplace un véhicule d'une certaine distance. Le déplacement doit être
     * valide selon les règles du jeu, sinon une exception est levée.
     *
     * <pre>
     * Préconditions :
     *      isLoaded()
     *      vehicle != null
     * </pre>
     *
     * @param vehicle Le véhicule à déplacer.
     * @param delta La distance à déplacer. Un delta positif déplace le véhicule
     * dans sa direction de déplacement, tandis qu'un delta négatif le déplace
     * dans la direction opposée.
     *
     * @throws PropertyVetoException Si le déplacement n'est pas valide selon
     * les règles du jeu.
     */
    void moveVehicle(Vehicle vehicle, int delta) throws PropertyVetoException;


    /**
     * Ajoute un mouvement à l'historique des mouvements du moteur de jeu.
     *
     * @param move Le mouvement à ajouter à l'historique des mouvements du
     * moteur de jeu.
     */
    void addMove(Move move);

    /**
     * Annule le dernier mouvement effectué. Le mouvement annulé doit être
     * valide selon les règles du jeu, sinon une exception est levée.
     * <pre>
     * Préconditions :
     *      canUndo()
     * </pre>
     *
     * @throws PropertyVetoException Si le mouvement annulé n'est pas valide
     * selon les règles du jeu.
     */
    void undoMove() throws PropertyVetoException;

    /**
     * Refait le dernier mouvement annulé. Le mouvement refait doit être valide
     * selon les règles du jeu, sinon une exception est levée.
     * <pre>
     * Préconditions :
     *      canRedo()
     * </pre>
     *
     * @throws PropertyVetoException Si le mouvement refait n'est pas valide
     * selon les règles du jeu.
     */
    void redoMove() throws PropertyVetoException;

    /**
     * Ajoute un écouteur de changements de propriété au moteur de jeu.
     *
     * <pre>
     * Préconditions :
     *      listener != null
     * </pre>
     *
     * @param listener L'écouteur de changements de propriété à ajouter.
     */
    void addPropertyChangeListener(PropertyChangeListener listener);


    /**
     * Supprime un écouteur de changements de propriété du moteur de jeu.
     *
     * <pre>
     * Préconditions :
     *      listener != null
     * </pre>
     *
     * @param listener L'écouteur de changements de propriété à supprimer.
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

}
