package jrush.app.model.logic.solver.heuristic;

import jrush.app.model.Board;
import jrush.app.model.Vehicle;
import jrush.app.util.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une heuristique qui compte le nombre de véhicules qui
 * bloquent directement la sortie de la voiture rouge. Cette heuristique est
 * utilisée pour guider la recherche d'une solution en donnant une estimation
 * du coût restant à chaque étape de la recherche.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          – Board b
 *      Postconditions :
 *          – getBoard() == b
 *          – getRedCar() == b.findVehicle(VehicleType.RED_CAR)
 * </pre>
 */
public class DirectlyBlockingCars extends BlockingCars {

    // ATTRIBUTS

    // CONSTRUCTEURS

    public DirectlyBlockingCars(Board board) {
        super(board);
        score = createDirectlyBlockingRedCarsExitList().size();
    }

    // COMMANDES

    /**
     * Crée une liste de véhicules qui bloquent directement la sortie de la
     * voiture rouge.
     *
     * @return une liste de véhicules qui bloquent directement la sortie de la
     * voiture rouge.
     */
    public List<Vehicle> createDirectlyBlockingRedCarsExitList() {
        List<Vehicle> list = new ArrayList<>();
        for (Vehicle vehicle : board.getVehicles()) {
            boolean result = isBlockingRedCarsExit(vehicle);
            if (result) {
                list.add(vehicle);
            }
        }
        return list;
    }

    /**
     * Vérifie si un véhicule bloque directement la sortie de la voiture rouge.
     *
     * @param vehicle le véhicule à vérifier
     *
     * @return true si le véhicule bloque directement la sortie de la voiture
     * rouge, false sinon.
     */
    public boolean isBlockingRedCarsExit(Vehicle vehicle) {
        for (int x = redVehicle.getTail().getX() + 1; x < Board.GRID_SIZE;
             x++) {

            List<Position> positions = vehicle.getOccupiedPositions();
            Position position =
                    new Position(x, redVehicle.getPosition().getY());
            if (isContainingPosition(positions, position)) {
                return true;
            }
        }
        return false;
    }
}
