package engine.model.physics.implementations;

import engine.model.physics.core.AbstractPhysicsEngine;
import engine.model.physics.ports.PhysicsValuesMDTO;

public class NullPhysicsEngine extends AbstractPhysicsEngine {

    // region Constructors
    public NullPhysicsEngine(PhysicsValuesMDTO dto1, PhysicsValuesMDTO dto2, PhysicsValuesMDTO dto3) {
        super(dto1, dto2, dto3);
    }
    // endregion

    // *** PUBLIC ***

    @Override
    public void angularAccelerationInc(double angularAcc) {
    }

    @Override
    public PhysicsValuesMDTO calcNewPhysicsValues() {
        return this.getPhysicsValues();
    }

    @Override
    public boolean isThrusting() {
        return false;
    }

    @Override
    public void reboundInEast(PhysicsValuesMDTO phyValues, double worldDim_x, double worldDim_y) {
    }

    @Override
    public void reboundInWest(PhysicsValuesMDTO phyValues, double worldDim_x, double worldDim_y) {
    }

    @Override
    public void reboundInNorth(PhysicsValuesMDTO phyValues, double worldDim_x, double worldDim_y) {
    }

    @Override
    public void reboundInSouth(PhysicsValuesMDTO phyValues, double worldDim_x, double worldDim_y) {
    }

    @Override
    public void setAngularSpeed(double angularSpeed) {
    }
}
