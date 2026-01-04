package model.physics.core;

import java.util.concurrent.atomic.AtomicReference;

import model.physics.ports.PhysicsValuesDTO;

public abstract class AbstractPhysicsEngine {

        private final AtomicReference<PhysicsValuesDTO> phyValues; // *+

        /**
         * CONSTRUCTORS
         */
        public AbstractPhysicsEngine(PhysicsValuesDTO phyValues) {
                this.phyValues = new AtomicReference<>(phyValues);
        }

        public AbstractPhysicsEngine(double size, double posX, double posY, double angle) {
                this.phyValues = new AtomicReference<>(
                                new PhysicsValuesDTO(size, posX, posY, angle));
        }

        /**
         * PUBLIC
         */
        public void addAngularAcceleration(double angularAcc) {
                PhysicsValuesDTO old = this.getPhysicsValues();
                this.setPhysicsValues(new PhysicsValuesDTO(
                                old.timeStamp,
                                old.posX, old.posY, old.angle,
                                old.size,
                                old.speedX, old.speedY,
                                old.accX, old.accY,
                                old.angularSpeed,
                                old.angularAcc + angularAcc,
                                old.thrust));
        }

        public PhysicsValuesDTO getPhysicsValues() {
                return this.phyValues.get();
        }

        public void reboundInEast(
                        PhysicsValuesDTO newPhyVals, PhysicsValuesDTO oldPhyVals,
                        double worldDim_x, double worldDim_y) {

                // New speed: horizontal component flipped, vertical preserved
                double speedX = -newPhyVals.speedX;
                double speedY = newPhyVals.speedY;

                // New position: snapped to the east boundary (slightly inside)
                double posX = 0.0001;
                double posY = newPhyVals.posY;
                double angle = newPhyVals.angle;

                // Acceleration is preserved
                double accX = newPhyVals.accX;
                double accY = newPhyVals.accY;

                PhysicsValuesDTO reboundPhyVals = new PhysicsValuesDTO(
                                newPhyVals.timeStamp,
                                posX, posY, angle,
                                newPhyVals.size,
                                speedX, speedY,
                                accX, accY,
                                oldPhyVals.angularSpeed, oldPhyVals.angularSpeed,
                                oldPhyVals.thrust);

                this.setPhysicsValues(reboundPhyVals);
        }

        public void reboundInWest(PhysicsValuesDTO newPhyVals, PhysicsValuesDTO oldPhyVals,
                        double worldDim_x, double worldDim_y) {

                // New speed: horizontal component flipped, vertical preserved
                double speedX = -newPhyVals.speedX;
                double speedY = newPhyVals.speedY;

                // New position: snapped to the east boundary (slightly inside)
                double posX = worldDim_x - 0.0001;
                double posY = newPhyVals.posY;
                double angle = newPhyVals.angle;

                // Acceleration is preserved
                double accX = newPhyVals.accX;
                double accY = newPhyVals.accY;

                PhysicsValuesDTO reboundPhyVals = new PhysicsValuesDTO(
                                newPhyVals.timeStamp,
                                posX, posY, angle,
                                newPhyVals.size,
                                speedX, speedY,
                                accX, accY,
                                oldPhyVals.angularSpeed, oldPhyVals.angularSpeed,
                                oldPhyVals.thrust);

                this.setPhysicsValues(reboundPhyVals);
        }

        public void reboundInNorth(PhysicsValuesDTO newPhyVals, PhysicsValuesDTO oldPhyVals,
                        double worldDim_x, double worldDim_y) {

                // New speed: horizontal component flipped, vertical preserved
                double speedX = newPhyVals.speedX;
                double speedY = -newPhyVals.speedY;

                // New position: snapped to the east boundary (slightly inside)
                double posX = newPhyVals.posX;
                double posY = 0.0001;
                double angle = newPhyVals.angle;

                // Acceleration is preserved
                double accX = newPhyVals.accX;
                double accY = newPhyVals.accY;

                PhysicsValuesDTO reboundPhyVals = new PhysicsValuesDTO(
                                newPhyVals.timeStamp,
                                posX, posY, angle,
                                newPhyVals.size,
                                speedX, speedY,
                                accX, accY,
                                oldPhyVals.angularSpeed, oldPhyVals.angularSpeed,
                                oldPhyVals.thrust);

                this.setPhysicsValues(reboundPhyVals);
        }

        public void reboundInSouth(PhysicsValuesDTO newPhyVals, PhysicsValuesDTO oldPhyVals,
                        double worldDim_x, double worldDim_y) {

                // New speed: horizontal component flipped, vertical preserved
                double speedX = newPhyVals.speedX;
                double speedY = -newPhyVals.speedY;

                // New position: snapped to the east boundary (slightly inside)
                double posX = newPhyVals.posX;
                double posY = worldDim_y - 0.0001;
                double angle = newPhyVals.angle;

                // Acceleration is preserved
                double accX = newPhyVals.accX;
                double accY = newPhyVals.accY;

                PhysicsValuesDTO reboundPhyVals = new PhysicsValuesDTO(
                                newPhyVals.timeStamp,
                                posX, posY, angle,
                                newPhyVals.size,
                                speedX, speedY,
                                accX, accY,
                                oldPhyVals.angularSpeed, oldPhyVals.angularSpeed,
                                oldPhyVals.thrust);

                this.setPhysicsValues(reboundPhyVals);
        }

        public void resetAcceleration() {
                PhysicsValuesDTO old = this.getPhysicsValues();
                this.setPhysicsValues(new PhysicsValuesDTO(
                                old.timeStamp,
                                old.posX, old.posY, old.angle,
                                old.size,
                                old.speedX, old.speedY,
                                0, 0,
                                old.angularSpeed,
                                old.angularAcc,
                                old.thrust));

        }

        public void setAngularAcceleration(double angularAcc) {
                PhysicsValuesDTO old = this.getPhysicsValues();
                this.setPhysicsValues(new PhysicsValuesDTO(
                                old.timeStamp,
                                old.posX, old.posY, old.angle,
                                old.size,
                                old.speedX, old.speedY,
                                old.accX, old.accY,
                                old.angularSpeed,
                                angularAcc,
                                old.thrust));
        }

        public void setAngularSpeed(double angularSpeed) {
                PhysicsValuesDTO old = this.getPhysicsValues();
                this.setPhysicsValues(new PhysicsValuesDTO(
                                old.timeStamp,
                                old.posX, old.posY, old.angle,
                                old.size,
                                old.speedX, old.speedY,
                                old.accX, old.accY,
                                angularSpeed,
                                old.angularAcc,
                                old.thrust));
        }

        public void setPhysicsValues(PhysicsValuesDTO phyValues) {
                this.phyValues.set(phyValues);
        }

        public void setThrust(double thrust) {
                PhysicsValuesDTO old = this.getPhysicsValues();
                this.setPhysicsValues(new PhysicsValuesDTO(
                                old.timeStamp,
                                old.posX, old.posY, old.angle,
                                old.size,
                                old.speedX, old.speedY,
                                old.accX, old.accY,
                                old.angularSpeed,
                                old.angularAcc,
                                thrust));
        }
}
