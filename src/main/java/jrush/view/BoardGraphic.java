package jrush.view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jrush.model.Board;
import util.Contract;

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
        setPrefSize(CELL_SIZE * GRID_SIZE, CELL_SIZE * GRID_SIZE);
        refresh();
    }

    // REQUÊTES

    // COMMANDES

    public void refresh() {
        getChildren().clear();
        drawGrid();
    }

    // OUTILS

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
    }

}
