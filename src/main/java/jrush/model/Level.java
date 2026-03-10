package jrush.model;

import jrush.util.Position;
import util.Contract;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface Level {

    static List<Vehicle> LoadFile(String path) {
        Contract.checkCondition(path != null);
        System.out.println(new java.io.File(path).getAbsolutePath());
        List<Vehicle> vehicleList = new ArrayList<>();
        try {
            BufferedReader buff = new BufferedReader(new FileReader(path));
            String line;
            while((line = buff.readLine()) != null) {
                String[] vehicleInfo = line.split(";");
                if(vehicleInfo.length != 5) {
                    return null;
                }
                char id = vehicleInfo[0].charAt(0);
                int size = Integer.parseInt(vehicleInfo[1]);
                boolean isHorizontal = Boolean.parseBoolean(vehicleInfo[2]);
                int x = Integer.parseInt(vehicleInfo[3]);
                int y = Integer.parseInt(vehicleInfo[4]);
                vehicleList.add(new StdVehicle(id,size,isHorizontal,new Position(x,y)));
            }
        } catch (IOException e) {
            System.out.println("error");
        }
        return vehicleList;
    }
}
