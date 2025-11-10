/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.physics;


import _helpers.DoubleVector;


/**
 *
 * @author juanm
 */
public interface PhysicsEngine {

    public PhysicsValuesDTO calcNewPhysicsValues();


    public void doMovement(PhysicsValuesDTO phyValues);


    public PhysicsValuesDTO getPhysicalValues();


    public void reboundInEast(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            DoubleVector worldDimension);


    public void reboundInWest(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            DoubleVector worldDimension);


    public void reboundInNorth(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            DoubleVector worldDimension);


    public void reboundInSouth(
            PhysicsValuesDTO newPhyValues,
            PhysicsValuesDTO oldPhyValues,
            DoubleVector worldDimension);

}
