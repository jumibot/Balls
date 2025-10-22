/**
 * Versiones del snapshot
 * Que el snapshot sea una referencia atomica
 */
package Balls.model;


import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import Balls.controller.Controller;
import Balls.dto.VisualBallCatalogDto;
import Balls.dto.VisualBallDto;
import Helpers.Position;
import Helpers.State;


/**
 *
 * @author juanm
 */
public class Model {

    private Map<Integer, Ball> balls = new ConcurrentHashMap<>(4096);
    private Controller controller = null;
    private State state = State.STARTING;
    private final int maxBallsQuantity;
    private final AtomicReference<VisualBallCatalogDto> visualCatalogReference = new AtomicReference();
    private int visualVersion = 0;


    /**
     * CONSTRUCTORS
     */
    public Model(int maxBallsQuantity) {
        this.maxBallsQuantity = maxBallsQuantity;
    }


    /**
     * PUBLIC
     */
    synchronized public boolean addBall(Ball newBall) {
        if (this.maxBallsQuantity >= Ball.getAliveQuantity()) {
            System.out.println("Max balls quantity reached · Model");
            return false; // =======================================>
        }

        this.balls.put(newBall.getId(), newBall);
        newBall.setModel(this);
        newBall.activate();
        this.snapshotVisualBalls();

        return true;
    }


    synchronized public VisualBallCatalogDto getVisualBalls() {
        return this.visualCatalogReference.get();
    }


    public Position getBallPosition() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    public boolean isAlive() {
        return this.state == State.ALIVE;
    }


    synchronized public void removeBall(Ball ball) {

        if (this.balls.remove(ball.getId()) == null) {
            return; // ======= Elmento no esta en la lista ========>
        }
        ball.die();
        this.snapshotVisualBalls();
    }


    public void setController(Controller controller) {
        this.controller = controller;
        this.state = State.ALIVE;
    }


    /**
     * PRIVATES
     */
    private void snapshotVisualBalls() {
        ArrayList<VisualBallDto> visualBalls = new ArrayList(4096);

        this.balls.forEach((key, value) -> {
            visualBalls.add(value.getVisual());
        });

        this.visualVersion++;
        VisualBallCatalogDto catalog = new VisualBallCatalogDto(this.visualVersion, visualBalls);
        this.visualCatalogReference.set(catalog); // Thread safe assignement
    }


    /**
     * STATIC
     */
    public static long getAlivedBallsQuantity() {
        return Ball.getAliveQuantity();
    }


    public static long getCreatedBallsQuantity() {
        return Ball.getCreatedQuantity();
    }


    public static long getDeadBallsQuantity() {
        return Ball.getDeadQuantity();
    }

}
