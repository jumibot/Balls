/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package world;


/**
 *
 * @author juanm
 */
public class ItemDto {

    public final String assetId;

    // Geometry
    public final double size;
    public final double angle;


    public ItemDto(String assetId, double size, double angle) {
        this.assetId = assetId;
        this.size = size;
        this.angle = angle;
    }
}
