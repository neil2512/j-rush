package jrush.app.model.logic.solver;

import jrush.app.model.*;
import jrush.app.model.util.Move;
import jrush.app.util.Contract;

import java.beans.PropertyVetoException;
import java.util.List;

/**
 * Classe abstraite représentant un résolveur de jeu. Un résolveur est capable
 * de trouver une séquence de mouvements qui mène à la victoire à partir d'un
 * état de jeu donné.
 *
 * <pre>
 * Constructeur
 *      Entrée :
 *          – Board board
 *      Préconditions :
 *          – board != null
 * </pre>
 */
public abstract class Solver {

    // ATTRIBUTS

    protected Board board;

    // CONSTRUCTEURS

    public Solver(Board board) {
        Contract.checkCondition(board != null);
        this.board = board;
    }

    // COMMANDES

    /**
     * Résout le jeu en trouvant une séquence de mouvements qui mène à la
     * victoire.
     *
     * @return Une liste de mouvements qui mène à la victoire.
     *
     * @throws PropertyVetoException Si une erreur de propriété se produit lors
     * de la résolution du jeu.
     */
    public abstract List<Move> solve() throws PropertyVetoException;
}
