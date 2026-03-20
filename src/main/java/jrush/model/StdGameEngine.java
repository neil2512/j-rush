package jrush.model;

import util.Contract;

import java.beans.PropertyVetoException;
import java.io.IOException;

public class StdGameEngine implements GameEngine {

    // ATTRIBUTS

    private final Vehicle selectedVehicle;
    private Board board;
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

    @Override
    public Board getBoard() {
        return board;
    }

    // COMMANDES

    @Override
    public void loadBoard(String filename) throws IOException {
        Contract.checkCondition(filename != null, "filename == null");
        this.board = LevelHandler.loadBoard(filename);
    }

    @Override
    public void moveVehicle(int delta) throws PropertyVetoException {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        Contract.checkCondition(isVehicleSelected(), "!isVehicleSelected()");
        selectedVehicle.move(delta);
        moveCount++;
    }

}
