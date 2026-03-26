package jrush.model;

import jrush.util.Position;
import util.Contract;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LevelHandler {

    // ATTRIBUTS

    // CONSTRUCTEURS

    private LevelHandler() {
    }

    // REQUÊTES

    public static Board loadBoard(String filename) throws IllegalArgumentException {
        Contract.checkCondition(filename != null);
        Board board = new StdBoard();
        try(BufferedReader buff = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = buff.readLine()) != null) {
                String[] vehicleInfo = line.split(";");
                if (vehicleInfo.length != 4) {
                    throw new IllegalArgumentException("Error on line : " + line);
                }
                try {
                    String id = vehicleInfo[0];
                    boolean isHorizontal = Boolean.parseBoolean(vehicleInfo[1]);
                    int x = Integer.parseInt(vehicleInfo[2]);
                    int y = Integer.parseInt(vehicleInfo[3]);
                    board.addVehicle(new StdVehicle(VehicleType.fromId(id),isHorizontal,new Position(x,y)));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Error on string : " + line + " " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error on reading file : " + e.getMessage());
        }

        return board;
    }

    public static void saveBoard(Board board, String fileName) {
        try( BufferedWriter buff = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8))){

            List<Vehicle> vehicleList = board.getVehicles();
            for(Vehicle v: vehicleList){
                buff.write(v.toString());
                buff.newLine();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error on saving files",e);
        }
    }

    // COMMANDES

    // OUTILS
}
