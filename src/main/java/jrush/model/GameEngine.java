package jrush.model;

import jrush.util.Move;

import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.IOException;

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
    boolean canUndoBoardMove();


    /**
     * Retourne true si un mouvement peut être refait, false sinon.
     *
     * @return true si un mouvement peut être refait, false sinon.
     */
    boolean canRedoBoardMove();

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
     * Enregistre un mouvement dans l'historique du plateau de jeu.
     *
     * <pre>
     * Préconditions :
     *      move != null
     * </pre>
     *
     * @param move Le mouvement à enregistrer.
     */
    void recordBoardMove(Move move);


    /**
     * Annule le dernier mouvement effectué sur le plateau de jeu. Le mouvement
     * annulé doit être valide selon les règles du jeu, sinon une exception est
     * levée.
     *
     * <pre>
     * Préconditions :
     *    canUndoBoardMove()
     * </pre>
     *
     * @throws PropertyVetoException Si le mouvement annulé n'est pas valide
     * selon les règles du jeu.
     */
    void undoBoardMove() throws PropertyVetoException;


    /**
     * Refait le dernier mouvement annulé sur le plateau de jeu. Le mouvement
     * refait doit être valide selon les règles du jeu, sinon une exception est
     * levée.
     *
     * <pre>
     * Préconditions :
     *    canRedoBoardMove()
     * </pre>
     *
     * @throws PropertyVetoException Si le mouvement refait n'est pas valide
     * selon les règles du jeu.
     */
    void redoBoardMove() throws PropertyVetoException;

    /**
     * Réinitialise le plateau de jeu à son état initial tel qu'il a été
     * chargé.
     *
     * <pre>
     * Préconditions :
     *      isLoaded()
     * </pre>
     */
    void resetBoard();

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
