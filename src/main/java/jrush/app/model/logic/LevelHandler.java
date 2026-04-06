package jrush.app.model.logic;

import jrush.app.model.Board;
import jrush.app.model.Vehicle;
import jrush.app.model.components.StdBoard;
import jrush.app.model.components.StdVehicle;
import jrush.app.model.components.VehicleType;
import jrush.app.model.util.Move;
import jrush.app.util.Position;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitaire pour charger et sauvegarder les niveaux de jeu à partir de
 * fichiers texte. Utilise des flux de caractères pour lire et écrire les
 * fichiers, assurant ainsi une compatibilité avec les encodages de texte
 * courants
 *
 * <pre>
 * Le format du fichier est le suivant :
 *      [METADATA]
 *      moves: <nombre de mouvements>
 *      time: <temps écoulé au format mm:ss>
 *      [BOARD]
 *      <type>;<orientation>;<positionX>;<positionY>
 *      ...
 *      [HISTORY]
 *      <type>;<orientation>;<positionX>;<positionY>;<delta>
 *      ...
 * </pre>
 *
 */
public class LevelHandler {

    // CONSTANTES

    public static final String EXTENSION = ".jrush";

    private static final String SECTION_METADATA = "[METADATA]";
    private static final String SECTION_BOARD = "[BOARD]";
    private static final String SECTION_HISTORY = "[HISTORY]";

    private static final String INNER_SEPARATOR = ";";
    private static final String MOVE_CURSOR = "cursor:";
    private static final String MOVE_LABEL = "moves:";
    private static final String TIME_LABEL = "time:";

    public static final int DEFAULT_CURSOR = -1;
    public static final int DEFAULT_MOVE = 0;
    public static final int DEFAULT_TIME = 0;

    // REQUETES

    /**
     * Tente de charger un plateau de jeu à partir d'un flux d'entrée. Le flux
     * d'entrée doit être formaté selon les spécifications décrites dans la
     * documentation de la classe, sinon une exception est levée. En cas de
     * réussite, les écouteurs de changements de propriété du plateau de jeu
     * sont correctement initialisés.
     *
     * @param inputStream Le flux d'entrée à partir duquel charger le plateau de
     * jeu
     *
     * @return Un objet LoadResult encapsulant le plateau de jeu chargé, le
     * nombre de mouvements effectués et le temps écoulé.
     *
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de la
     * lecture du fichier
     */
    public static LoadResult loadBoard(InputStream inputStream)
            throws IOException {
        Board board = new StdBoard();
        int cursor = DEFAULT_CURSOR;
        int moves = DEFAULT_MOVE;
        int time = DEFAULT_TIME;

        InputStreamReader streamReader = new InputStreamReader(inputStream);
        try (BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            String currentSection = "";
            List<Move> history = new ArrayList<>();

            while ((line = reader.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.startsWith("[")) {
                    currentSection = line;
                    continue;
                }

                String[] parts = line.split(INNER_SEPARATOR);
                switch (currentSection) {
                    case SECTION_METADATA:
                        if (line.startsWith(MOVE_CURSOR)) {
                            cursor = Integer.parseInt(line.split(":")[1]);
                        }
                        if (line.startsWith(MOVE_LABEL)) {
                            moves = Integer.parseInt(line.split(":")[1]);
                        }
                        if (line.startsWith(TIME_LABEL)) {
                            time = Integer.parseInt(line.split(":")[1]);
                        }
                        break;

                    case SECTION_BOARD:
                        if (parts.length == 4) {
                            VehicleType type = VehicleType.fromId(parts[0]);
                            boolean horizontal = "1".equals(parts[1]);
                            Position position =
                                    new Position(Integer.parseInt(parts[2]),
                                                 Integer.parseInt(parts[3]));

                            Vehicle v =
                                    new StdVehicle(type, horizontal, position);
                            board.addVehicle(v);
                        }
                        break;

                    case SECTION_HISTORY:
                        if (parts.length == 5) {
                            Vehicle v = board.findVehicle(
                                    VehicleType.fromId(parts[0]));
                            int delta = Integer.parseInt(parts[4]);
                            if (v != null) {
                                history.add(new Move(v, delta));
                            }
                        }
                        break;
                }
            }
            board.setHistory(history);
            board.setHistoryCursor(cursor);
            return new LoadResult(board, moves, time);
        }
    }

    /**
     * Tente de charger un plateau de jeu à partir d'un fichier texte. Le
     * fichier est formaté selon les spécifications décrites dans la
     * documentation de la classe, sinon une exception est levée. En cas de
     * réussite, les écouteurs de changements de propriété du plateau de jeu
     * sont correctement initialisés.
     *
     * @param filename Le nom du fichier à partir duquel charger le plateau de
     * jeu
     *
     * @return Un objet LoadResult encapsulant le plateau de jeu chargé, le
     * nombre de mouvements effectués et le temps écoulé.
     *
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de la
     * lecture du fichier
     */
    public static LoadResult loadBoard(String filename) throws IOException {
        try (InputStream is = new FileInputStream(filename)) {
            return loadBoard(is);
        }
    }

    // COMMANDES

    /**
     * Tente de sauvegarder le plateau de jeu dans un fichier texte. Le fichier
     * est formaté selon les spécifications décrites dans la documentation de la
     * classe. Si le nom du fichier ne se termine pas par l'extension .jrush,
     * celle-ci est automatiquement ajoutée. Si une erreur d'entrée/sortie se
     * produit lors de l'écriture du fichier, une IOException est levée.
     *
     * @param board Le plateau de jeu à sauvegarder
     * @param moves Le nombre de mouvements effectués
     * @param time Le temps écoulé au format "mm:ss"
     * @param filename Le nom du fichier dans lequel sauvegarder le niveau
     *
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de
     * l'écriture
     */
    public static void saveBoard(
            Board board, int moves, int time, String filename)
            throws IOException {

        if (!filename.endsWith(EXTENSION)) {
            filename += EXTENSION;
        }

        FileWriter fileWriter = new FileWriter(filename);
        try (BufferedWriter writer = new BufferedWriter(fileWriter)) {

            writer.write(SECTION_METADATA);
            writer.newLine();
            writer.write(MOVE_CURSOR + board.getHistoryCursor());
            writer.newLine();
            writer.write(MOVE_LABEL + moves);
            writer.newLine();
            writer.write(TIME_LABEL + time);
            writer.newLine();

            writer.write(SECTION_BOARD);
            writer.newLine();
            for (Vehicle v : board.getVehicles()) {
                writer.write(v.toString()); //
                writer.newLine();
            }

            writer.write(SECTION_HISTORY);
            writer.newLine();
            for (Move m : board.getHistory()) {
                writer.write(m.toString());
                writer.newLine();
            }
        }
    }

    // CLASSE INTERNE

    /**
     * Classe de données pour encapsuler les résultats du chargement d'un niveau
     * de jeu, incluant le plateau de jeu, le nombre de mouvements effectués et
     * le temps écoulé.
     *
     * <pre>
     * Constructeur :
     *      Entrée :
     *          board : Le plateau de jeu chargé
     *          moves : Le nombre de mouvements effectués
     *          time : Le temps écoulé en secondes
     * </pre>
     */
    public record LoadResult(Board board, int moves, int time) {
    }
}
