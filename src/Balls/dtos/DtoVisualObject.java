/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Balls.dtos;


import Helpers.DoubleVector;
import Helpers.Position;


/**
 *
 * @author juanm
 */
public class DtoVisualObject {

    private Position position;


    public DtoVisualObject(DoubleVector coordinates) {
        this.position = new Position(coordinates);
    }

}
