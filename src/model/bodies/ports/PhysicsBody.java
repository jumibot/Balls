package model.bodies.ports;


import model.physics.ports.PhysicsEngine;
import model.physics.ports.PhysicsValuesDTO;


public interface PhysicsBody {

    public PhysicsEngine getPhysicsEngine();


    default PhysicsValuesDTO getPhysicsValues() {
        return this.getPhysicsEngine().getPhysicsValues();
    }


    default void doMovement(PhysicsValuesDTO phyValues) {
        PhysicsEngine engine = this.getPhysicsEngine();
        engine.setPhysicsValues(phyValues);
    }


    default void reboundInEast(PhysicsValuesDTO newVals, PhysicsValuesDTO oldVals, 
            double worldWidth, double worldHeight) {
        
        PhysicsEngine engine = this.getPhysicsEngine();
        engine.reboundInEast(newVals, oldVals, worldWidth, worldHeight);
    }


    default void reboundInWest(PhysicsValuesDTO newVals, PhysicsValuesDTO oldVals,
            double worldWidth, double worldHeight) {
        
        PhysicsEngine engine = this.getPhysicsEngine();
        engine.reboundInWest(newVals, oldVals, worldWidth, worldHeight);
    }


    default void reboundInNorth(PhysicsValuesDTO newVals, PhysicsValuesDTO oldVals,
            double worldWidth, double worldHeight) {
        PhysicsEngine engine = this.getPhysicsEngine();
        engine.reboundInNorth(newVals, oldVals, worldWidth, worldHeight);
    }


    default void reboundInSouth(PhysicsValuesDTO newVals, PhysicsValuesDTO oldVals,
            double worldWidth, double worldHeight) {
        PhysicsEngine engine = this.getPhysicsEngine();
        engine.reboundInSouth(newVals, oldVals, worldWidth, worldHeight);
    }
}
