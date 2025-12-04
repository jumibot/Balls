package model.physics;


public interface PhysicsEngine {

    public abstract PhysicsValues calcNewPhysicsValues();


    public abstract PhysicsValues getPhysicsValues();


    public abstract void reboundInEast(
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues,
            double worldDim_x, double worldDim_y);


    public abstract void reboundInWest(
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues,
            double worldDim_x, double worldDim_y);


    public abstract void reboundInNorth(
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues,
            double worldDim_x, double worldDim_y);


    public abstract void reboundInSouth(
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues,
            double worldDim_x, double worldDim_y);


    public void setAngularAcceleration(double angularAcceleration);


    public abstract void setPhysicsValues(PhysicsValues phyValues);


    public void setThrust(double thrust);
}
