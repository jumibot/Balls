/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.physics;


import java.awt.Dimension;


/**
 *
 * @author juanm
 */
public interface PhysicsEngine {

    public abstract PhysicsValuesDTO calcNewPhysicsValues();


    public abstract PhysicsValuesDTO getPhysicalValues();


    public abstract void reboundInEast(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDimension);


    public abstract void reboundInWest(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDimension);


    public abstract void reboundInNorth(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDimension);


    public abstract void reboundInSouth(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            Dimension worldDimension);

}
