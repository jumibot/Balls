
package view;


import java.awt.Color;


/**
 * RenderInfoDTO
 *
 * Info related to VObject rendering. Immutable DTO that packages all visual and
 * physical data required to render a VObject during a given frame. It contains
 * a flattened snapshot of the object's appearance and kinematic state
 * (position, speed and acceleration), allowing the view layer to draw the
 * object without referencing any mutable model structures.
 */
public class RenderInfoDTO {

    public final int idVObject;
    public final int idImage;
    public final Color color;
    public final int size;
    public final long timeStamp;
    public final double posX;
    public final double posY;
    public final double speedX;
    public final double speedY;
    public final double accX;
    public final double accY;
    public final double angle;


    public RenderInfoDTO(
            int id, int imageId, int size, Color color,
            long timeStamp, double posX, double posY,
            double speedX, double speedY,
            double accX, double accY, double angle) {

        this.idVObject = id;
        this.idImage = imageId;
        this.size = size;
        this.color = color;
        this.timeStamp = timeStamp;
        this.posX = posX;
        this.posY = posY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.accX = accX;
        this.accY = accY;
        this.angle = angle;
    }


    @Override
    public String toString() {
        return "RenderInfo<" + this.idVObject
                + "> p(" + this.posX + "," + this.posY + ")"
                + " s(" + this.speedX + "," + this.speedY + ")";
    }

}
