package model.physics;


public class NullPhysicsEngine extends AbstractPhysicsEngine implements PhysicsEngine {

    /**
     * CONSTRUCTORS
     */
    public NullPhysicsEngine(PhysicsValues phyVals) {
        super(phyVals);
    }


    /**
     * PUBLICS
     *
     * @return
     */
    @Override
    public PhysicsValues calcNewPhysicsValues() {
        return this.getPhysicsValues();
    }


    @Override
    public void reboundInEast(PhysicsValues newPhyVals, PhysicsValues oldPhyVals,
            double worldDim_x, double worldDim_y) {
    }


    @Override
    public void reboundInWest(PhysicsValues newPhyVals, PhysicsValues oldPhyVals,
            double worldDim_x, double worldDim_y) {
    }


    @Override
    public void reboundInNorth(PhysicsValues newPhyVals, PhysicsValues oldPhyVals,
            double worldDim_x, double worldDim_y) {
    }


    @Override
    public void reboundInSouth(PhysicsValues newPhyVals, PhysicsValues oldPhyVals,
            double worldDim_x, double worldDim_y) {
    }


    @Override
    public void setAngularAcceleration(double angularAcceleration) {
    }


    @Override
    public void setThrust(double thrust) {
    }

}
