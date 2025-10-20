/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Balls.model;


import Balls.dto.VisualBallDto;
import Helpers.State;
import Helpers.DoubleVector;
import Helpers.Position;
import java.awt.image.BufferedImage;


/**
 *
 * @author juanm
 */
public class Ball implements Runnable {

    /* TO-DO: Replace indivdual counters by one array of counter */
    private static int aliveQuantity = 0;
    private static int createdQuantity = 0;
    private static int deadQuantity = 0;

    private int id;
    private final VisualBallDto visualBall;
    private DoubleVector coordinates;
    private Thread thread;
    private Model model;
    private State state;


    /**
     * CONSTRUCTORS
     */
    public Ball(int imageId, int maxSizeInPx, DoubleVector coordinates) {
        this.model = null;
        this.id = Ball.incCreatedQuantity();
        this.coordinates = coordinates;

        this.visualBall = new VisualBallDto(this.id, imageId, maxSizeInPx);

        this.thread = new Thread(this);
        this.thread.setName("Ball Thread · " + Ball.createdQuantity);

    }


    /**
     * PUBLICS
     */
    public int getId() {
        return this.id;
    }


    /**
     * PROTECTED
     */
    protected boolean activate() {
        if (!this.model.isAlive()) {
            System.err.println("ERROR Model is not alive! · (Ball)");
            return false;
        }

        if (this.state != State.STARTING) {
            System.err.println("ERROR Ball is not starting! · (Ball)");
            return false;
        }

        this.setState(State.ALIVE);
        this.thread.start();
        Ball.aliveQuantity++;
        return true;
    }


    protected void die() {
        if (this.state == State.ALIVE) {
            this.state = State.DEAD;
            Ball.deadQuantity++;
            Ball.aliveQuantity--;
        }
    }


    protected State getState() {
        return this.state;
    }


    protected VisualBallDto getVisualBall() {
        return this.visualBall;
    }


    protected void setModel(Model model) {
        this.model = model;
    }


    protected void setState(State state) {
        this.state = state;
    }


    @Override
    public void run() {
        Position newPos = new Position();

        while (this.getState() != State.DEAD) {

            if (this.getState() == State.ALIVE) {
                // Try to move

            }

            try {
                /* Descansar */
                Thread.sleep(40);
            } catch (InterruptedException ex) {
                System.err.println("ERROR Sleeping in ball thread! (Ball) · " + ex.getMessage());
            }
        }
    }


    /**
     * STATICS
     */
    static protected long getCreatedQuantity() {
        return Ball.createdQuantity;
    }


    static protected long getAliveQuantity() {
        return Ball.aliveQuantity;
    }


    static protected long getDeadQuantity() {
        return Ball.deadQuantity;
    }


    static synchronized protected int incCreatedQuantity() {
        Ball.createdQuantity++;

        return Ball.createdQuantity;
    }
}
