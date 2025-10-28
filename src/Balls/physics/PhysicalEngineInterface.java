/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Balls.physics;


import Helpers.Position;


/**
 *
 * @author juanm
 */
interface PhysicalEngineInterface {
    public void calcNewLocation(Position pos, PhysicalValuesDTO phyValues);
    
}
