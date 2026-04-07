package jrush.app.util;

/**
 * Classe utilitaire pour la vérification de contrats. Un contrat est une
 * condition qui doit être vérifiée à un moment donné dans le code, par exemple
 * avant d'exécuter une méthode ou après avoir effectué une opération. Si la
 * condition n'est pas vérifiée, une exception est levée pour signaler que le
 * contrat a été violé. Cette classe fournit des méthodes statiques pour
 * vérifier les conditions et lever des exceptions appropriées en cas de
 * violation de contrat.
 */
public final class Contract {

    // CONSTRUCTEURS

    private Contract() {
    }

    // COMMANDES

    /**
     * Vérifie que la condition spécifiée est vraie. Si la condition est fausse,
     * une AssertionError est levée avec un message indiquant que le contrat a
     * été violé.
     *
     * @param condition La condition à vérifier
     *
     * @throws AssertionError si la condition est fausse
     */
    public static void checkCondition(boolean condition) {
        if (!condition) {
            throw new AssertionError(
                    "Contrat violé : la condition est fausse.");
        }
    }

    /**
     * Vérifie que la condition spécifiée est vraie. Si la condition est fausse,
     * une AssertionError est levée avec un message indiquant que le contrat a
     * été violé.
     *
     * @param condition La condition à vérifier
     *
     * @throws AssertionError si la condition est fausse
     */
    public static void checkCondition(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Contrat violé : " + message);
        }
    }
}