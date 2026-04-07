package jrush.app.model.logic;

import jrush.app.model.Board;
import jrush.app.model.BuildEngine;
import jrush.app.model.Vehicle;
import jrush.app.model.components.StdBoard;
import jrush.app.model.logic.solver.AStarSolver;
import jrush.app.model.util.Move;
import jrush.app.util.Position;
import jrush.app.util.Contract;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.List;

import static jrush.app.model.logic.LevelHandler.DEFAULT_MOVE;
import static jrush.app.model.logic.LevelHandler.DEFAULT_TIME;

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

    @Override
    public boolean isSolvable() {
        if (!isValid()) {
            return false;
        }
        try {
            StdGameEngine tempEngine = new StdGameEngine();

            AStarSolver solver = new AStarSolver(this.board);
            List<Move> solution = solver.solve();

            return solution != null && !solution.isEmpty();
        } catch (Exception e) {
            return false;
        }
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
        LevelHandler.LoadResult result = LevelHandler.loadBoard(filename);
        this.board = result.board();
        pcs.firePropertyChange(PROP_BOARD, old, this.board);
    }

    @Override
    public void saveBoard(String filename) throws IOException {
        Contract.checkCondition(filename != null, "filename == null");
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        LevelHandler.saveBoard(board, DEFAULT_MOVE, DEFAULT_TIME, filename);
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
