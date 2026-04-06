package jrush.app.model.components;

import jrush.app.model.Board;
import jrush.app.model.Vehicle;
import jrush.app.model.util.Move;
import jrush.app.model.util.Placement;
import jrush.app.util.Position;
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

    // CONSTRUCTEURS

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
    public Vehicle findVehicle(VehicleType type) {
        Contract.checkCondition(type != null, "type == null");
        for (Vehicle v : vehicles.values()) {
            if (v.getId().equals(type.getId())) {
                return v;
            }
        }
        return null;
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
    public boolean canUndo() {
        return cursor >= 0;
    }

    @Override
    public boolean canRedo() {
        return cursor < history.size() - 1;
    }

    @Override
    public boolean canVehicleMove(
            Vehicle vehicle, Position oldPosition, Position newPosition) {
        Contract.checkCondition(vehicle != null, "vehicle == null");
        Contract.checkCondition(oldPosition != null, "oldPosition == null");
        Contract.checkCondition(newPosition != null, "newPosition == null");

        int deltaX = Integer.compare(newPosition.getX(), oldPosition.getX());
        int deltaY = Integer.compare(newPosition.getY(), oldPosition.getY());
        int distance =
                Math.max(Math.abs(newPosition.getX() - oldPosition.getX()),
                         Math.abs(newPosition.getY() - oldPosition.getY()));

        for (int i = 1; i <= distance; i++) {
            boolean horizontal = vehicle.isHorizontal();
            Position intermediatePos =
                    new Position(oldPosition.getX() + (i * deltaX),
                                 oldPosition.getY() + (i * deltaY));

            if (!validatePlacement(horizontal, intermediatePos, vehicle)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkWinCondition() {
        for (Vehicle v : getVehicles()) {
            if (v.getId().equals(Vehicle.WIN_CAR.getId())) {

                boolean correctRow =
                        v.getPosition().getY() == Board.EXIT_POSITION.getY();
                boolean correctCol =
                        (v.getPosition().getX() + v.getSize() - 1) ==
                        Board.EXIT_POSITION.getX();

                return correctCol && correctRow;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        StdBoard that = (StdBoard) obj;
        return vehicles.equals(that.vehicles);
    }

    @Override
    public int hashCode() {
        return vehicles.hashCode();
    }

    // COMMANDES

    @Override
    public void addVehicle(Vehicle vehicle) {
        Contract.checkCondition(vehicle != null, "vehicle == null");
        if (!validatePlacement(vehicle.isHorizontal(), vehicle.getPosition(),
                               vehicle)) {
            throw new IllegalArgumentException("Invalid starting position.");
        }
        vehicles.put(vehicle.getId(), vehicle);
        initials.put(vehicle.getId(), vehicle.getPosition());
        vehicle.addVetoableChangeListener(this);
    }

    @Override
    public void removeVehicle(Vehicle vehicle) {
        Contract.checkCondition(vehicle != null, "vehicle == null");
        vehicles.remove(vehicle.getId());
        initials.remove(vehicle.getId());
        vehicle.removeVetoableChangeListener(this);
    }

    @Override
    public void setHistory(List<Move> history) {
        this.history = history;
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
    public void vetoableChange(PropertyChangeEvent event)
            throws PropertyVetoException {
        Vehicle vehicle = (Vehicle) event.getSource();

        if (event.getPropertyName().equals(StdVehicle.PROP_PLACEMENT)) {
            Placement placement = (Placement) event.getNewValue();
            boolean horizontal = placement.horizontal();
            Position position = placement.position();

            if (!validatePlacement(horizontal, position, vehicle)) {
                throw new PropertyVetoException(
                        "Invalid operation for " + vehicle.getId(), event);
            }
        }

        if (event.getPropertyName().equals(Vehicle.PROP_POSITION)) {
            Position oldPos = (Position) event.getOldValue();
            Position newPos = (Position) event.getNewValue();

            if (!canVehicleMove(vehicle, oldPos, newPos)) {
                throw new PropertyVetoException(
                        "Invalid operation for " + vehicle.getId(), event);
            }
        }
    }

    // OUTILS

    /**
     * Vérifie si le placement proposé pour un véhicule est valide, c'est-à-dire
     * qu'il ne sort pas des limites du plateau et ne chevauche pas un autre
     * véhicule.
     *
     * @param horizontal L'orientation du véhicule
     * @param position La position proposée pour le véhicule
     * @param vehicle Le véhicule à placer
     *
     */
    private boolean validatePlacement(
            boolean horizontal, Position position,
            Vehicle vehicle
    ) {
        int size = vehicle.getSize();

        if (position.getX() < 0 || position.getY() < 0 ||
            (horizontal && position.getX() + size > GRID_SIZE) ||
            (!horizontal && position.getY() + size > GRID_SIZE)) {
            return false;
        }

        for (Vehicle other : vehicles.values()) {
            if (other.getId().equals(vehicle.getId())) {
                continue;
            }
            if (intersect(position, horizontal, vehicle, other)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie si le placement proposé pour un véhicule chevauche un autre
     * véhicule.
     *
     * @param p1 Position souhaitée du premier véhicule
     * @param h1 Orientation souhaitée du premier véhicule
     * @param v1 Premier véhicule
     * @param v2 Deuxième véhicule
     *
     * @return true si les véhicules se chevauchent, false sinon
     */
    private boolean intersect(Position p1, boolean h1, Vehicle v1, Vehicle v2) {
        Position p2 = v2.getPosition();
        boolean h2 = v2.isHorizontal();
        int s1 = v1.getSize();
        int s2 = v2.getSize();

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
