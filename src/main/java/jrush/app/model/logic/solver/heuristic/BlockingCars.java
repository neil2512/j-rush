package jrush.app.model.logic.solver.heuristic;

import jrush.app.model.Board;
import jrush.app.model.Vehicle;
import jrush.app.model.components.VehicleType;
import jrush.app.util.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite représentant une heuristique basée sur les voitures
 * bloquantes pour un solveur de jeu. Cette heuristique estime le coût restant
 * pour atteindre la victoire en comptant le nombre de voitures qui bloquent le
 * chemin du véhicule rouge vers la sortie. Elle est utilisée pour guider la
 * recherche d'une solution en donnant une estimation du coût restant à chaque
 * étape de la recherche.
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
public abstract class BlockingCars implements Heuristic {


    // ATTRIBUTS
    protected Vehicle redVehicle;
    protected Board board;
    protected int score;

    // CONSTRUCTEURS

    public BlockingCars(Board board) {
        this.redVehicle = board.findVehicle(VehicleType.RED_CAR);
        this.board = board;
    }


    // REQUETES

    /**
     * Retourne le plateau de jeu associé à cette heuristique.
     *
     * @return le plateau de jeu associé à cette heuristique.
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Retourne la liste des véhicules qui bloquent directement le véhicule
     * rouge.
     *
     * @return la liste des véhicules qui bloquent directement le véhicule
     * rouge.
     */
    public int getScore() {
        return this.score;
    }


    // COMMANDES

    /**
     * Retourne la liste des véhicules qui bloquent directement le véhicule
     * cible.
     *
     * @param target le véhicule cible
     *
     * @return la liste des véhicules qui bloquent directement le véhicule
     * cible.
     */
    public List<Vehicle> createTargetblockingCarsList(Vehicle target) {
        List<Vehicle> list = new ArrayList<>();
        for (Vehicle vehicle : board.getVehicles()) {
            boolean result = isVehicleblockingTarget(target, vehicle);
            if (result) {
                list.add(vehicle);
            }
        }
        return list;
    }


    /**
     * Retourne true si le véhicule donné bloque directement le véhicule cible,
     * false sinon.
     *
     * @param target le véhicule cible
     * @param vehicle le véhicule à vérifier
     *
     * @return true si le véhicule donné bloque directement le véhicule cible,
     * false sinon.
     */
    public boolean isVehicleblockingTarget(Vehicle target, Vehicle vehicle) {

        List<Position> bPositions = vehicle.getOccupiedPositions();

        if (target.isHorizontal()) {
            Position leftMove = new Position(target.getPosition().getX() - 1,
                                             target.getPosition().getY());
            Position rightMove = new Position(target.getTail().getX() + 1,
                                              target.getPosition().getY());
            return isContainingPosition(bPositions, leftMove) ||
                   isContainingPosition(bPositions, rightMove);
        } else {
            Position upMove = new Position(target.getPosition().getX(),
                                           target.getPosition().getY() - 1);
            Position downMove = new Position(target.getPosition().getX(),
                                             target.getTail().getY() + 1);
            return isContainingPosition(bPositions, upMove) ||
                   isContainingPosition(bPositions, downMove);
        }
    }

    public boolean isContainingPosition(List<Position> l, Position p) {
        for (Position po : l) {
            if (po.equals(p)) {
                return true;
            }
        }
        return false;
    }
}
