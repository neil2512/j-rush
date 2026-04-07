package jrush.app.model.logic.solver;


import jrush.app.model.Board;
import jrush.app.model.Vehicle;
import jrush.app.model.components.StdBoard;
import jrush.app.model.components.VehicleType;
import jrush.app.model.logic.solver.util.Couple;
import jrush.app.model.util.Move;
import jrush.app.util.Position;

import java.beans.PropertyVetoException;
import java.util.*;
import java.util.LinkedList;

/**
 * Implémentation de l'algorithme de recherche en largeur (Breadth-First Search)
 * pour résoudre le jeu Rush Hour.
 *
 * <pre>
 * Constructeur
 *      Entrée :
 *          – Board board
 *      Préconditions :
 *          – board != null
 * </pre>
 */
public class BreadthFirstSolver extends Solver {

    // ATTRIBUTS

    private final LinkedList<Couple<Board, List<Move>>> queue;
    private final Set<Board> alreadyVisited;

    // CONSTRUCTEUR

    public BreadthFirstSolver(Board board) {
        super(board);

        this.queue = new LinkedList<>();
        this.alreadyVisited = new HashSet<>();
    }

    // REQUETES

    @Override
    public List<Move> solve() throws PropertyVetoException {
        queue.addLast(new Couple<>(board, new ArrayList<>()));

        while (!queue.isEmpty()) {

            Couple<Board, List<Move>> temporary = queue.poll();
            Board board = new StdBoard(temporary.firstElement());
            List<Move> solution = temporary.secondElement();

            if (board.checkWinCondition()) {
                return solution;
            }

            if (!alreadyVisited.contains(board)) {
                alreadyVisited.add(board);
                List<Vehicle> listVehicles = board.getVehicles();

                for (Vehicle v : listVehicles) {
                    for (int distance = -4; distance <= 4; distance++) {
                        if (distance == 0) {
                            continue;
                        }

                        Position oldPos = v.getPosition();
                        Position newPos;

                        if (v.isHorizontal()) {
                            newPos = new Position(
                                    v.getPosition().getX() + distance,
                                    v.getPosition().getY());
                        } else {
                            newPos = new Position(v.getPosition().getX(),
                                                  v.getPosition().getY() +
                                                  distance);
                        }

                        if (board.canVehicleMove(v, oldPos, newPos)) {

                            Board newBoard = new StdBoard(board);
                            VehicleType type = VehicleType.fromId(v.getId());
                            Vehicle newVehicle = newBoard.findVehicle(type);

                            newVehicle.move(distance);
                            Move move = new Move(newVehicle, distance);

                            List<Move> newSolution = new ArrayList<>(solution);
                            newSolution.add(move);

                            queue.addLast(new Couple<>(newBoard, newSolution));
                        }
                    }
                }
            }
        }
        return null;
    }
}
