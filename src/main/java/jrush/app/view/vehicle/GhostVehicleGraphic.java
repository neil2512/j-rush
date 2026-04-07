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
import jrush.app.util.Contract;


/**
 * Classe qui représente graphiquement un véhicule fantôme sur le plateau de jeu
 * et qui permet de visualiser la position et l'orientation d'un véhicule lors
 * de sa création ou de sa modification en mode édition.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          – VehicleType type
 *          – BoardGraphic boardGraphic
 *          – boolean horizontal
 *      Préconditions :
 *          – type != null
 *          – boardGraphic != null
 *      Postconditions :
 *          – isHorizontal() == horizontal
 * </pre>
 */
public class GhostVehicleGraphic extends Rectangle {

    // CONSTANTES

    private static final double MARGIN = 6.0;
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

        this.setArcWidth(16);
        this.setArcHeight(16);

        setDefaultApparence();
        updateDimension();
    }

    // REQUETES

    /**
     * Indique si le véhicule fantôme est orienté horizontalement ou
     * verticalement.
     *
     * @return true si le véhicule fantôme est orienté horizontalement, false
     * sinon.
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * Retourne la position du véhicule fantôme sur le plateau de jeu en
     * fonction de sa position graphique.
     *
     * @return la position du véhicule fantôme sur le plateau de jeu.
     */
    public Position getGridPosition() {
        return new Position((int) (getX() / BoardGraphic.CELL_SIZE),
                            (int) (getY() / BoardGraphic.CELL_SIZE));
    }

    // COMMANDES

    /**
     * Affiche le véhicule fantôme sur le plateau de jeu et configure les
     * contrôleurs nécessaires pour permettre à l'utilisateur de le faire
     * pivoter en appuyant sur la touche de rotation.
     */
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

    /**
     * Masque le véhicule fantôme du plateau de jeu et désactive les contrôleurs
     * associés à sa manipulation.
     */
    public void hide() {
        boardGraphic.getChildren().remove(this);

        if (boardGraphic.getScene() != null) {
            boardGraphic.getScene().setOnKeyPressed(null);
        }
    }

    /**
     * Fait pivoter le véhicule fantôme en inversant son orientation
     * (horizontale/verticale) et en mettant à jour ses dimensions en
     * conséquence.
     */
    public void rotate() {
        this.horizontal = !this.horizontal;
        updateDimension();
    }

    /**
     * Met à jour la position du véhicule fantôme en fonction des coordonnées de
     * la souris sur le plateau de jeu. La position est ajustée pour que le
     * véhicule fantôme soit aligné sur la grille du plateau de jeu.
     *
     * @param sceneX les coordonnées X de la souris dans les coordonnées de la
     * scène
     * @param sceneY les coordonnées Y de la souris dans les coordonnées de la
     * scène
     */
    public void updatePosition(double sceneX, double sceneY) {
        Point2D local = boardGraphic.sceneToLocal(sceneX, sceneY);
        int gridX = (int) (local.getX() / BoardGraphic.CELL_SIZE);
        int gridY = (int) (local.getY() / BoardGraphic.CELL_SIZE);

        setX(gridX * BoardGraphic.CELL_SIZE + MARGIN);
        setY(gridY * BoardGraphic.CELL_SIZE + MARGIN);
    }

    // OUTILS

    /**
     * Configure l'apparence par défaut du véhicule fantôme, en lui donnant une
     * opacité réduite, une couleur de remplissage correspondant à son type, un
     * contour blanc en pointillés et en le rendant transparent aux événements
     * de la souris pour éviter les interférences avec les contrôleurs de
     * manipulation du véhicule fantôme.
     */
    private void setDefaultApparence() {
        setOpacity(0.4);
        setFill(type.getColor());
        setStroke(Color.WHITE);
        getStrokeDashArray().addAll(5.0, 5.0);
        setMouseTransparent(true);
    }

    /**
     * Met à jour les dimensions du véhicule fantôme en fonction de son
     * orientation (horizontale ou verticale) et de la taille de son type.
     * Les dimensions sont calculées pour que le véhicule fantôme soit aligné
     * sur la grille du plateau de jeu et qu'il respecte les marges définies
     * pour les éléments graphiques des véhicules.
     */
    private void updateDimension() {
        double size = type.getSize() * BoardGraphic.CELL_SIZE - (2 * MARGIN);
        double thick = BoardGraphic.CELL_SIZE - (2 * MARGIN);
        setWidth(horizontal ? size : thick);
        setHeight(horizontal ? thick : size);
    }
}
