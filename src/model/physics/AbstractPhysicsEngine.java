/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.physics;


import java.util.concurrent.atomic.AtomicReference;


/**
 *
 * @author juanm
 */
public abstract class AbstractPhysicsEngine {

    private final AtomicReference<PhysicsValuesDTO> phyValues; // *+


    /**
     * CONSTRUCTORS
     */
    public AbstractPhysicsEngine(PhysicsValuesDTO phyValues) {
        this.phyValues = new AtomicReference(phyValues);
    }


    /**
     * PUBLIC
     */
    public PhysicsValuesDTO getPhysicalValues() {
        return this.phyValues.get();
    }


    public void setPhysicsValues(PhysicsValuesDTO phyValues) {
        this.phyValues.set(phyValues);
    }
}
