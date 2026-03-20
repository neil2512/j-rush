package jrush.model;

import jrush.util.Position;
import util.Contract;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LevelHandler {

    // ATTRIBUTS

    // CONSTRUCTEURS

    private LevelHandler() {
    }

    // REQUÊTES

    public static Board loadBoard(String filename) {
        Contract.checkCondition(filename != null);
        Board board = new StdBoard();

        try {
            BufferedReader buff = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = buff.readLine()) != null) {
                String[] vehicleInfo = line.split(";");
                if (vehicleInfo.length != 4) {
                    return null;
                }
                String id = vehicleInfo[0];
                boolean isHorizontal = Boolean.parseBoolean(vehicleInfo[1]);
                int x = Integer.parseInt(vehicleInfo[2]);
                int y = Integer.parseInt(vehicleInfo[3]);
                try {
                    board.addVehicle(
                            new StdVehicle(VehicleType.fromId(id), isHorizontal,
                                           new Position(x, y)));
                } catch (IllegalArgumentException e) {
                    System.out.println(e.toString());
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        return board;
    }

    // COMMANDES

    // OUTILS
}
