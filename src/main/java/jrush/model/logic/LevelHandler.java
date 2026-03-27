package jrush.model.logic;

import jrush.model.Board;
import jrush.model.components.StdBoard;
import jrush.model.components.StdVehicle;
import jrush.model.components.VehicleType;
import jrush.util.Position;
import util.Contract;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public final class LevelHandler {

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
                    throw new IllegalArgumentException(
                            "Invalid line format: " + line);
                }
                String id = vehicleInfo[0];
                boolean isHorizontal = "1".equals(vehicleInfo[1]);
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
