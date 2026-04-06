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
 * – Chaque ligne représentant un véhicule contient les informations suivantes :
 *      type;horizontal;posX;posY
 * – Une ligne contenant uniquement le séparateur principal ("-") indique le
 *   début de l'historique des mouvements.
 * – Chaque ligne représentant un mouvement dans l'historique contient les
 *   informations suivantes :
 *      type;horizontal;posX;posY;delta
 * </pre>
 *
 */
public class LevelHandler {

    // ATTRIBUTS
    private static final String MAIN_SEPARATOR = "-";
    private static final String INNER_SEPARATOR = ";";
    private static final int NB_FILE_PART = 2;

    // CONSTRUCTEURS

    private LevelHandler() {
    }

    // COMMANDES

    /**
     * Tente de charger un niveau de jeu à partir d'un fichier texte. Le fichier
     * doit être formaté selon les spécifications décrites dans la documentation
     * de la classe.
     *
     * @param filename Le nom du fichier à charger
     *
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de la
     * lecture du fichier
     */
    public static Board loadBoard(String filename) throws IOException {
        Board board = new StdBoard();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filename))) {

            String line;
            boolean loadingHistory = false;
            List<Move> history = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.startsWith(MAIN_SEPARATOR)) {
                    loadingHistory = true;
                    continue;
                }

                String[] parts = line.split(INNER_SEPARATOR);

                if (!loadingHistory) {
                    if (parts.length == 4) {
                        VehicleType type = VehicleType.fromId(parts[0]);
                        boolean horizontal = Boolean.parseBoolean(parts[1]);
                        Position position =
                                new Position(Integer.parseInt(parts[2]),
                                             Integer.parseInt(parts[3]));

                        Vehicle v = new StdVehicle(type, horizontal, position);
                        board.addVehicle(v);
                    }
                } else {
                    if (parts.length == 5) {
                        Vehicle v =
                                board.findVehicle(VehicleType.fromId(parts[0]));
                        int delta = Integer.parseInt(parts[4]);
                        if (v != null) {
                            history.add(new Move(v, delta));
                        }
                    }
                }
            }

            board.setHistory(history);
            return board;
        }
    }

    /**
     * Sauvegarde un niveau de jeu dans un fichier texte. Le fichier sera
     * formaté selon les spécifications décrites dans la documentation de la
     * classe.
     *
     * @param board Le plateau de jeu à sauvegarder
     * @param filename Le nom du fichier dans lequel sauvegarder le niveau
     *
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de
     * l'écriture
     */
    public static void saveBoard(Board board, String filename)
            throws IOException {
        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filename))) {

            for (Vehicle v : board.getVehicles()) {
                writer.write(v.toString());
                writer.newLine();
            }

            writer.write(MAIN_SEPARATOR);
            writer.newLine();

            for (Move m : board.getHistory()) {
                writer.write(m.toString());
                writer.newLine();
            }
        }
    }
}
