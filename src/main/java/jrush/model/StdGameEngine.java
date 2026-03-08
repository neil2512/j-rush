package jrush.model;

import util.Contract;

import java.beans.PropertyVetoException;

public class StdGameEngine implements GameEngine {

  // ATTRIBUTS

  private final Board board;
  private Vehicle selectedVehicle;
  private int moveCount;

  // CONSTRUCTEUR

  public StdGameEngine() {
    this.board = null;
    this.selectedVehicle = null;
    this.moveCount = 0;
  }

  // REQUÊTES

  @Override
  public boolean isLoaded() {
    return (board != null);
  }

  @Override
  public boolean isVehicleSelected() {
    return (selectedVehicle != null);
  }

  @Override
  public Vehicle getSelectedVehicle() {
    return selectedVehicle;
  }

  @Override
  public int getMoveCount() {
    return moveCount;
  }

  // COMMANDES

  @Override
  public void moveVehicle(int delta) throws PropertyVetoException {
    Contract.checkCondition(isLoaded(), "!isLoaded()");
    Contract.checkCondition(isVehicleSelected(), "!isVehicleSelected()");
    selectedVehicle.move(delta);
    moveCount++;
  }

}
