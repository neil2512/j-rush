package jrush.app.model.logic.solver;

import jrush.app.model.logic.StdGameEngine;
import jrush.app.model.util.Move;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.List;


public class solverTest {

    public static void main(String[] args) throws IOException, PropertyVetoException {
        /*
        StdGameEngine game = new StdGameEngine();

        for (int i = 31; i<=40;i++){
            StdGameEngine game_i = new StdGameEngine();
            game_i.loadBoard("src/main/java/jrush/cartes/initialPack/expert/"+i);
            System.out.print("Carte "+i + "\n");
            long start = System.currentTimeMillis();
            List<Move> solution1 = new aStar(game_i).solve();
            long end = System.currentTimeMillis();
            printingSolution(solution1);
            System.out.print("\n");
            System.out.print("Time of execution: "+ ((end-start)/1000) +" seconds " +" or " + ((end-start)/60000)+" minutes \n");
            System.out.print("""
                    ///////////////////////////////


                    """);


        }
        */


          StdGameEngine game = new StdGameEngine();
          game.loadBoard("src/main/resources/jrush/app/levels/level1.jrush");
          System.out.print("BFS \n ");
          long start = System.currentTimeMillis();
          List<Move> solution1 = new BreadthFirstSolver(game.getBoard()).solve();
          long end = System.currentTimeMillis();
          printingSolution(solution1);
          System.out.print("\n");
          System.out.print("Time of execution: "+((end-start)/1000) +" seconds " +" or " + ((end-start)/60000)+" minutes \n");

          System.out.print("\n");
          System.out.print("\n");
          System.out.print("aStar \n");
          long start2 = System.currentTimeMillis();
          List<Move> solution2 = new AStarSolver(game.getBoard()).solve();
          long end2 = System.currentTimeMillis();
          printingSolution(solution2);
          System.out.print("Time of execution: "+((end2-start2)/1000) +" seconds " +" or " + ((end2-start2)/60000)+" minutes \n");



    }

    // OUTILS
    public static void printingSolution (List<Move> solution){
        System.out.print("Solution est: ");
        for (Move m : solution){
            System.out.print(m.display());
        }
        System.out.print("   ");
        System.out.print("\n");
        System.out.print("NB : ");
        System.out.print(solution.size());
        System.out.print("\n");
    }
}
