package jrush.app.model.logic;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import jrush.app.model.GameEngine;
import jrush.app.model.Vehicle;
import jrush.app.model.util.Move;
import jrush.app.util.Contract;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;

public class StdGameEngine extends AbstractEngine implements GameEngine {

    // ATTRIBUTS

    private int moveCount;

    private int secondsElapsed;
    private Timeline timer;

    // CONSTRUCTEUR

    public StdGameEngine() {
        super();

        this.moveCount = 0;
        this.secondsElapsed = 0;
        setupTimer();
    }

    // REQUÊTES

    @Override
    public int getMoveCount() {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        return moveCount;
    }

    @Override
    public int getTime() {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        return secondsElapsed;
    }

    @Override
    public boolean checkWinCondition() {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        boolean win = board.checkWinCondition();
        if (win) {
            timer.stop();
        }
        return win;
    }

    @Override
    public boolean canUndoBoardMove() {
        return isLoaded() && !checkWinCondition() && board.canUndo();
    }

    @Override
    public boolean canRedoBoardMove() {
        return isLoaded() && !checkWinCondition() && board.canRedo();
    }

    // COMMANDES

    @Override
    public void loadBoard(String filename) throws IOException {
        Contract.checkCondition(filename != null, "filename == null");
        applyLoadResult(LevelHandler.loadBoard(filename));
    }

    @Override
    public void loadBoard(InputStream inputStream) throws IOException {
        Contract.checkCondition(inputStream != null, "inputStream == null");
        applyLoadResult(LevelHandler.loadBoard(inputStream));
    }

    @Override
    public void saveBoard(String filename) throws IOException {
        Contract.checkCondition(filename != null, "filename == null");
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        LevelHandler.saveBoard(board, moveCount, secondsElapsed, filename);
    }

    @Override
    public void resetBoard() {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        board.reset();
        setMoveCount(0);
        this.secondsElapsed = 0;
        updateTimerValue();
    }

    @Override
    public void moveVehicle(Vehicle vehicle, int delta)
            throws PropertyVetoException {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        Contract.checkCondition(vehicle != null, "vehicle == null");
        vehicle.move(delta);
    }

    @Override
    public void recordBoardMove(Move move) {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        Contract.checkCondition(move != null, "move == null");
        board.record(move);
        setMoveCount(moveCount + 1);
    }

    @Override
    public void undoBoardMove() throws PropertyVetoException {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        Contract.checkCondition(canUndoBoardMove(), "!canUndo()");
        board.undo();
        setMoveCount(moveCount + 1);
    }

    @Override
    public void redoBoardMove() throws PropertyVetoException {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        Contract.checkCondition(canRedoBoardMove(), "!canRedo()");
        board.redo();
        setMoveCount(moveCount + 1);
    }

    @Override
    public void startTimer() {
        Contract.checkCondition(isLoaded(), "!isLoaded()");
        timer.play();
    }

    // OUTILS

    /**
     * Applique les résultats du chargement d'un niveau au moteur de jeu. Cette
     * méthode met à jour le plateau de jeu, le nombre de mouvements effectués
     * et le temps écoulé en fonction des résultats du chargement. Les écouteurs
     * de changements de propriété sont notifiés.
     *
     * <pre>
     * Préconditions :
     *      result != null
     * </pre>
     *
     * @param result Le résultat du chargement d'un niveau, encapsulant le
     * plateau de jeu, le nombre de mouvements effectués et le temps écoulé.
     */
    private void applyLoadResult(LevelHandler.LoadResult result) {
        Contract.checkCondition(result != null, "result == null");

        this.board = result.board();
        this.secondsElapsed = result.time();
        setMoveCount(result.moves());
        updateTimerValue();
    }

    /**
     * Met à jour le nombre de mouvements effectués et notifie les écouteurs de
     * changements de propriété du changement.
     *
     * @param newCount le nouveau nombre de mouvements effectués
     */
    private void setMoveCount(int newCount) {
        int oldCount = moveCount;
        moveCount = newCount;
        pcs.firePropertyChange(PROP_MOVECOUNT, oldCount, newCount);
    }

    /**
     * Configure le timer pour incrémenter le nombre de secondes écoulées toutes
     * les secondes. Les écouteurs de changements de propriété sont notifiés à
     * chaque incrémentation du timer. Le timer est configuré pour s'exécuter
     * indéfiniment jusqu'à ce qu'il soit arrêté manuellement.
     */
    private void setupTimer() {
        Duration sec = Duration.seconds(1);
        timer = new Timeline(new KeyFrame(sec, new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                secondsElapsed++;
                updateTimerValue();
            }
        }));
        this.timer.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Notifie les écouteurs de changements de propriété que le nombre de
     * secondes écoulées a été mis à jour.
     */
    private void updateTimerValue() {
        pcs.firePropertyChange(PROP_TIMER, null, secondsElapsed);
    }
}
