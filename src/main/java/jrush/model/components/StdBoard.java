package jrush.model.components;

import jrush.model.Board;
import jrush.model.Vehicle;
import jrush.util.Move;
import jrush.util.Position;
import util.Contract;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StdBoard implements Board, VetoableChangeListener {

    // ATTRIBUTS

    private final Map<String, Vehicle> vehicles;
    private final Map<String, Position> initials;

    private List<Move> history;
    private int cursor;

    // CONSTRUCTEUR

    public StdBoard() {
        this.vehicles = new HashMap<>();
        this.initials = new HashMap<>();
        this.history = new ArrayList<>();
        this.cursor = -1;
    }

    public StdBoard(Board other) {
        this.vehicles = new HashMap<>();
        this.initials = new HashMap<>(other.getInitialPositions());
        this.history = new ArrayList<>();
        this.cursor = -1;

        for (Vehicle v : other.getVehicles()) {
            Vehicle copy = new StdVehicle(v);
            this.vehicles.put(copy.getId(), copy);
            copy.addVetoableChangeListener(this);
        }
    }

    // REQUÊTES

    @Override
    public List<Vehicle> getVehicles() {
        return new ArrayList<>(vehicles.values());
    }

    @Override
    public Map<String, Position> getInitialPositions() {
        return new HashMap<>(initials);
    }

    @Override
    public List<Move> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public void setHistory(List<Move> history) {
        this.history = history;
    }


    @Override
    public boolean canUndo() {
        return cursor >= 0;
    }

    @Override
    public boolean canRedo() {
        return cursor < history.size() - 1;
    }

    @Override
    public boolean canVehicleMove(
            Vehicle vehicle, Position oldPos,
            Position newPos
    ) {
        Contract.checkCondition(vehicle != null, "vehicle == null");
        Contract.checkCondition(oldPos != null, "oldPos == null");
        Contract.checkCondition(newPos != null, "newPos == null");
        int deltaX = Integer.compare(newPos.getX(), oldPos.getX());
        int deltaY = Integer.compare(newPos.getY(), oldPos.getY());

        int distance = Math.max(Math.abs(newPos.getX() - oldPos.getX()),
                                Math.abs(newPos.getY() - oldPos.getY()));

        for (int i = 1; i <= distance; i++) {
            Position intermediatePos =
                    new Position(oldPos.getX() + (i * deltaX),
                                 oldPos.getY() + (i * deltaY));

            if (!validatePlacement(intermediatePos, vehicle)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canPlaceVehicle(Vehicle vehicle, Position position) {
        // Temporairement retirer le véhicule pour valider sans collision avec lui-même
        return validatePlacement(position, vehicle);
    }

    // COMMANDES

    @Override
    public void addVehicle(Vehicle vehicle) {
        Contract.checkCondition(vehicle != null, "vehicle == null");
        if (!validatePlacement(vehicle.getPosition(), vehicle)) {
            throw new IllegalArgumentException("Invalid starting position.");
        }
        vehicles.put(vehicle.getId(), vehicle);
        initials.put(vehicle.getId(), vehicle.getPosition());
        vehicle.addVetoableChangeListener(this);
    }

    @Override
    public void removeVehicle(Vehicle vehicle) {
        Contract.checkCondition(vehicle != null, "Vehicle == null");
        Contract.checkCondition(vehicles.containsKey(vehicle.getId()), "Vehicle not on board");
        vehicles.remove(vehicle.getId());
        initials.remove(vehicle.getId());
        vehicle.removeVetoableChangeListener(this);
    }

    @Override
    public void record(Move move) {
        Contract.checkCondition(move != null, "move == null");
        if (cursor < history.size() - 1) {
            history = new ArrayList<>(history.subList(0, cursor + 1));
        }
        history.add(move);
        cursor++;
    }

    @Override
    public void undo() throws PropertyVetoException {
        Contract.checkCondition(canUndo(), "!canUndo()");
        history.get(cursor).undo();
        cursor--;
    }

    @Override
    public void redo() throws PropertyVetoException {
        Contract.checkCondition(canRedo(), "!canRedo()");
        cursor++;
        history.get(cursor).redo();
    }

    @Override
    public void reset() {
        for (Vehicle v : vehicles.values()) {
            Position pos = initials.get(v.getId());
            if (v instanceof StdVehicle) {
                ((StdVehicle) v).setPosition(pos);
            }
        }
        history.clear();
        cursor = -1;
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt)
            throws PropertyVetoException {
        Vehicle vehicle = (Vehicle) evt.getSource();
        if (evt.getPropertyName().equals(Vehicle.PROP_POSITION)) {
            Position oldPos = (Position) evt.getOldValue();
            Position newPos = (Position) evt.getNewValue();

            if (!canVehicleMove(vehicle, oldPos, newPos)) {
                throw new PropertyVetoException(
                        "Invalid move for " + vehicle.getId(), null);
            }
        }
        if (evt.getPropertyName().equals(Vehicle.PROP_HORIZONTAL)) {
            // Vérifier si la nouvelle orientation est valide
            if (!canVehicleRotate(vehicle)) {
                throw new PropertyVetoException("Invalid rotation for " + vehicle.getId(), null);
            }
        }
    }

    private boolean canVehicleRotate(Vehicle vehicle) {
        int size = vehicle.getSize();
        Position pos = vehicle.getPosition();
        // Orientation inversée
        boolean newHorizontal = !vehicle.isHorizontal();

        // Vérifier les limites du plateau
        if (newHorizontal && pos.getX() + size > GRID_SIZE) return false;
        if (!newHorizontal && pos.getY() + size > GRID_SIZE) return false;

        // Vérifier les collisions avec les autres véhicules
        for (Vehicle other : vehicles.values()) {
            // Simuler le véhicule avec la nouvelle orientation
            if (other.getId().equals(vehicle.getId())) continue;
            Vehicle rotated = new StdVehicle(
                    VehicleType.fromId(vehicle.getId()),
                    newHorizontal,
                    pos
            );
            if (intersect(pos, rotated, other)) return false;
        }
        return true;
    }

    // OUTILS

    /**
     * Valide que le véhicule peut être placé à la position donnée sans sortir
     * du plateau ni entrer en collision avec d'autres véhicules.
     *
     * @param position Position proposée pour le véhicule
     * @param vehicle Le véhicule à placer
     *
     */
    private boolean validatePlacement(Position position, Vehicle vehicle) {
        int size = vehicle.getSize();
        boolean isHorizontal = vehicle.isHorizontal();

        if (position.getX() < 0 || position.getY() < 0 ||
            (isHorizontal && position.getX() + size > GRID_SIZE) ||
            (!isHorizontal && position.getY() + size > GRID_SIZE)) {
            return false;
        }

        for (Vehicle other : vehicles.values()) {
            if (other == vehicle) {
                continue;
            }
            if (intersect(position, vehicle, other)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie si les deux véhicules se chevauchent à leurs positions données.
     *
     * @param v1 Premier véhicule
     * @param v2 Deuxième véhicule
     *
     * @return true si les véhicules se chevauchent, false sinon
     */
    private boolean intersect(Position p1, Vehicle v1, Vehicle v2) {
        Position p2 = v2.getPosition();
        int s1 = v1.getSize();
        int s2 = v2.getSize();
        boolean h1 = v1.isHorizontal();
        boolean h2 = v2.isHorizontal();

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
