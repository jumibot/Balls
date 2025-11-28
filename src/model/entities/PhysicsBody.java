package model.entities;


import model.physics.PhysicsEngine;
import model.physics.PhysicsValues;


public interface PhysicsBody {

    public PhysicsEngine getPhysicsEngine();


    default PhysicsValues getPhysicsValues() {
        return getPhysicsEngine().getPhysicsValues();
    }


    default void doMovement(PhysicsValues phyValues) {
        PhysicsEngine engine = getPhysicsEngine();
        engine.setPhysicsValues(phyValues);
    }


    default void reboundInEast(PhysicsValues newVals, PhysicsValues oldVals, 
            double worldWidth, double worldHeight) {
        
        PhysicsEngine engine = getPhysicsEngine();
        engine.reboundInEast(newVals, oldVals, worldWidth, worldHeight);
    }


    default void reboundInWest(PhysicsValues newVals, PhysicsValues oldVals,
            double worldWidth, double worldHeight) {
        
        PhysicsEngine engine = getPhysicsEngine();
        engine.reboundInWest(newVals, oldVals, worldWidth, worldHeight);
    }


    default void reboundInNorth(PhysicsValues newVals, PhysicsValues oldVals,
            double worldWidth, double worldHeight) {
        PhysicsEngine engine = getPhysicsEngine();
        engine.reboundInNorth(newVals, oldVals, worldWidth, worldHeight);
    }


    default void reboundInSouth(PhysicsValues newVals, PhysicsValues oldVals,
            double worldWidth, double worldHeight) {
        PhysicsEngine engine = getPhysicsEngine();
        engine.reboundInSouth(newVals, oldVals, worldWidth, worldHeight);
    }
}
