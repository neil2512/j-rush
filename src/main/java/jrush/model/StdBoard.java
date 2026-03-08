package jrush.model;

import jrush.util.Position;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.List;


public class StdBoard implements Board, VetoableChangeListener {

  // ATTRIBUTS

  private final List<Vehicle> vehicles;

  // CONSTRUCTEUR

  public StdBoard() {
    this.vehicles = new ArrayList<>();
  }

  // REQUÊTES

  @Override
  public List<Vehicle> getVehicles() {
    return new ArrayList<>(vehicles);
  }

  @Override
  public boolean isVictory() {
    for (Vehicle v : vehicles) {
      if (v.getId() == Vehicle.WIN_CAR_ID) {
        return v.getPosition().equals(EXIT_POSITION);
      }
    }
    return false;
  }

  // COMMANDES

  @Override
  public void addVehicle(Vehicle v) {
    v.addVetoableChangeListener(this);
    vehicles.add(v);
  }

  @Override
  public void vetoableChange(PropertyChangeEvent evt)
      throws PropertyVetoException {

    Vehicle v = (Vehicle) evt.getSource();
    Position newPos = (Position) evt.getNewValue();

    if (newPos.getX() < 0 || newPos.getY() < 0 ||
        (v.isHorizontal() && newPos.getX() + v.getSize() > GRID_SIZE) ||
        (!v.isHorizontal() && newPos.getY() + v.getSize() > GRID_SIZE)) {
      throw new PropertyVetoException("Out of bounds", evt);
    }

    for (Vehicle other : vehicles) {
      if (other == v) {
        continue;
      }

      if (intersect(newPos, v.getSize(), v.isHorizontal(), other.getPosition(),
          other.getSize(), other.isHorizontal())) {
        throw new PropertyVetoException("Colliding with " + other.getId(), evt);
      }
    }
  }

  // OUTILS

  private boolean intersect(Position p1, int s1, boolean h1, Position p2,
      int s2, boolean h2) {

    for (int i = 0; i < s1; i++) {
      int x1 = h1 ? p1.getX() + i : p1.getX();
      int y1 = h1 ? p1.getY() : p1.getY() + i;

      for (int j = 0; j < s2; j++) {
        int x2 = h2 ? p2.getX() + j : p2.getX();
        int y2 = h2 ? p2.getY() : p2.getY() + j;

        if (x1 == x2 && y1 == y2) {
          return true;
        }
      }
    }
    return false;
  }
}
