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
public interface PhysicsEngineInterface {

    public abstract PhysicsValues calcNewPhysicsValues();


    public abstract PhysicsValues getPhysicsValues();


    public abstract void reboundInEast(
            PhysicsValues newPhyValues,
            PhysicsValues oldPhyValues,
            Dimension worldDimension);


    public abstract void reboundInWest(
            PhysicsValues newPhyValues,
            PhysicsValues oldPhyValues,
            Dimension worldDimension);


    public abstract void reboundInNorth(
            PhysicsValues newPhyValues,
            PhysicsValues oldPhyValues,
            Dimension worldDimension);


    public abstract void reboundInSouth(
            PhysicsValues newPhyValues,
            PhysicsValues oldPhyValues,
            Dimension worldDimension);

}
