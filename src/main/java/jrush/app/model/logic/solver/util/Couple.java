package jrush.app.model.logic.solver.util;

import jrush.app.util.Contract;

/**
 * Classe générique représentant un couple d'objets de types E et G. Cette
 * classe est utilisée pour stocker des paires d'objets, par exemple un état de
 * jeu et la liste des mouvements associés.
 *
 * <pre>
 * Constructeur
 *      Entrée :
 *          – E a
 *          – G b
 *      Préconditions :
 *          – a != null
 *          – b != null
 * </pre>
 */
public record Couple<E, G>(E firstElement, G secondElement) {
    // CONSTRUCTEUR

    public Couple {
        Contract.checkCondition(firstElement != null);
        Contract.checkCondition(secondElement != null);
    }
}