package jrush.model.components;

import javafx.scene.paint.Color;
import jrush.model.Vehicle;
import util.Contract;
import jrush.util.Position;

import java.beans.*;

public class StdVehicle implements Vehicle {

    // ATTRIBUTS

    private final VehicleType type;
    private final boolean horizontal;
    private Position position;

    private final VetoableChangeSupport vcs;
    private final PropertyChangeSupport pcs;

    // CONSTRUCTEUR

    public StdVehicle(VehicleType type, boolean horizontal, Position position) {
        Contract.checkCondition(type != null, "type == null");
        Contract.checkCondition(position != null, "position == null");

        this.type = type;
        this.horizontal = horizontal;
        this.position = position;

        this.vcs = new VetoableChangeSupport(this);
        this.pcs = new PropertyChangeSupport(this);
    }

    // REQUÊTES

    @Override
    public String getId() {
        return type.getId();
    }

    @Override
    public int getSize() {
        return type.getSize();
    }

    @Override
    public Color getColor() {
        return type.getColor();
    }

    @Override
    public boolean isHorizontal() {
        return horizontal;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public VetoableChangeListener[] getVetoableChangeListeners() {
        return vcs.getVetoableChangeListeners(PROP_POSITION);
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return pcs.getPropertyChangeListeners(PROP_POSITION);
    }

    @Override
    public String toString() {
        return this.type.getId() + ";" +
               horizontal + ";" + position.getX() + ";" + position.getY();
    }

    // COMMANDES

    @Override
    public void move(int delta) throws PropertyVetoException {
        Contract.checkCondition(delta != 0, "delta == 0");
        Position oldPos = position;
        Position newPos = new Position(position.getX(), position.getY());
        if (isHorizontal()) {
            newPos.addX(delta);
        } else {
            newPos.addY(delta);
        }
        vcs.fireVetoableChange(PROP_POSITION, oldPos, newPos);
        position = newPos;
        pcs.firePropertyChange(PROP_POSITION, oldPos, newPos);
    }

    /**
     * Change la position du véhicule. Cette méthode est utilisée pour
     * réinitialiser la position du véhicule à son état initial. Elle ne doit
     * pas être utilisée pour déplacer le véhicule pendant le jeu, car elle ne
     * vérifie pas les règles de déplacement.
     *
     * <pre>
     * Préconditions :
     *    newPos != null
     * </pre>
     *
     * @param newPos La nouvelle position du véhicule.
     */
    void setPosition(Position newPos) {
        Contract.checkCondition(newPos != null, "newPos == null");
        Position oldPos = this.position;
        position = newPos;
        pcs.firePropertyChange(PROP_POSITION, oldPos, newPos);
    }

    @Override
    public void addVetoableChangeListener(VetoableChangeListener listener) {
        vcs.addVetoableChangeListener(PROP_POSITION, listener);
    }

    @Override
    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        vcs.removeVetoableChangeListener(PROP_POSITION, listener);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(PROP_POSITION, listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(PROP_POSITION, listener);
    }

}
