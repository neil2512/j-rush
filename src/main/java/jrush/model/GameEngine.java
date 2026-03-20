package jrush.model;

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
 *          isVehicleSelected() <==> getSelectedVehicle() != null
 * </pre>
 */
public interface GameEngine {

    // REQUÊTES

    /**
     * Retourne true si un plateau de jeu est chargé, false sinon.
     *
     * @return true si un plateau de jeu est chargé, false sinon.
     */
    boolean isLoaded();

    /**
     * Retourne true si un véhicule est sélectionné, false sinon.
     *
     * @return true si un véhicule est sélectionné, false sinon.
     */
    boolean isVehicleSelected();

    /**
     * Retourne le véhicule actuellement sélectionné. Si aucun véhicule n'est
     * sélectionné, retourne null.
     *
     * @return Le véhicule actuellement sélectionné, ou null si aucun véhicule
     * n'est sélectionné.
     */
    Vehicle getSelectedVehicle();

    /**
     * Retourne le nombre de mouvements effectués depuis le chargement du
     * plateau de jeu. Un mouvement est défini comme un déplacement valide d'un
     * véhicule.
     *
     * @return Le nombre de mouvements effectués depuis le chargement du plateau
     * de jeu.
     */
    int getMoveCount();

    /**
     * Retourne le plateau de jeu actuellement chargé. Si aucun plateau de jeu
     * n'est chargé, retourne null.
     *
     * @return Le plateau de jeu actuellement chargé, ou null si aucun plateau
     * de jeu n'est chargé.
     */
    Board getBoard();

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
     * Déplace le véhicule actuellement sélectionné d'une distance donnée dans
     * la direction de son orientation. Le déplacement est valide si le véhicule
     * ne sort pas du plateau et ne chevauche pas un autre véhicule. Si le
     * déplacement est valide, la position du véhicule est mise à jour et les
     * écouteurs de changements de position sont notifiés. Si le déplacement
     * n'est pas valide, une exception de type PropertyVetoException est levée
     * pour indiquer que le déplacement a été refusé.
     *
     * <pre>
     * Préconditions :
     *      delta == -1 || delta == 1
     * </pre>
     *
     * @param delta La distance de déplacement. Doit être égal à -1 ou 1.
     */
    void moveVehicle(int delta) throws PropertyVetoException;

}
