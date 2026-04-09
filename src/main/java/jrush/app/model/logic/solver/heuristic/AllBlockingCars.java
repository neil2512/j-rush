package jrush.app.model.logic.solver.heuristic;

import jrush.app.model.Board;
import jrush.app.model.Vehicle;
import jrush.app.util.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une heuristique basée sur le nombre total de voitures
 * bloquantes pour un solveur de jeu. Cette heuristique estime le coût restant
 * pour atteindre la victoire en comptant le nombre total de voitures qui
 * bloquent directement ou indirectement le chemin du véhicule rouge vers la
 * sortie. Elle est utilisée pour guider la recherche d'une solution en donnant
 * une estimation du coût restant à chaque étape de la recherche.
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
