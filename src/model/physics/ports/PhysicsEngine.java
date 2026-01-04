package model.physics.ports;

public interface PhysicsEngine {

    public abstract PhysicsValuesDTO calcNewPhysicsValues();


    public abstract PhysicsValuesDTO getPhysicsValues();


    public abstract void reboundInEast(
            PhysicsValuesDTO newPhyValues, PhysicsValuesDTO oldPhyValues,
            double worldDim_x, double worldDim_y);


    public abstract void reboundInWest(
            PhysicsValuesDTO newPhyValues, PhysicsValuesDTO oldPhyValues,
            double worldDim_x, double worldDim_y);


    public abstract void reboundInNorth(
            PhysicsValuesDTO newPhyValues, PhysicsValuesDTO oldPhyValues,
            double worldDim_x, double worldDim_y);


    public abstract void reboundInSouth(
            PhysicsValuesDTO newPhyValues, PhysicsValuesDTO oldPhyValues,
            double worldDim_x, double worldDim_y);


    public void setAngularAcceleration(double angularAcceleration);


    public abstract void setPhysicsValues(PhysicsValuesDTO phyValues);


    public void setThrust(double thrust);
}
