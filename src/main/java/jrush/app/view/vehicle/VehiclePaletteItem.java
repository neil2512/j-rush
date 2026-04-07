package jrush.app.view.vehicle;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jrush.app.model.Board;
import jrush.app.model.BuildEngine;
import jrush.app.model.Vehicle;
import jrush.app.model.components.StdVehicle;
import jrush.app.model.components.VehicleType;
import jrush.app.util.Position;
import jrush.app.view.board.BoardGraphic;
import jrush.app.util.Contract;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Classe qui représente graphiquement un élément de la palette de véhicules en
 * mode édition. Permet de créer un véhicule du type représenté par l'élément et
 * de le placer sur le plateau de jeu.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          – BuildEngine engine
 *          – VehicleType type
 *          – BoardGraphic boardGraphic
 *      Préconditions :
 *          – engine != null
 *          – type != null
 *          – boardGraphic != null
 * </pre>
 */
public class VehiclePaletteItem extends VBox {

    // CONSTANTES

    public static final int PREVIEW_CELL_SIZE = 40;

    // ATTRIBUTS

    private final BuildEngine engine;
    private final VehicleType type;
    private final BoardGraphic boardGraphic;

    private GhostVehicleGraphic ghostGraphic;

    // CONSTRUCTEURS

    public VehiclePaletteItem(
            BuildEngine engine, VehicleType type,
            BoardGraphic boardGraphic
    ) {
        Contract.checkCondition(engine != null, "engine == null");
        Contract.checkCondition(type != null, "type == null");
        Contract.checkCondition(boardGraphic != null, "boardGraphic == null");

        this.engine = engine;
        this.type = type;
        this.boardGraphic = boardGraphic;

        drawIcon();
        connectControllers();
        setupAutoVisibility();
    }

    // OUTILS

    private void drawIcon() {
        double margin = 3.0;
        double width = type.getSize() * PREVIEW_CELL_SIZE - (2 * margin);
        double height = PREVIEW_CELL_SIZE - (2 * margin);

        Rectangle rect = new Rectangle(width, height);
        rect.setFill(type.getColor());

        rect.setArcWidth(8);
        rect.setArcHeight(8);

        rect.setStroke(Color.rgb(0, 0, 0, 0.2));
        rect.setStrokeWidth(1.5);

        this.getChildren().add(rect);

        this.setStyle(
                "-fx-padding: 5; -fx-alignment: center; -fx-cursor: hand;");
    }

    private void connectControllers() {

        this.setOnMouseDragged(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                if (ghostGraphic == null) {
                    ghostGraphic =
                            new GhostVehicleGraphic(type, boardGraphic, true);
                    ghostGraphic.show();
                }
                ghostGraphic.updatePosition(event.getSceneX(),
                                            event.getSceneY());
            }
        });

        this.setOnMouseReleased(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                if (ghostGraphic != null) {
                    boolean horizontal = ghostGraphic.isHorizontal();
                    Position position = ghostGraphic.getGridPosition();

                    ghostGraphic.hide();
                    ghostGraphic = null;
                    try {
                        Vehicle newV =
                                new StdVehicle(type, horizontal, position);
                        engine.addVehicleOnBoard(newV);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        });
    }

    private void setupAutoVisibility() {
        engine.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getPropertyName().equals(BuildEngine.PROP_BOARD)) {
                    Board board = (Board) event.getNewValue();
                    if (board != null) {
                        boolean alreadyOnBoard = false;
                        for (Vehicle v : board.getVehicles()) {
                            if (v.getId().equals(type.getId())) {
                                alreadyOnBoard = true;
                                break;
                            }
                        }
                        setVisible(!alreadyOnBoard);
                        setManaged(!alreadyOnBoard);
                    }
                }
            }
        });
    }

}
