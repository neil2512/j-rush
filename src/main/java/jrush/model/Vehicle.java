package jrush.model;

import javafx.scene.paint.Color;
import jrush.util.Position;

import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

/**
 * Un véhicule est un objet qui a une position, une taille, une orientation et
 * un identifiant. Il peut être déplacé dans une direction donnée, à condition
 * que le déplacement soit valide. Un véhicule peut également être écouté pour
 * les changements de position, ce qui permet au plateau de vérifier la validité
 * du déplacement avant qu'il ne soit effectué.
 *
 * <pre>
 * Invariant :
 *
 * Constructeur :
 *      Entrée :
 *          VehicleType type : Type du véhicule
 *          boolean horizontal : Orientation du véhicule
 *          Position position : Position du véhicule
 *      Préconditions :
 *          type != null
 *          position != null
 *      Postconditions :
 *          getId() == type.getId()
 *          getSize() == type.getSize()
 *          getColor() == type.getColor()
 *          isHorizontal() == horizontal
 *          getPosition() == position
 * </pre>
 */
public interface Vehicle {

    // CONSTANTES

    VehicleType WIN_CAR = VehicleType.RED_CAR;
    String PROP_POSITION = "position";

    // REQUÊTES

    /**
     * Retourne l'identifiant du véhicule. L'identifiant est une chaîne de
     * caractères unique qui permet de différencier les différents types de
     * véhicules.
     *
     * @return L'identifiant du véhicule.
     */
    String getId();

    /**
     * Retourne la taille du véhicule. La taille est un entier qui indique le
     * nombre de cases occupées par le véhicule sur le plateau.
     *
     * @return La taille du véhicule.
     */
    int getSize();

    /**
     * Retourne la couleur du véhicule.
     *
     * @return La couleur du véhicule.
     */
    Color getColor();

    /**
     * Retourne l'orientation du véhicule. L'orientation est un booléen qui
     * indique si le véhicule est horizontal (true) ou vertical (false).
     *
     * @return L'orientation du véhicule.
     */
    boolean isHorizontal();

    /**
     * Retourne la position du véhicule. La position est un objet de type
     * Position qui indique les coordonnées de la case en haut à gauche du
     * véhicule sur le plateau.
     *
     * @return La position du véhicule.
     */
    Position getPosition();

    /**
     * Retourne les écouteurs de changements de position du véhicule. Ces
     * écouteurs sont utilisés pour vérifier la validité des déplacements avant
     * qu'ils ne soient effectués.
     *
     * @return Les écouteurs de changements de position du véhicule.
     */
    VetoableChangeListener[] getVetoableChangeListeners();

    /**
     * Retourne les écouteurs de changements de position du véhicule. Ces
     * écouteurs sont utilisés pour notifier les changements de position après
     * qu'ils ont été effectués.
     *
     * @return Les écouteurs de changements de position du véhicule.
     */
    PropertyChangeListener[] getPropertyChangeListeners();

    // COMMANDES

    /**
     * Déplace le véhicule d'une distance donnée dans la direction de son
     * orientation. Le déplacement est valide si le véhicule ne sort pas du
     * plateau et ne chevauche pas un autre véhicule. Si le déplacement est
     * valide, la position du véhicule est mise à jour et les écouteurs de
     * changements de position sont notifiés. Si le déplacement n'est pas
     * valide, une exception de type PropertyVetoException est levée pour
     * indiquer que le déplacement a été refusé.
     *
     * <pre>
     * Préconditions :
     *      delta == -1 || delta == 1
     * </pre>
     *
     * @param delta La distance de déplacement. Doit être égal à -1 ou 1.
     *
     * @throws PropertyVetoException Si le déplacement est refusé par un
     * écouteur de changements de position.
     */
    void move(int delta) throws PropertyVetoException;

    /**
     * Ajoute un écouteur de changements de position au véhicule. Cet écouteur
     * est utilisé pour vérifier la validité des déplacements avant qu'ils ne
     * soient effectués.
     *
     * <pre>
     * Préconditions :
     *      listener != null
     * </pre>
     *
     * @param listener L'écouteur de changements de position à ajouter.
     */
    void addVetoableChangeListener(VetoableChangeListener listener);

    /**
     * Supprime un écouteur de changements de position du véhicule. Cet écouteur
     * est utilisé pour vérifier la validité des déplacements avant qu'ils ne
     * soient effectués.
     *
     * <pre>
     * Préconditions :
     *      listener != null
     * </pre>
     *
     * @param listener L'écouteur de changements de position à supprimer.
     */
    void removeVetoableChangeListener(VetoableChangeListener listener);

    /**
     * Ajoute un écouteur de changements de position au véhicule. Cet écouteur
     * est utilisé pour notifier les changements de position après qu'ils ont
     * été effectués.
     *
     * <pre>
     * Préconditions :
     *      listener != null
     * </pre>
     *
     * @param listener L'écouteur de changements de position à ajouter.
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Supprime un écouteur de changements de position du véhicule. Cet écouteur
     * est utilisé pour notifier les changements de position après qu'ils ont
     * été effectués.
     *
     * <pre>
     * Préconditions :
     *      listener != null
     * </pre>
     *
     * @param listener L'écouteur de changements de position à supprimer.
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

}
