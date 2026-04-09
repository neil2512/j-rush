package jrush.app.model.logic;

import jrush.app.model.Board;
import jrush.app.model.Vehicle;
import jrush.app.model.components.StdBoard;
import jrush.app.model.components.StdVehicle;
import jrush.app.model.components.VehicleType;
import jrush.app.model.logic.solver.AStarSolver;
import jrush.app.model.util.Move;
import jrush.app.util.Contract;
import jrush.app.util.Position;

import java.util.*;

/**
 * Classe responsable de la génération de niveaux pour le jeu Rush Hour. Elle
 * fournit une méthode pour créer un plateau de jeu avec une configuration
 * aléatoire de véhicules, tout en garantissant que le niveau généré est
 * solvable et correspond à une difficulté spécifiée (en termes de nombre de
 * mouvements nécessaires pour résoudre le niveau). La génération se fait par
 * essais successifs, en créant des configurations aléatoires et en évaluant
 * leur difficulté jusqu'à ce qu'un niveau satisfaisant soit trouvé ou que le
 * nombre maximum d'essais soit atteint.
 */
public class LevelGenerator {

    // CONSTANTES

    public static final int DEFAULT_OBSTACLE_NB = 6;

    private static final int STUCK_LIMIT = 75;
    private static final int MAX_ATTEMPTS = 35;

    private static final Random random = new Random();

    // REQUETES

    /**
     * Gé&anère un niveau de jeu avec une difficulté comprise entre minMoves et
     * maxMoves. La méthode crée un plateau de jeu en partant de la
     * configuration gagnante, ajoute un nombre aléatoire de véhicules
     * (obstacles), puis effectue une série de mouvements aléatoires pour
     * mélanger le plateau. Après chaque tentative, la difficulté du niveau est
     * évaluée en utilisant un algorithme de résolution (A*), et si elle
     * correspond aux critères spécifiés, le niveau est retourné. Si aucun
     * niveau satisfaisant n'est trouvé après le nombre maximum d'essais, la
     * méthode retourne null.
     *
     * <pre>
     * Préconditions :
     *       minMoves > 0
     *       minMoves <= maxMoves
     * </pre>
     *
     * @param minMoves Le nombre minimum de mouvements nécessaires pour résoudre
     * le niveau.
     * @param maxMoves Le nombre maximum de mouvements nécessaires pour résoudre
     * le niveau.
     */
    public static Board generateLevel(int minMoves, int maxMoves) {
        Contract.checkCondition(minMoves > 0, "minMoves <= 0");
        Contract.checkCondition(maxMoves >= minMoves, "maxMoves < minMoves");

        int globalAttempts = 0;

        while (globalAttempts < MAX_ATTEMPTS) {
            globalAttempts++;

            Board currentBoard = createStartBoard();
            int currentDifficulty = evaluateDifficulty(currentBoard);
            if (currentDifficulty == -1) {
                continue;
            }

            int stuckCounter = 0;

            while (stuckCounter < STUCK_LIMIT) {

                if (currentDifficulty >= minMoves &&
                    currentDifficulty <= maxMoves) {
                    currentBoard.setHistory(new ArrayList<>());
                    return currentBoard;
                }

                Board mutatedBoard = mutateBoard(currentBoard);
                int newDifficulty = evaluateDifficulty(mutatedBoard);

                if (newDifficulty != -1) {
                    if (newDifficulty > currentDifficulty) {
                        currentBoard = mutatedBoard;
                        currentDifficulty = newDifficulty;
                        stuckCounter = 0;
                    } else if (newDifficulty == currentDifficulty) {
                        currentBoard = mutatedBoard;
                        stuckCounter++;
                    } else {
                        stuckCounter++;
                    }
                } else {
                    stuckCounter++;
                }

            }
        }
        return null;
    }

    // OUTILS

    /**
     * Crée un plateau de jeu de départ avec la voiture rouge placée dans sa
     * position initiale (généralement en bas à gauche du plateau). La voiture
     * rouge est le véhicule que le joueur doit faire sortir du plateau pour
     * gagner.
     *
     * @return Un plateau de jeu avec la voiture rouge placée dans sa position
     * de départ.
     */
    private static Board createStartBoard() {
        Board board = new StdBoard();
        int startX = random.nextInt(2);
        VehicleType redCarType = VehicleType.RED_CAR;
        Position startPosition =
                new Position(startX, Board.EXIT_POSITION.getY());
        Vehicle redCar = new StdVehicle(redCarType, true, startPosition);
        board.addVehicle(redCar);

        for (int i = 0; i < DEFAULT_OBSTACLE_NB; i++) {
            addSingleRandomVehicle(board);
        }

        return board;
    }

    /**
     * Transforme le plateau de jeu en effectuant une des trois actions
     * suivantes :
     *
     * <pre>
     * - 1. Ajouter un véhicule aléatoire (20% de chance) : Un nouveau véhicule
     * est généré et ajouté au plateau à une position aléatoire, en respectant
     * les règles du jeu.
     * - 2. Supprimer un véhicule aléatoire (30% de chance) : Un véhicule
     * existant (autre que la voiture rouge) est aléatoirement sélectionné et
     * retiré du plateau.
     * - 3. Déplacer un véhicule aléatoire (50% de chance) : Un véhicule
     * existant (autre que la voiture rouge) est aléatoirement sélectionné et
     * déplacé d'une case dans une direction aléatoire, en respectant les
     * règles du jeu.
     * </pre>
     *
     * @param original Le plateau de jeu à muter.
     *
     * @return Un nouveau plateau de jeu résultant de la mutation du plateau
     * original.
     */
    private static Board mutateBoard(Board original) {
        Board mutated = new StdBoard();

        List<Vehicle> currentVehicles = new ArrayList<>(original.getVehicles());
        Vehicle redCar = null;
        for (Vehicle v : currentVehicles) {
            if (VehicleType.fromId(v.getId()) == VehicleType.RED_CAR) {
                redCar = v;
            }
        }
        currentVehicles.remove(redCar);


        // 20% Suppression - 50% Ajouter - 30% Déplacer
        int action = random.nextInt(10);

        if (action >= 5 && action < 8 && !currentVehicles.isEmpty()) {
            int index = random.nextInt(currentVehicles.size());
            Vehicle vToMove = currentVehicles.get(index);

            int delta = random.nextBoolean() ? 1 : -1;

            int newX = vToMove.getPosition().getX() +
                       (vToMove.isHorizontal() ? delta : 0);
            int newY = vToMove.getPosition().getY() +
                       (!vToMove.isHorizontal() ? delta : 0);

            VehicleType type = VehicleType.fromId(vToMove.getId());
            boolean horizontal = vToMove.isHorizontal();
            Position newPos = new Position(newX, newY);

            Vehicle movedVehicle = new StdVehicle(type, horizontal, newPos);
            currentVehicles.set(index, movedVehicle);
        } else if (action >= 8 && !currentVehicles.isEmpty()) {
            currentVehicles.remove(random.nextInt(currentVehicles.size()));
        }

        mutated.addVehicle(new StdVehicle(redCar));

        for (Vehicle v : currentVehicles) {
            try {
                mutated.addVehicle(new StdVehicle(v));
            } catch (Exception ignored) {
            }
        }

        if (action < 5) {
            addSingleRandomVehicle(mutated);
        }

        return mutated;
    }

    /**
     * Ajoute le nombre de véhicules passés en paramètre (autres que la voiture
     * rouge) au plateau de jeu. Les véhicules sont placés de manière aléatoire,
     * en respectant les règles du jeu.
     *
     * @param board Le plateau de jeu sur lequel ajouter les véhicules.
     */
    private static void addSingleRandomVehicle(Board board) {
        List<VehicleType> availableTypes =
                new ArrayList<>(Arrays.asList(VehicleType.values()));

        for (Vehicle v : board.getVehicles()) {
            availableTypes.remove(VehicleType.fromId(v.getId()));
        }

        if (availableTypes.isEmpty()) {
            return;
        }

        Collections.shuffle(availableTypes);
        VehicleType type = availableTypes.getFirst();

        for (int i = 0; i < 15; i++) {
            boolean isHorizontal = random.nextBoolean();
            int x = random.nextInt(Board.GRID_SIZE);
            int y = random.nextInt(Board.GRID_SIZE);

            try {
                board.addVehicle(new StdVehicle(type, isHorizontal,
                                                new Position(x, y)));
                break;
            } catch (IllegalArgumentException | AssertionError ignored) {
            }
        }
    }

    /**
     * Évalue la difficulté d'un plateau de jeu en utilisant un algorithme de
     * résolution (A*). La difficulté est mesurée par le nombre de mouvements
     * nécessaires pour résoudre le plateau à partir de sa configuration
     * actuelle. Si le plateau est insolvable, la méthode retourne -1.
     *
     * @param board Le plateau de jeu à évaluer.
     *
     * @return Le nombre de mouvements nécessaires pour résoudre le plateau, ou
     * -1 si le plateau est insolvable.
     */
    private static int evaluateDifficulty(Board board) {
        try {
            AStarSolver solver = new AStarSolver(board);
            List<Move> solution = solver.solve();
            return (solution != null) ? solution.size() : -1;
        } catch (Exception e) {
            return -1;
        }
    }
}
