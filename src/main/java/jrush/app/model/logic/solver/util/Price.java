package jrush.app.model.logic.solver.util;


import jrush.app.model.logic.solver.heuristic.Heuristic;
import jrush.app.model.util.Move;
import jrush.app.util.Contract;

import java.util.List;


/**
 * Classe représentant le coût total d'un plateau (état du jeu) dans
 * l'algorithme A*. Le coût total est calculé comme la somme du nombre de coups
 * déjà effectués (taille de la solution) et du score obtenu avec
 * l'heuristique.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          – List<Move> solution
 *          – Heuristic heuristic
 *      Préconditions :
 *          – solution != null
 *          – heuristic != null
 *      Postconditions :
 *          - getPrice() == solution.size() + heuristic.getScore()
 * </pre>
 */
public class Price {

    // ATTRIBUTS

    private final int price;

    // CONSTRUCTEURS

    public Price(List<Move> solution, Heuristic heuristic) {
        Contract.checkCondition(solution != null);
        Contract.checkCondition(heuristic != null);
        this.price = solution.size() + heuristic.getScore();
    }

    // REQUÊTES

    /**
     * Retourne le coût total calculé pour ce plateau, qui est la somme du
     * nombre de coups déjà effectués (taille de la solution) et du score obtenu
     * avec l'heuristique.
     *
     * @return Le coût total calculé pour ce plateau.
     */
    public int getPrice() {
        return this.price;
    }
}
