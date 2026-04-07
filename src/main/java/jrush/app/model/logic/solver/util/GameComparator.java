package jrush.app.model.logic.solver.util;

import jrush.app.model.Board;

import jrush.app.model.logic.solver.heuristic.Heuristic;
import jrush.app.model.util.Move;

import java.util.Comparator;
import java.util.List;

/**
 * Classe représentant un comparateur pour les quadruples de type (Board,
 * List<Move>, Heuristic, Integer). Ce comparateur est utilisé pour trier les
 * quadruples en fonction de leur quatrième élément, qui est un entier
 * représentant le score de l'heuristique associée à l'état de jeu. Le
 * comparateur compare les scores de deux quadruples et retourne un entier
 * indiquant leur ordre relatif.
 */
public class GameComparator implements
                            Comparator<Quadruple<Board, List<Move>, Heuristic, Integer>> {

    // REQUETES

    /**
     * Compare deux quadruples en fonction de leur quatrième élément (score de
     * l'heuristique). Retourne un entier négatif, zéro ou positif selon que le
     * score de l'heuristique du premier quadruple est respectivement inférieur,
     * égal ou supérieur à celui du second quadruple.
     *
     * @param a Le premier quadruple à comparer.
     * @param b Le second quadruple à comparer.
     *
     * @return Un entier négatif, zéro ou positif selon que le score de
     * l'heuristique du premier quadruple est respectivement inférieur, égal
     * ou supérieur à celui du second quadruple.
     */
    @Override
    public int compare(
            Quadruple<Board, List<Move>, Heuristic, Integer> a,
            Quadruple<Board, List<Move>, Heuristic, Integer> b
    ) {
        return Integer.compare(a.fourthElement(), b.fourthElement());
    }
}
