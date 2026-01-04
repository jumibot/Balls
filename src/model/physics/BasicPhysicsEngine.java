package model.physics;

import static java.lang.System.nanoTime;

import model.physics.core.AbstractPhysicsEngine;
import model.physics.ports.PhysicsEngine;
import model.physics.ports.PhysicsValuesDTO;

public class BasicPhysicsEngine extends AbstractPhysicsEngine implements PhysicsEngine {

    /**
     * CONSTRUCTORS
     */
    public BasicPhysicsEngine(PhysicsValuesDTO phyVals) {
        super(phyVals);
    }

    /**
     * PUBLICS
     */
    @Override
    public PhysicsValuesDTO calcNewPhysicsValues() {
        PhysicsValuesDTO phyVals = this.getPhysicsValues();
        long now = nanoTime();
        long elapsedNanos = now - phyVals.timeStamp;

        double dt = elapsedNanos / 1_000_000_000.0; // Nanos to seconds
        return integrateMRUA(phyVals, dt);
    }

    /**
     * PRIVATES
     */
    private PhysicsValuesDTO integrateMRUA(PhysicsValuesDTO phyVals, double dt) {
        // Applying thrust according actual angle
        double newAccX = phyVals.accX;
        double newAccY = phyVals.accY;
        double angleRad = Math.toRadians(phyVals.angle);
        if (phyVals.thrust != 0.0d) {
            newAccX += Math.cos(angleRad) * phyVals.thrust;
            newAccY += Math.sin(angleRad) * phyVals.thrust;
        }

        // v1 = v0 + a*dt
        double oldSpeedX = phyVals.speedX;
        double oldSpeedY = phyVals.speedY;
        double newSpeedX = oldSpeedX + newAccX * dt;
        double newSpeedY = oldSpeedY + newAccY * dt;

        // avg_speed = (v0 + v1) / 2
        double avgSpeedX = (oldSpeedX + newSpeedX) * 0.5;
        double avgSpeedY = (oldSpeedY + newSpeedY) * 0.5;

        // x1 = x0 + v_avg * dt
        double newPosX = phyVals.posX + avgSpeedX * dt;
        double newPosY = phyVals.posY + avgSpeedY * dt;

        // w1 = w0 + α*dt
        double newAngularSpeed = phyVals.angularSpeed + phyVals.angularAcc * dt;

        // θ1 = θ0 + w0*dt + 0.5*α*dt^2
        double newAngle = (phyVals.angle
                + phyVals.angularSpeed * dt
                + 0.5d * newAngularSpeed * dt * dt) % 360;


        long newTimeStamp = phyVals.timeStamp + (long) (dt * 1_000_000_000.0d);

        return new PhysicsValuesDTO(
                newTimeStamp,
                newPosX, newPosY, newAngle,
                phyVals.size,
                newSpeedX, newSpeedY,
                newAccX, newAccY,
                newAngularSpeed,
                phyVals.angularAcc, // keep same angular acc
                phyVals.thrust // keep same thrust
        );
    }
}
