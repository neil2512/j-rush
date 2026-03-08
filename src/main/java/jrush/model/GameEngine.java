package jrush.model;

import java.beans.PropertyVetoException;

public interface GameEngine {

  // REQUÊTES

  boolean isLoaded();

  boolean isVehicleSelected();

  Vehicle getSelectedVehicle();

  int getMoveCount();

  // COMMANDES

  void moveVehicle(int delta) throws PropertyVetoException;

}
