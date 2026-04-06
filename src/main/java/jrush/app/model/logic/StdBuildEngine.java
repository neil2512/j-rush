package jrush.app.model.logic;

import jrush.app.model.Board;
import jrush.app.model.BuildEngine;
import jrush.app.model.Vehicle;
import jrush.app.model.components.StdBoard;
import jrush.app.util.Position;
import util.Contract;

import java.beans.PropertyVetoException;
import java.io.IOException;

public class StdBuildEngine extends AbstractEngine implements BuildEngine {

    // CONSTRUCTEURS

    public StdBuildEngine() {
        super();
    }

    // REQUÊTES

    @Override
    public boolean isValid() {
        Contract.checkCondition(isLoaded(), "!isLoaded()");

        Vehicle winCar = board.findVehicle(Vehicle.WIN_CAR);
        if (winCar == null) {
            return false;
        }

        return winCar.isHorizontal() &&
               winCar.getPosition().getY() == Board.EXIT_POSITION.getY();
    }

    // COMMANDES

    @Override
    public void newBoard() {
        Board old = this.board;
        this.board = new StdBoard();
        pcs.firePropertyChange(PROP_BOARD, old, this.board);
    }

    @Override
    public void loadBoard(String filename) throws IOException {
        Contract.checkCondition(filename != null, "filename == null");
        Board old = this.board;
        this.board = LevelHandler.loadBoard(filename);
        pcs.firePropertyChange(PROP_BOARD, old, this.board);
    }

    @Override
    public void resetBoard() {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        board.reset();
        pcs.firePropertyChange(PROP_BOARD, null, board);
    }

    @Override
    public void addVehicleOnBoard(Vehicle vehicle) {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        Contract.checkCondition(vehicle != null, "vehicle == null");
        board.addVehicle(vehicle);
        pcs.firePropertyChange(PROP_BOARD, null, board);
    }

    @Override
    public void removeVehicleFromBoard(Vehicle vehicle) {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        board.removeVehicle(vehicle);
        pcs.firePropertyChange(PROP_BOARD, null, board);
    }


    @Override
    public void rotateAndMove(
            Vehicle vehicle, Position position, boolean horizontal)
            throws PropertyVetoException {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        vehicle.setPlacement(position, horizontal);
        pcs.firePropertyChange(PROP_BOARD, null, board);
    }
}
