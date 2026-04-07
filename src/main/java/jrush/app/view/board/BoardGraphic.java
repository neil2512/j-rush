package jrush.app.view.board;

import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import jrush.app.model.Board;

import static jrush.app.model.Board.GRID_SIZE;

/**
 * Classe qui représente graphiquement le plateau de jeu.
 */
public class BoardGraphic extends Pane {

    // CONSTANTES

    public static final int CELL_SIZE = 80;

    // CONSTRUCTEURS

    public BoardGraphic() {
        this.setMinSize(CELL_SIZE * GRID_SIZE, CELL_SIZE * GRID_SIZE);
        this.setMaxSize(CELL_SIZE * GRID_SIZE, CELL_SIZE * GRID_SIZE);

        refresh();
    }

    // COMMANDES

    /**
     * Rafraîchit l'affichage du plateau de jeu en redessinant la grille et la
     * sortie.
     */
    public void refresh() {
        getChildren().clear();
        drawGrid();
        drawExit();
    }

    // OUTILS

    /**
     * Dessine la grille du plateau de jeu en utilisant des lignes et un cadre.
     * Les lignes sont espacées de CELL_SIZE pixels et le cadre entoure toute
     * la grille.
     */
    private void drawGrid() {
        for (int i = 1; i < GRID_SIZE; i++) {
            Line vLine = new Line(i * CELL_SIZE, 0, i * CELL_SIZE,
                                  GRID_SIZE * CELL_SIZE);
            vLine.getStyleClass().add("board-grid-line");
            this.getChildren().add(vLine);

            Line hLine = new Line(0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE,
                                  i * CELL_SIZE);
            hLine.getStyleClass().add("board-grid-line");
            this.getChildren().add(hLine);
        }

        Rectangle frame =
                new Rectangle(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        frame.getStyleClass().add("board-frame");
        this.getChildren().add(frame);
    }

    /**
     * Dessine une flèche pointant vers la sortie du plateau de jeu.
     */
    private void drawExit() {
        int xGrid = Board.EXIT_POSITION.getX();
        int yGrid = Board.EXIT_POSITION.getY();

        double xPos = (xGrid + 1) * CELL_SIZE + 3.0;
        double yCenter = (yGrid + 0.5) * CELL_SIZE;

        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(xPos, yCenter - 12.0,
                                    xPos + 14.0, yCenter,
                                    xPos, yCenter + 12.0);

        triangle.getStyleClass().add("board-exit");
        this.getChildren().add(triangle);
    }
}
