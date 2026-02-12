package engine.model.physics.ports;

public interface PhysicsEngine {

        public void angularAccelerationInc(double angularAcc);

        public PhysicsValuesMDTO calcNewPhysicsValues();

        public PhysicsValuesMDTO getPhysicsValues();

        public boolean isThrusting();

        public void reboundInEast(
                        PhysicsValuesMDTO phyValues, double worldDim_x, double worldDim_y);

        public void reboundInWest(
                        PhysicsValuesMDTO phyValues, double worldDim_x, double worldDim_y);

        public void reboundInNorth(
                        PhysicsValuesMDTO phyValues, double worldDim_x, double worldDim_y);

        public void reboundInSouth(
                        PhysicsValuesMDTO phyValues, double worldDim_x, double worldDim_y);

        public void resetAcceleration();

        public void setAngularAcceleration(double angularAcceleration);

        public void setAngularSpeed(double angularSpeed);

        public void setPhysicsValues(PhysicsValuesMDTO phyValues);

        public void setThrust(double thrust);

        public void stopPushing();
}
