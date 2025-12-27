package model.bodies;

import model.physics.NullPhysicsEngine;

public class DecoBody extends AbstractBody {

    /**
     * CONSTRUCTORS
     */
    public DecoBody(double size, double posX, double posY, double angle) {
        super(new NullPhysicsEngine(size, posX, posY, angle));
    }

    /**
     * PUBLICS
     */
    @Override
    public synchronized void activate() {
        super.activate();
        this.setState(BodyState.ALIVE);
    }

}
