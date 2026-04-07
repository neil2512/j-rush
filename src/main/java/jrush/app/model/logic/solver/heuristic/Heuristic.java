package jrush.app.model.logic.solver.heuristic;

/**
 * Classe représentant une heuristique pour un solveur de jeu. Une heuristique
 * est une fonction qui estime le coût restant pour atteindre la victoire à
 * partir d'un état de jeu donné. Elle est utilisée pour guider la recherche
 * d'une solution en donnant une estimation du coût restant à chaque étape de la
 * recherche.
 */
public interface Heuristic {

    // REQUETES

    /**
     * Retourne une estimation du coût restant pour atteindre la victoire à
     * partir d'un état de jeu donné.
     *
     * @return une estimation du coût restant pour atteindre la victoire.
     */
    int getScore();
}
