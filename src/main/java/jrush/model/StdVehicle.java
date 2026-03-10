package jrush.model;

import util.Contract;
import jrush.util.Position;

import java.beans.*;

public class StdVehicle implements Vehicle {

  // ATTRIBUTS

  private final char id;
  private final int size;
  private final boolean horizontal;
  private Position position;

  private final VetoableChangeSupport vcs;
  private final PropertyChangeSupport pcs;

  // CONSTRUCTEUR

  StdVehicle(char id, int size, boolean horizontal, Position position) {
    // CONTRACT_TO_ADD
    this.id = id;
    this.size = size;
    this.horizontal = horizontal;
    this.position = position;

    this.vcs = new VetoableChangeSupport(this);
    this.pcs = new PropertyChangeSupport(this);
  }

  // REQUÊTES

  @Override
  public char getId() {
    return id;
  }

  @Override
  public int getSize() {
    return size;
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
    return id + " " + size + " " + horizontal + " " + position.getX() + " " + position.getY();
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
