package jrush.app.model.logic.solver;


import jrush.app.model.Board;
import jrush.app.model.Vehicle;
import jrush.app.model.components.StdBoard;
import jrush.app.model.components.VehicleType;

import jrush.app.model.logic.solver.heuristic.Heuristic;
import jrush.app.model.logic.solver.heuristic.AllBlockingCars;
import jrush.app.model.logic.solver.util.Couple;
import jrush.app.model.logic.solver.util.Cost;
import jrush.app.model.logic.solver.util.Quadruple;
import jrush.app.model.logic.solver.util.GameComparator;
import jrush.app.model.util.Move;
import jrush.app.util.Position;

import java.beans.PropertyVetoException;
import java.util.*;

import jrush.app.util.Contract;

/**
 * Implémentation de l'algorithme A* pour résoudre le jeu Rush Hour. Cet
 * algorithme utilise une file de priorité pour explorer les états du jeu en
 * fonction d'une évaluation du coût total (coût déjà accumulé + estimation du
 * coût restant). L'heuristique utilisée dans cet algorithme est basée sur le
 * nombre de voitures bloquant la voiture rouge.
 *
 * <pre>
 * Constructeur
 *      Entrée :
 *          – Board board
 *      Préconditions :
 *          – board != null
 * </pre>
 */
public class AStarSolver extends Solver {

    // ATTRIBUTS

    private final PriorityQueue<Quadruple<Board, List<Move>, Heuristic, Integer>>
            priorityQueue;
    private final Map<Board, Couple<List<Move>, Integer>> alreadyVisited;

    // CONSTRUCTEUR
    public AStarSolver(Board board) {
        super(board);
        this.priorityQueue = new PriorityQueue<>(new GameComparator());
        this.alreadyVisited = new HashMap<>();
    }

    //REQUETES

    @Override
    public List<Move> solve() throws PropertyVetoException {

        List<Move> initSolution = new ArrayList<>();
        Heuristic heuristicInitial = new AllBlockingCars(board);
        int pInitial = new Cost(initSolution, heuristicInitial).getPrice();

        priorityQueue.add(new Quadruple<>(board, initSolution, heuristicInitial,
                                          pInitial));

        while (!priorityQueue.isEmpty()) {

            Quadruple<Board, List<Move>, Heuristic, Integer> temporary =
                    priorityQueue.poll();
            Board board = new StdBoard(temporary.firstElement());
            List<Move> solution = temporary.secondElement();

            if (board.checkWinCondition()) {
                return solution;
            }

            Couple<List<Move>, Integer> solutionPriceCouple =
                    new Couple<>(solution, temporary.fourthElement());

            if (!containsOrContainsBetterBoard(board, solutionPriceCouple)) {

                for (Vehicle v : board.getVehicles()) {
                    for (int distance = -4; distance <= 4; distance++) {
                        if (distance == 0) {
                            continue;
                        }

                        Position oldPos = v.getPosition();
                        Position newPos = v.isHorizontal() ?
                                          new Position(
                                                  oldPos.getX() + distance,
                                                  oldPos.getY())
                                                           :
                                          new Position(
                                                  oldPos.getX(),
                                                  oldPos.getY() + distance);


                        if (board.canVehicleMove(v, oldPos, newPos)) {

                            Board newBoard = new StdBoard(board);
                            VehicleType type = VehicleType.fromId(v.getId());
                            Vehicle newVehicle = newBoard.findVehicle(type);

                            newVehicle.move(distance);
                            Move move = new Move(newVehicle, distance);

                            List<Move> newSolution = new ArrayList<>(solution);
                            newSolution.add(move);

                            Heuristic newHeuristic =
                                    new AllBlockingCars(newBoard);
                            Cost cost = new Cost(newSolution, newHeuristic);
                            priorityQueue.add(
                                    new Quadruple<>(newBoard, newSolution,
                                                    newHeuristic,
                                                    cost.getPrice()));
                        }
                    }
                }
            }
        }
        return null;
    }

    // OUTILS

    /**
     * Vérifie si la liste alreadyVisited contient un plateau équivalent au
     * plateau passé en paramètre avec un coût total plus élevé ou égal. Si un
     * plateau équivalent est trouvé avec un coût total plus élevé ou égal, true
     * est retourné, sinon false est retourné.
     *
     * <pre>
     * Préconditions :
     *      spCouple != null
     * </pre>
     *
     * @param board Le plateau à vérifier pour trouver un plateau équivalent
     * dans la liste alreadyVisited.
     * @param spCouple Le couple contenant la solution et le coût total associé
     * au plateau à vérifier.
     *
     * @return true si la liste alreadyVisited contient un plateau équivalent
     * avec un coût total plus élevé ou égal, false sinon.
     */
    private boolean containsOrContainsBetterBoard(
            Board board, Couple<List<Move>, Integer> spCouple
    ) {
        Contract.checkCondition(spCouple != null);

        if (alreadyVisited.containsKey(board)) {
            Integer oldPrice = alreadyVisited.get(board).secondElement();

            if (oldPrice != null && oldPrice <= spCouple.secondElement()) {
                return true;
            }

        }
        alreadyVisited.put(board, spCouple);
        return false;
    }
}
