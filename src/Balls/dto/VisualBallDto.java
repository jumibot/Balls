/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Balls.dto;


/**
 *
 * @author juanm
 */
public class VisualBallDto {

    private int imageId;
    private int ballId;
    private int maxSizeInPx;

    // Color
    // ...

    public VisualBallDto(int ballId, int imageId, int maxSizeInPx) {

        this.ballId = ballId;
        this.imageId = imageId;
        this.maxSizeInPx = maxSizeInPx;
    }
}
