package jrush.app.model.logic.solver;


import jrush.app.model.Board;
import jrush.app.model.Vehicle;
import jrush.app.model.components.StdBoard;
import jrush.app.model.components.VehicleType;

import jrush.app.model.logic.solver.heuristic.Heuristic;
import jrush.app.model.logic.solver.heuristic.AllBlockingCars;
import jrush.app.model.logic.solver.util.Price;
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
    private final List<Quadruple<Board, List<Move>, Heuristic, Integer>>
            alreadyVisited;

    // CONSTRUCTEUR
    public AStarSolver(Board board) {
        super(board);
        this.priorityQueue = new PriorityQueue<>(new GameComparator());
        this.alreadyVisited = new ArrayList<>();
    }

    //REQUETES

    @Override
    public List<Move> solve() throws PropertyVetoException {

        List<Move> initSolution = new ArrayList<>();
        Heuristic heuristicInitial = new AllBlockingCars(board);
        int pInitial = new Price(initSolution, heuristicInitial).getPrice();

        priorityQueue.add(new Quadruple<>(board, initSolution,
                                          heuristicInitial, pInitial));

        while (!priorityQueue.isEmpty()) {

            Quadruple<Board, List<Move>, Heuristic, Integer> temporary =
                    priorityQueue.poll();
            Board board = new StdBoard(temporary.firstElement());
            List<Move> solution = temporary.secondElement();

            if (board.checkWinCondition()) {
                return solution;
            }

            if (!containsOrContainsBetterBoard(temporary)) {

                for (Vehicle v : board.getVehicles()) {
                    for (int distance = -4; distance <= 4; distance++) {
                        if (distance == 0) {
                            continue;
                        }

                        Position oldPos = v.getPosition();
                        Position newPos = v.isHorizontal() ? new Position(
                                oldPos.getX() + distance, oldPos.getY())
                                                           : new Position(
                                                                   oldPos.getX(),
                                                                   oldPos.getY() +
                                                                   distance);


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
                            priorityQueue.add(
                                    new Quadruple<>(newBoard, newSolution,
                                                    newHeuristic,
                                                    new Price(newSolution,
                                                              newHeuristic).getPrice()));
                        }
                    }
                }
            }
        }
        return null;
    }

    // OUTILS

    /**
     * Vérifie si la liste alreadyVisited contient le plateau du quadruple passé
     * en paramètre ou un plateau équivalent avec un coût total plus élevé. Si
     * un plateau équivalent avec un coût total plus élevé est trouvé, il est
     * remplacé par le quadruple actuel dans la liste alreadyVisited.
     *
     * <pre>
     * Préconditions :
     *      currentElement != null
     * </pre>
     *
     * @param currentElement Le quadruple contenant le plateau à vérifier, la
     * solution associée, l'heuristique et le coût total.
     *
     * @return true si la liste alreadyVisited contient un plateau équivalent
     * avec un coût total plus élevé ou égal, false sinon.
     */
    private boolean containsOrContainsBetterBoard(
            Quadruple<Board, List<Move>, Heuristic, Integer> currentElement
    ) {
        Contract.checkCondition(currentElement != null);
        Quadruple<Board, List<Move>, Heuristic, Integer> result =
                containsBoard(currentElement.firstElement());
        if (result != null) {
            if (currentElement.fourthElement() < result.fourthElement()) {
                this.alreadyVisited.remove(result);
                this.alreadyVisited.add(currentElement);
                return false;
            } else {
                return true;
            }
        } else {
            this.alreadyVisited.add(currentElement);
            return false;
        }


    }

    /**
     * Vérifie si la liste alreadyVisited contient un plateau équivalent au
     * plateau passé en paramètre. Si un plateau équivalent est trouvé, le
     * quadruple correspondant est retourné, sinon null est retourné.
     *
     * <pre>
     * Préconditions :
     *      board != null
     * </pre>
     *
     * @param board Le plateau à vérifier pour trouver un plateau équivalent
     * dans la liste alreadyVisited.
     *
     * @return Le quadruple contenant le plateau équivalent, la solution
     * associée, l'heuristique et le coût total, ou null si aucun plateau
     * équivalent n'est trouvé.
     */
    private Quadruple<Board, List<Move>, Heuristic, Integer> containsBoard(
            Board board
    ) {
        Contract.checkCondition(board != null);
        for (Quadruple<Board, List<Move>, Heuristic, Integer> e : this.alreadyVisited) {
            Board boardFromElement = e.firstElement();
            if (board.equals(boardFromElement)) {
                return e;
            }
        }
        return null;
    }
}
