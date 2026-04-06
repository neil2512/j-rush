package jrush.app.model;

import javafx.scene.paint.Color;
import jrush.app.model.components.VehicleType;
import jrush.app.util.Position;

import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

/**
 * Un véhicule est un objet qui a une position, une taille, une orientation et
 * un identifiant. Il peut être déplacé par glissement dans une direction
 * donnée, à condition que le déplacement soit valide. Il peut être également
 * déplacé par positionnement, à condition que la position soit également
 * valide.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          – VehicleType type : Type du véhicule
 *          – boolean horizontal : Orientation du véhicule
 *          – Position position : Position du véhicule
 *      Préconditions :
 *          – type != null
 *          – position != null
 *      Postconditions :
 *          – getId().equals(type.getId())
 *          – getSize() == type.getSize()
 *          – getColor().equals(type.getColor())
 *          – isHorizontal() == horizontal
 *          – getPosition().equals(position)
 *          – getVetoableChangeListeners().length == 0
 *          – getPropertyChangeListeners().length == 0
 *
 * Constructeur :
 *    Entrée :
 *          – Vehicle other
 *    Préconditions :
 *          – other != null
 *    Postconditions :
 *          – getId().equals(other.getId())
 *          – getSize() == other.getSize()
 *          – getColor().equals(other.getColor())
 *          – isHorizontal() == other.isHorizontal()
 *          – getPosition().equals(other.getPosition())
 *          – getPosition() != other.getPosition()
 *          – getVetoableChangeListeners().length == 0
 *          – getPropertyChangeListeners().length == 0
 * </pre>
 */
public interface Vehicle {

    // CONSTANTES

    VehicleType WIN_CAR = VehicleType.RED_CAR;
    String PROP_POSITION = "position";
    String PROP_PLACEMENT = "placement";

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
     * Retourne un tableau des écouteurs de veto de changements de propriétés
     * actuellement enregistrés auprès du véhicule.
     *
     * @return Un tableau des écouteurs de veto de changements de propriétés
     * actuellement enregistrés auprès du véhicule.
     */
    VetoableChangeListener[] getVetoableChangeListeners();


    /**
     * Retourne un tableau des écouteurs de changements de propriété
     * actuellement enregistrés auprès du véhicule.
     *
     * @return Un tableau des écouteurs de changements de propriété actuellement
     * enregistrés auprès du véhicule.
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
     *      delta != 0
     * </pre>
     *
     * @param delta La distance de déplacement
     *
     * @throws PropertyVetoException Si le déplacement est refusé par un
     * écouteur de veto de changement de propriété.
     */
    void move(int delta) throws PropertyVetoException;

    /**
     * Redéfinit la position et l'orientation du véhicule. Celles-ci doivent
     * être valides, c'est-à-dire que le véhicule ne doit pas sortir du plateau
     * et ne doit pas chevaucher un autre véhicule. Si le placement est valide,
     * la position et l'orientation du véhicule sont mises à jour et les
     * écouteurs de changements de position et de placement sont notifiés. Si le
     * placement n'est pas valide, une exception de type PropertyVetoException
     * est levée pour indiquer que le placement a été refusé.
     *
     * <pre>
     * Préconditions :
     *      position != null
     * </pre>
     *
     * @param position La nouvelle position du véhicule
     * @param horizontal La nouvelle orientation du véhicule
     *
     * @throws PropertyVetoException Si le changement de position ou
     * d'orientation est refusé par un écouteur de veto de changement de
     * propriété.
     */
    void setPlacement(Position position, boolean horizontal)
            throws PropertyVetoException;

    /**
     * Ajoute un écouteur de veto de changements de propriétés au véhicule.
     *
     * @param listener L'écouteur de veto de changements de propriétés à
     * ajouter
     */
    void addVetoableChangeListener(VetoableChangeListener listener);

    /**
     * Supprime un écouteur de veto de changements de propriétés du véhicule.
     *
     * @param listener L'écouteur de veto de changements de propriétés à
     * supprimer
     */
    void removeVetoableChangeListener(VetoableChangeListener listener);

    /**
     * Ajoute un écouteur de changements de propriété au véhicule.
     *
     * @param listener L'écouteur de changements de propriété à ajouter
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Supprime un écouteur de changements de propriété du véhicule.
     *
     * @param listener L'écouteur de changements de propriété à supprimer
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

}
