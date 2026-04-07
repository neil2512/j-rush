package jrush.app.model.logic.solver.util;

import jrush.app.util.Contract;

/**
 * Classe utilitaire générique représentant un quadruplet de quatre éléments
 * potentiellement de types différents.
 *
 * <pre>
 * Constructeur
 *      Entrée :
 *          – E firstElement
 *          – G secondElement
 *          – H thirdElement
 *          – T fourthElement
 *      Préconditions :
 *          – firstElement != null
 *          – secondElement != null
 *          – thirdElement != null
 *          – fourthElement != null
 * </pre>
 */
public record Quadruple<E, G, H, T>(E firstElement, G secondElement,
                                    H thirdElement, T fourthElement) {
    public Quadruple {
        Contract.checkCondition(firstElement != null);
        Contract.checkCondition(secondElement != null);
        Contract.checkCondition(thirdElement != null);
        Contract.checkCondition(fourthElement != null);
    }
}