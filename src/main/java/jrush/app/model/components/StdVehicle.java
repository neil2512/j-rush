package jrush.app.model.components;

import javafx.scene.paint.Color;
import jrush.app.model.Vehicle;
import jrush.app.model.util.Placement;
import jrush.app.util.Position;
import jrush.app.util.Contract;

import java.beans.*;
import java.util.ArrayList;
import java.util.List;

public class StdVehicle implements Vehicle {

    // ATTRIBUTS

    private final VehicleType type;
    private boolean horizontal;
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

    public StdVehicle(Vehicle other) {
        Contract.checkCondition(other != null, "other == null");

        this.type = VehicleType.fromId(other.getId());
        this.horizontal = other.isHorizontal();
        this.position = new Position(other.getPosition().getX(),
                                     other.getPosition().getY());

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
    public Position getTail() {
        Position head = getPosition();
        int offset = getSize() - 1;

        if (isHorizontal()) {
            return new Position(head.getX() + offset, head.getY());
        } else {
            return new Position(head.getX(), head.getY() + offset);
        }
    }

    @Override
    public List<Position> getOccupiedPositions() {
        List<Position> wholeCar = new ArrayList<>();
        Position head = getPosition();
        int size = getSize();

        for (int i = 0; i < size; i++) {
            if (isHorizontal()) {
                wholeCar.add(new Position(head.getX() + i, head.getY()));
            } else {
                wholeCar.add(new Position(head.getX(), head.getY() + i));
            }
        }
        return wholeCar;
    }

    @Override
    public List<Position> getOccupiedPositionsAt(
            Position position,
            boolean horizontal
    ) {
        List<Position> positions = new ArrayList<>();
        int size = getSize();

        for (int i = 0; i < size; i++) {
            if (horizontal) {
                positions.add(
                        new Position(position.getX() + i, position.getY()));
            } else {
                positions.add(
                        new Position(position.getX(), position.getY() + i));
            }
        }
        return positions;
    }

    @Override
    public VetoableChangeListener[] getVetoableChangeListeners() {
        return vcs.getVetoableChangeListeners();
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return pcs.getPropertyChangeListeners();
    }

    @Override
    public String toString() {
        return this.type.getId() + ";" + (horizontal ? "1" : "0") + ";" +
               position.getX() + ";" + position.getY();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        StdVehicle that = (StdVehicle) obj;

        return this.type.equals(that.type) &&
               this.position.equals(that.position) &&
               this.horizontal == that.horizontal;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + (horizontal ? 1 : 0);
        return result;
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

    @Override
    public void setPlacement(Position position, boolean horizontal)
            throws PropertyVetoException {
        Contract.checkCondition(position != null, "position == null");

        Placement oldPlacement = new Placement(this.position, this.horizontal);
        Placement newPlacement = new Placement(position, horizontal);

        vcs.fireVetoableChange(PROP_PLACEMENT, oldPlacement, newPlacement);
        this.position = position;
        this.horizontal = horizontal;
        pcs.firePropertyChange(PROP_PLACEMENT, oldPlacement, newPlacement);
        pcs.firePropertyChange(PROP_POSITION, oldPlacement.position(),
                               position);
    }


    void setPosition(Position position) {
        Contract.checkCondition(position != null, "position == null");
        Position oldPosition = this.position;

        this.position = position;
        pcs.firePropertyChange(PROP_POSITION, oldPosition, position);
    }

    @Override
    public void addVetoableChangeListener(VetoableChangeListener listener) {
        vcs.addVetoableChangeListener(listener);
    }

    @Override
    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        vcs.removeVetoableChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

}
