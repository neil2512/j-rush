package jrush.app.model.logic.solver.heuristic;

import jrush.app.model.Board;
import jrush.app.model.Vehicle;
import jrush.app.util.Contract;

import java.util.ArrayList;
import java.util.List;

public class AllBlockingCars extends BlockingCars {

    // ATTRIBUTS

    private final List<Vehicle> directlyBlockingRedCarList;

    private int score;

    // CONSTRUCTEUR

    public AllBlockingCars(Board b) {
        super(b);
        directlyBlockingRedCarList = createTargetblockingCarsList(redVehicle);
        score = createAllBlockingCarsList().size();
    }

    // COMMANDES

    /**
     * Calcule la liste de tous les véhicules bloquant indirectement le véhicule
     * rouge.
     *
     * @return la liste de tous les véhicules bloquant indirectement le véhicule
     * rouge.
     */
    public List<Vehicle> createIndirecltlyRedCarBlokingList() {
        Contract.checkCondition(directlyBlockingRedCarList != null);
        List<Vehicle> list = new ArrayList<>();
        for (Vehicle vehicle : directlyBlockingRedCarList) {
            List<Vehicle> blocking = createTargetblockingCarsList(vehicle);
            list.addAll(blocking);
        }
        return list;
    }

    /**
     * Calcule la liste de tous les véhicules bloquant directement ou
     * indirectement le véhicule rouge.
     *
     * @return la liste de tous les véhicules bloquant directement ou
     * indirectement le véhicule rouge.
     */
    public List<Vehicle> createAllBlockingCarsList() {
        List<Vehicle> list = new ArrayList<>(directlyBlockingRedCarList);
        list.addAll(createIndirecltlyRedCarBlokingList());
        return list;
    }
}
