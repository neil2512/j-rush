package jrush.app.view.vehicle;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jrush.app.model.components.VehicleType;
import jrush.app.util.Position;
import jrush.app.view.board.BoardGraphic;
import util.Contract;



/**
 * Classe qui représente graphiquement un véhicule fantôme sur le plateau de jeu
 * et qui permet de visualiser la position et l'orientation d'un véhicule lors
 * de sa création ou de sa modification en mode édition.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          VehicleType type
 *          BoardGraphic boardGraphic
 *          boolean horizontal
 *      Préconditions :
 *          type != null
 *          boardGraphic != null
 *      Postconditions :
 *          isHorizontal() == horizontal
 * </pre>
 */
public class GhostVehicleGraphic extends Rectangle {

    // CONSTANTES

    private final KeyCode rotationKey = KeyCode.R;

    // ATTRIBUTS

    private final VehicleType type;
    private final BoardGraphic boardGraphic;
    private boolean horizontal;

    // CONSTRUCTEURS

    public GhostVehicleGraphic(
            VehicleType type, BoardGraphic boardGraphic,
            boolean horizontal
    ) {
        Contract.checkCondition(type != null, "type == null");
        Contract.checkCondition(boardGraphic != null, "boardGraphic == null");

        this.type = type;
        this.boardGraphic = boardGraphic;
        this.horizontal = horizontal;

        setDefaultApparence();
        updateDimension();
    }

    // REQUETES

    public boolean isHorizontal() {
        return horizontal;
    }

    public Position getGridPosition() {
        return new Position((int) (getX() / BoardGraphic.CELL_SIZE),
                            (int) (getY() / BoardGraphic.CELL_SIZE));
    }

    // COMMANDES

    public void show() {
        if (!boardGraphic.getChildren().contains(this)) {
            boardGraphic.getChildren().add(this);
        }

        boardGraphic.getScene().setOnKeyPressed(new EventHandler<>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == rotationKey) {
                    rotate();
                }
            }
        });
    }

    public void hide() {
        boardGraphic.getChildren().remove(this);

        if (boardGraphic.getScene() != null) {
            boardGraphic.getScene().setOnKeyPressed(null);
        }
    }

    public void rotate() {
        this.horizontal = !this.horizontal;
        updateDimension();
    }

    public void updatePosition(double sceneX, double sceneY) {
        Point2D local = boardGraphic.sceneToLocal(sceneX, sceneY);
        int gridX = (int) (local.getX() / BoardGraphic.CELL_SIZE);
        int gridY = (int) (local.getY() / BoardGraphic.CELL_SIZE);

        setX(gridX * BoardGraphic.CELL_SIZE + 2);
        setY(gridY * BoardGraphic.CELL_SIZE + 2);
    }

    // OUTILS

    private void setDefaultApparence() {
        setOpacity(0.4);
        setFill(type.getColor());
        setStroke(Color.WHITE);
        getStrokeDashArray().addAll(5.0, 5.0);
        setMouseTransparent(true);
    }

    private void updateDimension() {
        double size = type.getSize() * BoardGraphic.CELL_SIZE - 4;
        double thick = BoardGraphic.CELL_SIZE - 4;
        setWidth(horizontal ? size : thick);
        setHeight(horizontal ? thick : size);
    }
}
