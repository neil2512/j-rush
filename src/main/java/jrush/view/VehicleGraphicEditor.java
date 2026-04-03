package jrush.view;


import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jrush.model.BuildEngine;
import jrush.model.Vehicle;
import jrush.util.Position;
import util.Contract;
import java.beans.PropertyVetoException;

public class VehicleGraphicEditor extends Rectangle{

    // ATTRIBUTS

    private static VehicleGraphicEditor currentSelected = null;

    private final BoardGraphic boardGraphic;
    private final BuildEngine engine;
    private final Vehicle vehicle;

    private Rectangle ghost;
    private boolean ghostHorizontal;

    private double anchorX;
    private double anchorY;
    private int gridAnchorX;
    private int gridAnchorY;
    private boolean selected = false;

    // CONSTRUCTEUR

    public  VehicleGraphicEditor(BuildEngine engine, Vehicle vehicle, BoardGraphic boardGraphic) {
        Contract.checkCondition(engine != null, "engine == null");
        Contract.checkCondition(vehicle != null, "vehicle == null");
        this.engine = engine;
        this.vehicle = vehicle;
        this.boardGraphic = boardGraphic;
        paint();
        connectControllers();
    }

    // REQUÊTES

    // COMMANDES

    // OUTILS

    /**
     * Configure l'apparence du rectangle en fonction des propriétés du véhicule
     * et qui positionne le rectangle à la bonne place sur le plateau de jeu.
     */
    private void paint() {
        if (vehicle.isHorizontal()) {
            setWidth(vehicle.getSize() * BoardGraphic.CELL_SIZE - 4);
            setHeight(BoardGraphic.CELL_SIZE - 4);
        } else {
            setWidth(BoardGraphic.CELL_SIZE - 4);
            setHeight(vehicle.getSize() * BoardGraphic.CELL_SIZE - 4);
        }
        setFill(vehicle.getColor());
        updatePosition();
    }


    private void updatePosition() {
        setX(vehicle.getPosition().getX() * BoardGraphic.CELL_SIZE + 2);
        setY(vehicle.getPosition().getY() * BoardGraphic.CELL_SIZE + 2);
    }

    private void connectControllers() {
        vehicle.addPropertyChangeListener(event -> {
            if (event.getPropertyName().equals(Vehicle.PROP_POSITION)) {
                updatePosition();
            }
            if (event.getPropertyName().equals(Vehicle.PROP_HORIZONTAL)) {
                paint();
            }
        });

        this.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            gridAnchorX = vehicle.getPosition().getX();
            gridAnchorY = vehicle.getPosition().getY();
            ghostHorizontal = vehicle.isHorizontal();
        });

        //
        this.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // Vérifier que le véhicule est encore sur le board
                if (engine.getBoard().getVehicles().stream()
                        .noneMatch(v -> v.getId().equals(vehicle.getId()))) {
                    return;
                }

                if (ghost == null) {
                    if (currentSelected != null) {
                        currentSelected.setSelected(false);
                    }
                    setSelected(false);
                    ghost = new Rectangle();
                    updateGhost();
                    boardGraphic.getChildren().add(ghost);

                    // Écouter R sur la scène
                    boardGraphic.getScene().setOnKeyPressed(keyEvent -> {
                        if (keyEvent.getCode() == KeyCode.R && ghost != null) {
                            ghostHorizontal = !ghostHorizontal;
                            updateGhost();
                        }
                    });
                }

                javafx.geometry.Point2D local = boardGraphic.sceneToLocal(
                        event.getSceneX(), event.getSceneY()
                );
                int gridX = (int) (local.getX() / BoardGraphic.CELL_SIZE);
                int gridY = (int) (local.getY() / BoardGraphic.CELL_SIZE);

                ghost.setX(gridX * BoardGraphic.CELL_SIZE + 2);
                ghost.setY(gridY * BoardGraphic.CELL_SIZE + 2);
            }
        });


        // Sélectionner et désélectionner
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                setSelected(!selected);
            }
            if (event.getButton() == MouseButton.SECONDARY) {
                engine.removeVehicleFromBoard(vehicle);
                boardGraphic.getChildren().remove(VehicleGraphicEditor.this);
            }
        });

        //
        this.setOnMouseReleased(event -> {
            if (ghost != null) {
                boardGraphic.getChildren().remove(ghost);
                ghost = null;

                // Retirer le listener clavier
                boardGraphic.getScene().setOnKeyPressed(null);

                javafx.geometry.Point2D local = boardGraphic.sceneToLocal(
                        event.getSceneX(), event.getSceneY()
                );
                int x = (int) (local.getX() / BoardGraphic.CELL_SIZE);
                int y = (int) (local.getY() / BoardGraphic.CELL_SIZE);

                try {
                    engine.rotateAndMove(vehicle, new Position(x, y), ghostHorizontal);
                    setSelected(true);
                } catch (PropertyVetoException e) {
                    // Position invalide, on ne fait rien
                }

                ghost = null;
            }
        });
    }


    // Sélectionne et désélectionne la voiture en s'occupant de colorier ou non le contour
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            if (currentSelected != null && currentSelected != this) {
                currentSelected.setSelected(false);
            }
            currentSelected = this;
            this.setStroke(Color.YELLOW);
            this.setStrokeWidth(3);
            this.requestFocus();
            // Écouter R pour rotation
            boardGraphic.getScene().setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.R) {
                    try {
                        vehicle.rotate();
                    } catch (PropertyVetoException ignored) {}
                }
            });
        } else {
            if (currentSelected == this) {
                currentSelected = null;
            }
            this.setStroke(null);
            this.setStrokeWidth(0);
            // Retirer le listener clavier
            if (boardGraphic.getScene() != null) {
                boardGraphic.getScene().setOnKeyPressed(null);
            }
        }
    }


    //Fonction qui s'occupe de dessiner le véhicule fantôme
    private void updateGhost() {
        if (ghostHorizontal) {
            ghost.setWidth(vehicle.getSize() * BoardGraphic.CELL_SIZE - 4);
            ghost.setHeight(BoardGraphic.CELL_SIZE - 4);
        } else {
            ghost.setWidth(BoardGraphic.CELL_SIZE - 4);
            ghost.setHeight(vehicle.getSize() * BoardGraphic.CELL_SIZE - 4);
        }
        ghost.setFill(vehicle.getColor());
        ghost.setOpacity(0.5);
        ghost.setStroke(Color.BLACK);
        ghost.setStrokeWidth(1);
        }

}
