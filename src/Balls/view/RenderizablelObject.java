/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Balls.view;


//int size = Integer.max(bfImage.getHeight(), bfImage.getWidth());
//this.scale = new Double(size) / new Double(maxSizeInPx);
import Helpers.DoubleVector;
import Helpers.Position;


/**
 *
 * @author juanm
 */
public class RenderizablelObject {

    private Position position;


    public RenderizablelObject(DoubleVector coordinates) {
        this.position = new Position(coordinates);
    }

}
