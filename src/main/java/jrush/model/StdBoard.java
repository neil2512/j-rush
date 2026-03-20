package jrush.model;

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

    // CONSTRUCTEUR

    public StdBoard() {
        this.vehicles = new HashMap<>();
    }

    // REQUÊTES

    @Override
    public List<Vehicle> getVehicles() {
        return new ArrayList<>(vehicles.values());
    }

    // COMMANDES

    @Override
    public void addVehicle(Vehicle vehicle) {
        Contract.checkCondition(vehicle != null, "vehicle == null");
        try {
            validatePlacement(vehicle.getPosition(), vehicle.getSize(),
                              vehicle.isHorizontal(), vehicle.getId(), null);
            vehicles.put(vehicle.getId(), vehicle);
            vehicle.addVetoableChangeListener(this);
        } catch (PropertyVetoException e) {
            throw new IllegalArgumentException(
                    "Invalid starting position: " + e.getMessage());
        }
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt)
            throws PropertyVetoException {

        Vehicle v = (Vehicle) evt.getSource();
        Position newPos = (Position) evt.getNewValue();

        validatePlacement(newPos, v.getSize(), v.isHorizontal(), v.getId(), v);
    }

    // OUTILS

    /**
     * Valide que le véhicule peut être placé à la position donnée sans sortir
     * du plateau ni entrer en collision avec d'autres véhicules.
     *
     * @param pos Position proposée pour le véhicule
     * @param size Taille du véhicule
     * @param isHorizontal Orientation du véhicule (true si horizontal)
     * @param id Identifiant du véhicule (pour les messages d'erreur)
     * @param toIgnore Véhicule à ignorer lors de la vérification des
     * collisions
     *
     * @throws PropertyVetoException si la position est invalide (hors limites
     * ou collision).
     */
    private void validatePlacement(
            Position pos, int size, boolean isHorizontal,
            String id, Vehicle toIgnore
    ) throws PropertyVetoException {

        if (pos.getX() < 0 || pos.getY() < 0 ||
            (isHorizontal && pos.getX() + size > GRID_SIZE) ||
            (!isHorizontal && pos.getY() + size > GRID_SIZE)) {
            throw new PropertyVetoException("Out of bounds for " + id, null);
        }

        for (Vehicle other : vehicles.values()) {
            if (other == toIgnore) {
                continue;
            }
            if (intersect(pos, size, isHorizontal, other.getPosition(),
                          other.getSize(), other.isHorizontal())) {
                throw new PropertyVetoException(
                        "Collision between " + id + " and " + other.getId(),
                        null);
            }
        }
    }

    /**
     * Vérifie si les deux véhicules se chevauchent à leurs positions données.
     *
     * @param p1 Position du premier véhicule
     * @param s1 Taille du premier véhicule
     * @param h1 Orientation du premier véhicule (true si horizontal)
     * @param p2 Position du second véhicule
     * @param s2 Taille du second véhicule
     * @param h2 Orientation du second véhicule (true si horizontal)
     *
     * @return true si les véhicules se chevauchent, false sinon
     */
    private boolean intersect(
            Position p1, int s1, boolean h1, Position p2,
            int s2, boolean h2
    ) {
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
