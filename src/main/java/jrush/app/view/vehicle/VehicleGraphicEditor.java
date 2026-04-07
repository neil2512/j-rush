package jrush.app.view.vehicle;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import jrush.app.model.BuildEngine;
import jrush.app.model.Vehicle;
import jrush.app.model.components.VehicleType;
import jrush.app.view.board.BoardGraphic;
import jrush.app.util.Contract;

import java.beans.PropertyVetoException;

/**
 * Classe qui représente graphiquement un véhicule sur le plateau de jeu et qui
 * permet de le déplacer et de le faire pivoter en mode édition.
 *
 * <pre>
 * Constructeur :
 *      Entrée :
 *          BuildEngine engine
 *          BoardGraphic boardGraphic
 *          Vehicle vehicle
 *      Préconditions :
 *          engine != null
 *          boardGraphic != null
 *          vehicle != null
 * </pre>
 */
public class VehicleGraphicEditor extends VehicleGraphic {

    // ATTRIBUTS

    private final BuildEngine engine;
    private final BoardGraphic boardGraphic;

    private GhostVehicleGraphic ghostGraphic;

    // CONSTRUCTEURS

    public VehicleGraphicEditor(
            BuildEngine engine, BoardGraphic boardGraphic, Vehicle vehicle) {
        super(vehicle);
        Contract.checkCondition(engine != null, "engine == null");
        Contract.checkCondition(boardGraphic != null, "boardGraphic == null");
        this.engine = engine;
        this.boardGraphic = boardGraphic;

        connectBaseControllers();
        connectControllers();
    }

    // OUTILS

    /**
     * Connecte les contrôleurs de l'élément graphique.
     */
    private void connectControllers() {
        this.setOnMouseClicked(new EventHandler<>() {
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    engine.removeVehicleFromBoard(vehicle);
                }
            }
        });

        this.setOnMouseDragged(new EventHandler<>() {
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (ghostGraphic == null) {
                        ghostGraphic =
                                new GhostVehicleGraphic(
                                        VehicleType.fromId(vehicle.getId()),
                                        boardGraphic,
                                        vehicle.isHorizontal());
                        ghostGraphic.show();
                    }
                    ghostGraphic.updatePosition(event.getSceneX(),
                                                event.getSceneY());
                }
            }
        });

        this.setOnMouseReleased(new EventHandler<>() {
            public void handle(MouseEvent event) {
                if (ghostGraphic != null) {
                    ghostGraphic.hide();

                    try {
                        engine.rotateAndMove(vehicle,
                                             ghostGraphic.getGridPosition(),
                                             ghostGraphic.isHorizontal());
                    } catch (PropertyVetoException ignored) {
                    }

                    ghostGraphic = null;
                }
            }
        });
    }
}
