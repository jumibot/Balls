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

    public final int id;
    public final int imageOrder;
    public final int maxSizeInPx;

    // Color
    // ...

    public VisualBallDto(int id, int imageId, int maxSizeInPx) {

        this.id = id;
        this.imageOrder = imageId;
        this.maxSizeInPx = maxSizeInPx;
    }
}
