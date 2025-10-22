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

    private int idBall;
    private int imageOrder;
    private int maxSizeInPx;

    // Color
    // ...

    public VisualBallDto(int idBall, int imageId, int maxSizeInPx) {

        this.idBall = idBall;
        this.imageOrder = imageId;
        this.maxSizeInPx = maxSizeInPx;
    }
}
