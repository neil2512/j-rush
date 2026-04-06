package jrush.app.model;


import jrush.app.util.Position;

import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.IOException;

/**
 * Interface représentant le moteur de construction du jeu. Elle permet de créer
 * et de modifier un plateau de jeu.
 *
 * <pre>
 * Invariant :
 *      – getBoard() == null <==> !isLoaded()
 * Constructeur :
 *      Postconditions :
 *          – getBoard() == null
 *          – !isLoaded()
 *          – getPropertyChangeListeners().length == 0
 * </pre>
 */
public interface BuildEngine {

    // CONSTANTES

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

    /**
     * Retourne true si le plateau de jeu actuellement chargé est valide selon
     * les règles du jeu, false sinon. Un plateau de jeu est considéré comme
     * valide si tous les véhicules sont positionnés à des positions valides,
     * c'est-à-dire qu'ils ne sortent pas du plateau et ne chevauchent pas
     * d'autres véhicules.
     *
     * <pre>
     * Préconditions :
     *      isLoaded()
     * </pre>
     */
    boolean isValid();

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
     * Crée un nouveau plateau de jeu vide et le définit comme le plateau actuel
     * du moteur de jeu. Les écouteurs de changements de propriété sont
     * notifiés.
     */
    void newBoard();

    /**
     * Tente d'ajouter un véhicule sur le plateau de jeu actuel du moteur de
     * jeu. Le véhicule doit être positionné à une position valide selon les
     * règles du jeu, sinon une exception est levée. En cas de réussite, les
     * écouteurs de changements de propriété sont notifiés.
     *
     * @param vehicle Le véhicule à ajouter sur le plateau de jeu
     *
     * @throws IllegalArgumentException Si la position du véhicule est
     * interdite.
     */
    void addVehicleOnBoard(Vehicle vehicle);

    /**
     * Supprime un véhicule du plateau de jeu actuel du moteur de jeu. Les
     * écouteurs de changements de propriété sont notifiés.
     *
     * @param vehicle Le véhicule à supprimer du plateau de jeu
     */
    void removeVehicleFromBoard(Vehicle vehicle);

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
     * Tente d'enregistrer le plateau actuel du moteur de construction dans un
     * fichier. Le fichier doit être au format correct pour pouvoir être chargé
     * ultérieurement, sinon une exception est levée. En cas de réussite, les
     * écouteurs de changements de propriété sont notifiés.
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
     * Réinitialise le plateau de jeu à son état initial tel qu'il a été
     * chargé. Les écouteurs de changements de propriété sont notifiés.
     *
     * <pre>
     * Préconditions :
     *      isLoaded()
     * </pre>
     */
    void resetBoard();

    /**
     * Redéfinit la position et l'orientation d'un véhicule sur le plateau de
     * jeu actuel du moteur de jeu. Celles-ci doivent être valides, c'est-à-dire
     * que le véhicule ne doit pas sortir du plateau et ne doit pas chevaucher
     * un autre véhicule. Si le placement est valide, la position et
     * l'orientation du véhicule sont mises à jour et les écouteurs de
     * changements de position et de placement sont notifiés. Si le placement
     * n'est pas valide, une exception de type PropertyVetoException est levée
     * pour indiquer que le placement a été refusé. En cas de réussite, les
     * écouteurs de changements de propriété sont notifiés.
     *
     * @param vehicle Le véhicule à déplacer et faire pivoter
     * @param position La nouvelle position du véhicule
     * @param horizontal La nouvelle orientation du véhicule
     *
     * @throws PropertyVetoException Si le changement de position ou
     * d'orientation est refusé par un écouteur de veto de changement de
     * propriété.
     */
    void rotateAndMove(Vehicle vehicle, Position position, boolean horizontal)
            throws PropertyVetoException;

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
