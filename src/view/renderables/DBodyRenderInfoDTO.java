package view.renderables;


import java.awt.Color;


public class DBodyRenderInfoDTO {

    public final int entityId;
    public final String assetId;
    public final int size;
    public final long timeStamp;
    public final double posX;
    public final double posY;
    public final double speedX;
    public final double speedY;
    public final double accX;
    public final double accY;
    public final double angle;


    public DBodyRenderInfoDTO(
            int entityId, String assetId, int size, 
            long timeStamp, double posX, double posY,
            double speedX, double speedY,
            double accX, double accY, double angle) {

        this.entityId = entityId;
        this.assetId = assetId;
        this.size = size;
        this.timeStamp = timeStamp;
        this.posX = posX;
        this.posY = posY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.accX = accX;
        this.accY = accY;
        this.angle = angle;
    }


    public DBodyRenderInfoDTO(
            int entityId, String assetId, int size,
            double posX, double posY, double angle) {

        this.entityId = entityId;
        this.assetId = assetId;
        this.size = size;
        this.angle = angle;

        this.timeStamp = 0;
        this.posX = 0;
        this.posY = 0;
        this.speedX = 0;
        this.speedY = 0;
        this.accX = 0;
        this.accY = 0;
    }


    @Override
    public String toString() {
        return "DBody RenderInfo<" + this.entityId
                + "> p(" + this.posX + "," + this.posY + ")"
                + " s(" + this.speedX + "," + this.speedY + ")";
    }
}
