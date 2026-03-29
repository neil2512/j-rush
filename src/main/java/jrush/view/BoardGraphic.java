package jrush.view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import jrush.model.Board;

import static jrush.model.Board.GRID_SIZE;

/**
 * Classe qui représente graphiquement le plateau de jeu.
 */
public class BoardGraphic extends Pane {

    // CONSTANTES

    public static final int CELL_SIZE = 80;

    // ATTRIBUTS

    // CONSTRUCTEURS

    public BoardGraphic() {
        this.setMinSize(CELL_SIZE * GRID_SIZE, CELL_SIZE * GRID_SIZE);
        this.setMaxSize(CELL_SIZE * GRID_SIZE, CELL_SIZE * GRID_SIZE);
        refresh();
    }

    // REQUÊTES

    // COMMANDES

    /**
     * Rafraîchit l'affichage du plateau de jeu en redessinant la grille.
     */
    public void refresh() {
        getChildren().clear();
        drawGrid();
    }

    // OUTILS

    /**
     * Dessine la grille du plateau de jeu en créant des rectangles pour chaque
     * cellule.
     */
    private void drawGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setFill(Color.TRANSPARENT);
                cell.setStroke(Color.web("#2c3e50"));
                cell.setX(i * CELL_SIZE);
                cell.setY(j * CELL_SIZE);
                this.getChildren().add(cell);
            }
        }

        drawExit();
    }

    /**
     * Dessine une ligne verticale pour indiquer la position de la sortie sur le
     * plateau de jeu.
     */
    private void drawExit() {
        int xGrid = Board.EXIT_POSITION.getX();
        int yGrid = Board.EXIT_POSITION.getY();

        double xPos = (xGrid + 1) * CELL_SIZE;
        double yStart = yGrid * CELL_SIZE;
        double yEnd = (yGrid + 1) * CELL_SIZE;

        Line exitLine = new Line(xPos, yStart, xPos, yEnd);

        exitLine.setStroke(Color.BLACK);
        exitLine.setStrokeWidth(4);

        this.getChildren().add(exitLine);
    }

}
