package engine.model.physics.core;

import static java.lang.System.nanoTime;

import java.util.concurrent.atomic.AtomicReference;

import engine.model.physics.ports.PhysicsEngine;
import engine.model.physics.ports.PhysicsValuesMDTO;

public abstract class AbstractPhysicsEngine implements PhysicsEngine {

        private final AtomicReference<PhysicsValuesMDTO> phyValues; // Current values (DTO#1)
        protected PhysicsValuesMDTO nextPhyValues; // Next frame values (DTO#2)
        protected PhysicsValuesMDTO snapshotDTO; // Snapshot for rendering (DTO#3)

        // region Constructors
        public AbstractPhysicsEngine(PhysicsValuesMDTO dto1, PhysicsValuesMDTO dto2, PhysicsValuesMDTO dto3) {
                if (dto1 == null || dto2 == null || dto3 == null) {
                        throw new IllegalArgumentException("PhysicsValuesDTO cannot be null");
                }

                this.phyValues = new AtomicReference<>(dto1);
                this.nextPhyValues = dto2;
                this.snapshotDTO = dto3;
        }
        // endregion

        // *** PUBLIC ***

        public abstract PhysicsValuesMDTO calcNewPhysicsValues();

        public abstract void angularAccelerationInc(double angularAcc);

        public final PhysicsValuesMDTO getPhysicsValues() {
                return this.phyValues.get();
        }

        // region Rebound (reboundIn***)
        public abstract void reboundInEast(
                        PhysicsValuesMDTO phyValues, double worldDim_x, double worldDim_y);

        public abstract void reboundInWest(
                        PhysicsValuesMDTO phyValues, double worldDim_x, double worldDim_y);

        public abstract void reboundInNorth(
                        PhysicsValuesMDTO phyValues, double worldDim_x, double worldDim_y);

        public abstract void reboundInSouth(
                        PhysicsValuesMDTO phyValues, double worldDim_x, double worldDim_y);
        // endregion

        public void resetAcceleration() {
                PhysicsValuesMDTO old = this.getPhysicsValues();
                
                // Update nextPhyValues instead of creating new DTO
                nextPhyValues.update(
                                old.timeStamp,
                                old.posX, old.posY, old.angle,
                                old.size,
                                old.speedX, old.speedY,
                                0, 0,
                                old.angularSpeed,
                                old.angularAcc,
                                old.thrust);
                
                this.setPhysicsValues(nextPhyValues);
        }

        // region Setters (set***)
        public final void setAngularAcceleration(double angularAcc) {
                PhysicsValuesMDTO old = this.getPhysicsValues();
                
                // Update nextPhyValues instead of creating new DTO
                nextPhyValues.update(
                                old.timeStamp,
                                old.posX, old.posY, old.angle,
                                old.size,
                                old.speedX, old.speedY,
                                old.accX, old.accY,
                                old.angularSpeed,
                                angularAcc,
                                old.thrust);
                
                this.setPhysicsValues(nextPhyValues);
        }

        public abstract void setAngularSpeed(double angularSpeed);

        public final void setPhysicsValues(PhysicsValuesMDTO phyValues) {
                if (phyValues == null) {
                        throw new IllegalArgumentException("PhysicsValuesDTO cannot be null");
                }

                // Doble buffer swap: phyValues becomes nextPhyValues
                this.nextPhyValues = this.phyValues.getAndSet(phyValues);
        }
        
        public final PhysicsValuesMDTO getNextPhyValues() {
                return this.nextPhyValues;
        }
        
        public final PhysicsValuesMDTO getSnapshotDTO() {
                return this.snapshotDTO;
        }

        public final void setThrust(double thrust) {
                PhysicsValuesMDTO old = this.getPhysicsValues();
                
                // Update nextPhyValues instead of creating new DTO
                nextPhyValues.update(
                                old.timeStamp,
                                old.posX, old.posY, old.angle,
                                old.size,
                                old.speedX, old.speedY,
                                old.accX, old.accY,
                                old.angularSpeed,
                                old.angularAcc,
                                thrust);
                
                this.setPhysicsValues(nextPhyValues);
        }
        // endregion

        @Override
        public void stopPushing() {
                PhysicsValuesMDTO old = this.getPhysicsValues();
                
                // Update nextPhyValues instead of creating new DTO
                nextPhyValues.update(
                                old.timeStamp,
                                old.posX, old.posY, old.angle,
                                old.size,
                                old.speedX, old.speedY,
                                0, 0, // Reset accelerations
                                old.angularSpeed,
                                old.angularAcc,
                                0.0d); // Reset thrust
                
                this.setPhysicsValues(nextPhyValues);
        }
}
