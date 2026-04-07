package jrush.app.model;

import jrush.app.model.util.Move;

import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Interface représentant le moteur de jeu du jeu. Elle permet de charger un
 * plateau de jeu, de déplacer des véhicules, d'annuler et de refaire des
 * mouvements, et de vérifier les conditions de victoire.
 *
 * <pre>
 * Invariant :
 *      – getBoard() == null <==> !isLoaded()
 * Constructeur :
 *      Postconditions :
 *          – getBoard() == null
 *          – !isLoaded()
 *          - canUndoBoardMove() == canRedoBoardMove() == false
 *          – getPropertyChangeListeners().length == 0
 * </pre>
 */
public interface GameEngine {

    // CONSTANTES

    String PROP_MOVECOUNT = "move_count";
    String PROP_TIMER = "timer";

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
     * Retourne le temps écoulé en secondes depuis le chargement du plateau de
     * jeu.
     *
     * @return Le temps écoulé en secondes depuis le chargement du plateau de
     * jeu.
     */
    int getTime();

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
     * Tente de charger un plateau à partir d'un fichier. Le fichier doit être
     * au format correct et contenir une configuration de plateau de jeu valide,
     * sinon une exception est levée. En cas de réussite, les écouteurs de
     * changements de propriété sont notifiés.
     *
     * <pre>
     * Préconditions :
     *      filename != null
     * </pre>
     *
     * @param filename Le nom du fichier à partir duquel charger le plateau
     *
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de la
     * lecture du fichier, ou si le fichier n'existe pas.
     */
    void loadBoard(String filename) throws IOException;

    /**
     * Tente de charger un plateau à partir d'un flux d'entrée. Le flux doit
     * être au format correct et contenir une configuration de plateau de jeu
     * valide, sinon une exception est levée. En cas de réussite, les écouteurs
     * de changements de propriété sont notifiés.
     *
     * <pre>
     * Préconditions :
     *      inputStream != null
     * </pre>
     *
     * @param inputStream Le flux d'entrée à partir duquel charger le plateau
     */
    void loadBoard(InputStream inputStream) throws IOException;

    /**
     * Tente d'enregistrer le plateau actuel du moteur de jeu dans un fichier.
     * Le fichier doit être au format correct pour pouvoir être chargé
     * ultérieurement, sinon une exception est levée.
     *
     * <pre>
     * Préconditions :
     *      filename != null
     * </pre>
     *
     * @param filename Le nom du fichier dans lequel enregistrer le plateau
     *
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de
     * l'écriture du fichier, ou si le fichier ne peut pas être créé ou
     * modifié.
     */
    void saveBoard(String filename) throws IOException;

    /**
     * Réinitialise le plateau de jeu à son état initial tel qu'il a été chargé.
     * Réinitialise le nombre de coups et le timer. Les écouteurs de changements
     * de propriété sont notifiés.
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
     * Enregistre un mouvement dans l'historique du plateau de jeu. Les
     * écouteurs de changements de propriété sont notifiés.
     *
     * <pre>
     * Préconditions :
     *      isLoaded()
     *      move != null
     * </pre>
     *
     * @param move Le mouvement à enregistrer.
     */
    void recordBoardMove(Move move);

    /**
     * Annule le dernier mouvement effectué sur le plateau de jeu. Le mouvement
     * annulé doit être valide selon les règles du jeu, sinon une exception est
     * levée. En cas de réussite, les écouteurs de changements de propriété sont
     * notifiés.
     *
     * <pre>
     * Préconditions :
     *      isLoaded()
     *      canUndoBoardMove()
     * </pre>
     *
     * @throws PropertyVetoException Si le mouvement annulé n'est pas valide
     * selon les règles du jeu.
     */
    void undoBoardMove() throws PropertyVetoException;


    /**
     * Refait le dernier mouvement annulé sur le plateau de jeu. Le mouvement
     * refait doit être valide selon les règles du jeu, sinon une exception est
     * levée. En cas de réussite, les écouteurs de changements de propriété sont
     * notifiés.
     *
     * <pre>
     * Préconditions :
     *      isLoaded()
     *      canRedoBoardMove()
     * </pre>
     *
     * @throws PropertyVetoException Si le mouvement refait n'est pas valide
     * selon les règles du jeu.
     */
    void redoBoardMove() throws PropertyVetoException;

    /**
     * Démarre le timer du moteur de jeu.
     *
     * <pre>
     * Préconditions :
     *      isLoaded()
     * </pre>
     */
    void startTimer();

    /**
     * Ajoute un écouteur de changements de propriété au moteur de jeu.
     *
     * <pre>
     * Préconditions :
     *      listener != null
     * </pre>
     *
     * @param listener L'écouteur de changements de propriété à ajouter
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
     * @param listener L'écouteur de changements de propriété à supprimer
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

}
