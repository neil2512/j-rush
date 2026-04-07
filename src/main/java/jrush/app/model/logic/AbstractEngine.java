package jrush.app.model.logic;

import jrush.app.model.Board;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Classe abstraite qui construit la base d'un moteur utilitaire, responsable de
 * la gestion du plateau et de la communication avec les contrôleurs
 * graphiques.
 *
 * <pre>
 * Invariant :
 *      – getBoard() == null <==> !isLoaded()
 * Constructeur :
 *      Postconditions :
 *          – getBoard() == null
 *          – !isLoaded()
 *          – getPropertyChangeListeners().length == 0
 * </pre>
 */
public abstract class AbstractEngine {

    // ATTRIBUTS

    protected Board board;
    protected final PropertyChangeSupport pcs;

    // CONSTRUCTEURS

    protected AbstractEngine() {
        this.board = null;
        this.pcs = new PropertyChangeSupport(this);
    }

    // REQUETES

    public Board getBoard() {
        return board;
    }

    public boolean isLoaded() {
        return board != null;
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return pcs.getPropertyChangeListeners();
    }

    // COMMANDES

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
