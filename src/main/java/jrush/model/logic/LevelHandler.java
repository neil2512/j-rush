package jrush.model.logic;

import jrush.model.Board;
import jrush.model.Vehicle;
import jrush.model.components.StdBoard;
import jrush.model.components.StdVehicle;
import jrush.util.Move;
import jrush.util.Position;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LevelHandler {

    // ATTRIBUTS
    private static final String MAIN_SEPARATOR = "-";
    private static final String INNER_SEPARATOR = ";";
    private static final int NB_FILE_PART = 2;

    // CONSTRUCTEURS

    private LevelHandler() {
    }

    // REQUÊTES

    public static Board loadBoard(String filename) throws IllegalArgumentException {
        Board board = new StdBoard();
        List<List<String>> sections = new ArrayList<>();
        List<String> currentSection = new ArrayList<>();
        //Lecture de tout le fichier
        try(BufferedReader buff = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = buff.readLine()) != null) {
                if(line.equals(MAIN_SEPARATOR)) {
                    sections.add(currentSection);
                    currentSection = new ArrayList<>();
                    continue;
                }
                currentSection.add(line);
            }
            sections.add(currentSection);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error on reading file : " + e.getMessage());
        }
        //Vérification du nombre de parties
        if(sections.size() > NB_FILE_PART) {
            throw  new IllegalArgumentException("Too much file parts");
        }

        //Première partie : liste de vehicules
        for(String vehicleLine : sections.getFirst()) {
            String[] vehicleInfo = vehicleLine.split(INNER_SEPARATOR);
            if(vehicleInfo.length != 4) {
                throw new IllegalArgumentException("Error on line " + vehicleLine);
            }
            try {
                String id = vehicleInfo[0];
                boolean isHorizontal = Boolean.parseBoolean(vehicleInfo[1]);
                int x = Integer.parseInt(vehicleInfo[2]);
                int y = Integer.parseInt(vehicleInfo[3]);
                board.addVehicle(new StdVehicle(jrush.model.components.VehicleType.fromId(id),isHorizontal,new Position(x,y)));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Error on string : " + vehicleLine + " " + e.getMessage());
            }
        }

        //Seconde partie : liste des moves
        List<Move> history = new ArrayList<Move>();
        if(sections.size() == 2) {
            for (String moveLine : sections.get(1)) {
                String[] moveInfo = moveLine.split(INNER_SEPARATOR);
                if (moveInfo.length != 5) {
                    throw new IllegalArgumentException("Error on line " + moveLine);
                }
                try {
                    String id = moveInfo[0];
                    boolean isHorizontal = Boolean.parseBoolean(moveInfo[1]);
                    int x = Integer.parseInt(moveInfo[2]);
                    int y = Integer.parseInt(moveInfo[3]);
                    int delta = Integer.parseInt(moveInfo[4]);
                    history.add(
                            new Move(
                                    new StdVehicle(jrush.model.components.VehicleType.fromId(id), isHorizontal,
                                            new Position(x, y)),
                                    delta));

                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Error on string : " + moveLine + " " + e.getMessage());
                }
            }
        }
        board.setHistory(history);

        return board;
    }

    public static void saveBoard(Board board, String fileName) {
        // Forcer l'extension .txt
        if (!fileName.endsWith(".txt")) {
            fileName = fileName + ".txt";
        }
        try( BufferedWriter buff = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8))){

            List<Vehicle> vehicleList = board.getVehicles();
            for(Vehicle v: vehicleList){
                buff.write(v.toString());
                buff.newLine();
            }
            buff.write(MAIN_SEPARATOR);
            buff.newLine();
            for(Move move : board.getHistory()) {
                buff.write(move.toString());
                buff.newLine();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error on saving files",e);
        }
    }

    // COMMANDES

    // OUTILS
}
