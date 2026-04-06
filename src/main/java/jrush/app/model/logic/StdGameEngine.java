package jrush.app.model.logic;

import jrush.app.model.GameEngine;
import jrush.app.model.Vehicle;
import jrush.app.model.util.Move;
import util.Contract;

import java.beans.PropertyVetoException;
import java.io.IOException;

public class StdGameEngine extends AbstractEngine implements GameEngine {

    // ATTRIBUTS

    private int moveCount;

    // CONSTRUCTEUR

    public StdGameEngine() {
        super();

        this.moveCount = 0;
    }

    // REQUÊTES

    @Override
    public int getMoveCount() {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        return moveCount;
    }

    @Override
    public boolean checkWinCondition() {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        return board.checkWinCondition();
    }

    @Override
    public boolean canUndoBoardMove() {
        return isLoaded() && !checkWinCondition() && board.canUndo();
    }

    @Override
    public boolean canRedoBoardMove() {
        return isLoaded() && !checkWinCondition() && board.canRedo();
    }

    // COMMANDES

    @Override
    public void loadBoard(String filename) throws IOException {
        Contract.checkCondition(filename != null, "filename == null");
        board = LevelHandler.loadBoard(filename);
        setMoveCount(0);
    }

    @Override
    public void resetBoard() {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        board.reset();
        setMoveCount(0);
    }

    @Override
    public void moveVehicle(Vehicle vehicle, int delta)
            throws PropertyVetoException {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        Contract.checkCondition(vehicle != null, "vehicle == null");
        vehicle.move(delta);
    }

    @Override
    public void recordBoardMove(Move move) {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        Contract.checkCondition(move != null, "move == null");
        board.record(move);
        setMoveCount(moveCount + 1);
    }

    @Override
    public void undoBoardMove() throws PropertyVetoException {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        Contract.checkCondition(canUndoBoardMove(), "!canUndo()");
        board.undo();
        setMoveCount(moveCount + 1);
    }

    @Override
    public void redoBoardMove() throws PropertyVetoException {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        Contract.checkCondition(canRedoBoardMove(), "!canRedo()");
        board.redo();
        setMoveCount(moveCount + 1);
    }

    // OUTILS

    private void setMoveCount(int newCount) {
        int oldCount = moveCount;
        moveCount = newCount;
        pcs.firePropertyChange(PROP_MOVECOUNT, oldCount, newCount);
    }

}
