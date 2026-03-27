package jrush.model.logic;

import jrush.model.Board;
import jrush.model.GameEngine;
import jrush.model.Vehicle;
import jrush.util.Move;
import util.Contract;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StdGameEngine implements GameEngine {

    // ATTRIBUTS

    private Board board;
    private List<Move> history;
    private int cursor;
    private int moveCount;

    private final PropertyChangeSupport pcs;

    // CONSTRUCTEUR

    public StdGameEngine() {
        this.board = null;
        this.history = new ArrayList<>();
        this.cursor = -1;
        this.moveCount = 0;

        pcs = new PropertyChangeSupport(this);
    }

    // REQUÊTES

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public List<Move> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public boolean isLoaded() {
        return (board != null);
    }

    @Override
    public int getMoveCount() {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        return moveCount;
    }

    @Override
    public boolean checkWinCondition() {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        for (Vehicle v : board.getVehicles()) {
            if (v.getId().equals(Vehicle.WIN_CAR.getId())) {
                boolean correctCol =
                        v.getPosition().getY() == Board.EXIT_POSITION.getY();
                boolean correctRow =
                        (v.getPosition().getX() + v.getSize() - 1) ==
                        Board.EXIT_POSITION.getX();
                return correctCol && correctRow;
            }
        }
        return false;
    }

    @Override
    public boolean canUndo() {
        return isLoaded() && !checkWinCondition() && cursor >= 0;
    }

    @Override
    public boolean canRedo() {
        return isLoaded() && !checkWinCondition() &&
               cursor < history.size() - 1;
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return pcs.getPropertyChangeListeners(PROP_MOVECOUNT);
    }

    // COMMANDES

    @Override
    public void loadBoard(String filename) throws IOException {
        Contract.checkCondition(filename != null, "filename == null");
        board = LevelHandler.loadBoard(filename);
    }

    @Override
    public void resetBoard() {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        board.reset();
        history.clear();
        cursor = -1;
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
    public void addMove(Move move) {
        Contract.checkCondition(move != null, "move == null");
        if (cursor < history.size() - 1) {
            history = new ArrayList<>(history.subList(0, cursor + 1));
        }
        history.add(move);
        cursor++;
        setMoveCount(moveCount + 1);
    }

    @Override
    public void undoMove() throws PropertyVetoException {
        Contract.checkCondition(canUndo(), "!canUndo()");
        history.get(cursor).undo();
        cursor--;
        setMoveCount(moveCount + 1);
    }

    @Override
    public void redoMove() throws PropertyVetoException {
        Contract.checkCondition(canRedo(), "!canRedo()");
        cursor++;
        history.get(cursor).redo();
        setMoveCount(moveCount + 1);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(PROP_MOVECOUNT, listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(PROP_MOVECOUNT, listener);
    }

    // OUTILS

    private void setMoveCount(int newCount) {
        int oldCount = moveCount;
        moveCount = newCount;
        pcs.firePropertyChange(PROP_MOVECOUNT, oldCount, newCount);
    }

}
