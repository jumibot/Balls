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

    public abstract PhysicsValues calcNewPhysicsValues();


    public abstract PhysicsValues getPhysicsValues();


    public abstract void reboundInEast(
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues,
            double worldDim_x, double worldDim_y);


    public abstract void reboundInWest(
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues,
            double worldDim_x, double worldDim_y);


    public abstract void reboundInNorth(
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues,
            double worldDim_x, double worldDim_y);


    public abstract void reboundInSouth(
            PhysicsValues newPhyValues, PhysicsValues oldPhyValues,
            double worldDim_x, double worldDim_y);
    
    
    public abstract void setPhysicsValues(PhysicsValues phyValues);


}
