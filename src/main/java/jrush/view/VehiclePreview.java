package jrush.view;

import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import jrush.model.Board;
import jrush.model.BuildEngine;
import jrush.model.Vehicle;
import jrush.model.components.StdVehicle;
import jrush.model.components.VehicleType;
import jrush.util.Position;
import util.Contract;
import javafx.scene.paint.Color;

public class VehiclePreview extends VBox {

    // CONSTANCE

    public static final int PREVIEW_CELL_SIZE = 60;

    // ATTRIBUTS

    private final BuildEngine engine;
    private final VehicleType type;
    private final BoardGraphic boardGraphic;

    private Rectangle ghost;
    private boolean ghostHorizontal ;

    // CONSTRUCTEUR

    public VehiclePreview(BuildEngine engine, VehicleType type,
                          BoardGraphic boardGraphic) {
        Contract.checkCondition(engine != null, "engine == null");
        Contract.checkCondition(type != null, "type == null");
        Contract.checkCondition(boardGraphic != null, "boardGraphic == null");

        this.engine = engine;
        this.type = type;
        this.boardGraphic = boardGraphic;
        this.ghost = null;
        this.ghostHorizontal = true;

        paint();
        connectControllers();
    }

    // REQUÊTE

    // MÉTHODES

    // OUTILS

    private void paint() {
        Rectangle rect = new Rectangle();
        rect.setWidth(PREVIEW_CELL_SIZE - 4);
        rect.setHeight(type.getSize() * PREVIEW_CELL_SIZE - 4);
        rect.setFill(type.getColor());
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(1);

        this.getChildren().add(rect);
    }

    private void connectControllers() {

        this.setOnMousePressed(event -> {
            ghost = null;
            event.consume();
        });

        this.setOnMouseDragged(dragEvent -> {
            if (ghost == null) {
                ghost = new Rectangle();
                updateGhost();
                boardGraphic.getChildren().add(ghost);
            }

            this.getScene().setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.R) {
                    ghostHorizontal = !ghostHorizontal;
                    updateGhost();
                }
            });

            javafx.geometry.Point2D local = boardGraphic.sceneToLocal(
                    dragEvent.getSceneX(), dragEvent.getSceneY()
            );
            int gridX = (int) (local.getX() / BoardGraphic.CELL_SIZE);
            int gridY = (int) (local.getY() / BoardGraphic.CELL_SIZE);

            ghost.setX(gridX * BoardGraphic.CELL_SIZE + 2);
            ghost.setY(gridY * BoardGraphic.CELL_SIZE + 2);
        });

        this.setOnMouseReleased(releaseEvent -> {
            if (ghost != null) {
                // Retirer le fantôme
                boardGraphic.getChildren().remove(ghost);
                ghost = null;

                if (boardGraphic.getScene() != null) {
                    boardGraphic.getScene().setOnKeyPressed(null);
                }

                // Convertir les coordonnées
                javafx.geometry.Point2D local = boardGraphic.sceneToLocal(
                        releaseEvent.getSceneX(), releaseEvent.getSceneY()
                );
                int x = (int) (local.getX() / BoardGraphic.CELL_SIZE);
                int y = (int) (local.getY() / BoardGraphic.CELL_SIZE);

                // Vérifier qu'on est bien sur le board
                if (x < 0 || y < 0 || x >= Board.GRID_SIZE || y >= Board.GRID_SIZE) {
                    return;
                }

                if (!engine.isLoaded()) return;

                StdVehicle vehicle = new StdVehicle(type, ghostHorizontal, new Position(x, y));
                try {
                    engine.addVehicleOnBoard(vehicle);
                    VehicleGraphicEditor vge = new VehicleGraphicEditor(
                            engine, vehicle, boardGraphic
                    );
                    boardGraphic.getChildren().add(vge);
                    vge.setSelected(true);
                } catch (IllegalArgumentException e) {
                    // Position invalide
                }
            }
        });

        // Écoute les suppressions
        engine.addPropertyChangeListener(event -> {
            if (event.getPropertyName().equals(BuildEngine.PROP_BOARD)) {
                Object oldValue = event.getOldValue();
                Object newValue = event.getNewValue();

                // Véhicule supprimé, réafficher la preview
                if (oldValue instanceof Vehicle removed
                        && removed.getId().equals(type.getId())) {
                    this.setVisible(true);
                    this.setManaged(true);
                }

                // Nouveau board (newBoard/loadBoard) :  afficher selon contenu
                if (newValue instanceof Board loadedBoard) {
                    boolean isOnBoard = loadedBoard.getVehicles().stream()
                            .anyMatch(v -> v.getId().equals(type.getId()));
                    this.setVisible(!isOnBoard);
                    this.setManaged(!isOnBoard);
                }

                // Véhicule ajouté : cacher la preview
                if (newValue instanceof Vehicle added
                        && added.getId().equals(type.getId())) {
                    this.setVisible(false);
                    this.setManaged(false);
                }
            }
        });
    }

    private void updateGhost() {
        if (ghostHorizontal) {
            ghost.setWidth(type.getSize() * BoardGraphic.CELL_SIZE - 4);
            ghost.setHeight(BoardGraphic.CELL_SIZE - 4);
        } else {
            ghost.setWidth(BoardGraphic.CELL_SIZE - 4);
            ghost.setHeight(type.getSize() * BoardGraphic.CELL_SIZE - 4);
        }
        ghost.setFill(type.getColor());
        ghost.setOpacity(0.5);
        ghost.setStroke(Color.BLACK);
        ghost.setStrokeWidth(1);
    }
}
