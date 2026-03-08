package jrush.model;



import jrush.util.Position;

import java.util.List;

public interface Board {

  // CONSTANTES

  int GRID_SIZE = 6;
  Position EXIT_POSITION = new Position(GRID_SIZE - 1, GRID_SIZE / 2);

  // REQUÊTES

  List<Vehicle> getVehicles();

  boolean isVictory();

  // COMMANDES

  void addVehicle(Vehicle v);
}
