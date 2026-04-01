package jrush.model.logic;

import jrush.model.Board;
import jrush.model.BuildEngine;
import jrush.model.Vehicle;
import jrush.model.components.StdBoard;
import jrush.util.Position;
import util.Contract;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;

public class StdBuildEngine implements BuildEngine {

    // ATTRIBUTS
    private Board board;
    private final PropertyChangeSupport pcs;

    // CONSTRUCTEUR
    public StdBuildEngine() {
        this.board = null;
        this.pcs = new PropertyChangeSupport(this);
    }

    // REQUÊTES

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public boolean isLoaded() {
        return board != null;
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return pcs.getPropertyChangeListeners(PROP_BOARD);
    }

    //COMMANDES

    @Override
    public void newBoard(){
        board = new StdBoard();
        pcs.firePropertyChange(PROP_BOARD,null,board);
    }

    @Override
    public void addVehicleOnBoard(Vehicle vehicle) {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        Contract.checkCondition(vehicle != null, "vehicle == null");
        Contract.checkCondition(
                board.getVehicles().stream()
                        .noneMatch(v -> v.getId().equals(vehicle.getId())),
                "vehicle already on board"
        );
        board.addVehicle(vehicle);
        pcs.firePropertyChange(PROP_BOARD, null, vehicle);
    }

    @Override
    public void removeVehicleFromBoard(Vehicle vehicle) {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        Contract.checkCondition(vehicle != null, "vehicle == null");
        Contract.checkCondition(
                board.getVehicles().stream()
                        .anyMatch(v -> v.getId().equals(vehicle.getId())),
                "vehicle not on board"
        );
        board.removeVehicle(vehicle);
        pcs.firePropertyChange(PROP_BOARD, vehicle, null);
    }

    @Override
    public void moveVehicleOnBoard(Vehicle vehicle, Position position)
            throws PropertyVetoException {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        Contract.checkCondition(vehicle != null, "vehicle == null");
        Contract.checkCondition(position != null, "position == null");
        Contract.checkCondition(
                board.getVehicles().stream()
                        .anyMatch(v -> v.getId().equals(vehicle.getId())),
                "vehicle not on board"
        );

        if (!board.canPlaceVehicle(vehicle, position)) {
            throw new PropertyVetoException("Invalid position", null);
        }

        vehicle.setPosition(position);
    }

    @Override
    public void rotateAndMove(Vehicle vehicle, Position position, boolean horizontal)
            throws PropertyVetoException {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        Contract.checkCondition(vehicle != null, "vehicle == null");
        Contract.checkCondition(position != null, "position == null");

        boolean needsRotation = horizontal != vehicle.isHorizontal();

        // Retirer temporairement le véhicule du board
        board.removeVehicle(vehicle);


        if (needsRotation) {
            vehicle.rotate();
        }

        // Vérifier si la nouvelle position est valide
        if (!board.canPlaceVehicle(vehicle, position)) {
            // Annuler la rotation
            if (needsRotation) {
                vehicle.rotate();
            }
            // Remettre le véhicule
            board.addVehicle(vehicle);
            throw new PropertyVetoException("Invalid position", null);
        }

        // Appliquer la position
        vehicle.setPosition(position);vehicle.setPosition(position);
        board.addVehicle(vehicle);
    }

    @Override
    public void loadBoard(String filename) {
        Contract.checkCondition(filename != null, "filename == null");
        board = LevelHandler.loadBoard(filename);
        pcs.firePropertyChange(PROP_BOARD, null, board);
    }

    @Override
    public void saveBoard(String filename) {
        Contract.checkCondition(filename != null, "filename == null");
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        LevelHandler.saveBoard(board, filename);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(PROP_BOARD, listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(PROP_BOARD, listener);
    }
}
