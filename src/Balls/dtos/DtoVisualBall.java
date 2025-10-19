/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Balls.dtos;


import Balls.model.Model;
import Helpers.DoubleVector;
import Helpers.State;
import Images.Images;
import java.awt.image.BufferedImage;


/**
 *
 * @author juanm
 */
public class DtoVisualBall extends DtoVisualObject {

    private final BufferedImage bfImage;
    private Double scale;


    public DtoVisualBall(BufferedImage bfImage, int maxSizeInPx, DoubleVector coordinates) {
        super(coordinates);
        
        this.bfImage = bfImage;

        int size = Integer.max(bfImage.getHeight(), bfImage.getWidth());
        this.scale = new Double(size) / new Double(maxSizeInPx);
    }


    public DtoVisualBall(State state, int maxSizeInPx, DoubleVector coordinates) {
        super(coordinates);
        
        this.bfImage = Images.getImage();
        
        int size = Integer.max(bfImage.getHeight(), bfImage.getWidth());
        this.scale = new Double(size) / new Double(maxSizeInPx);
    }
}
