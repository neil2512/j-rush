package jrush.model;

import javafx.scene.paint.Color;
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

    StdVehicle(VehicleType type, boolean horizontal, Position position) {
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
        return this.type.getId() + " " + this.type.getSize() + " " +
               horizontal + " " + position.getX() + " " + position.getY();
    }

    // COMMANDES

    @Override
    public void move(int delta) throws PropertyVetoException {
        Contract.checkCondition(delta == -1 || delta == 1,
                                "delta != -1 && delta " + "!= 1");
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
