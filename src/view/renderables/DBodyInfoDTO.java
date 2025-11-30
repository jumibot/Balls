package view.renderables;


public class DBodyInfoDTO extends EntityInfoDTO {

    public final long timeStamp;
    public final double speedX;
    public final double speedY;
    public final double accX;
    public final double accY;


    public DBodyInfoDTO(
            int entityId, String assetId, double size,
            long timeStamp, double posX, double posY,
            double speedX, double speedY,
            double accX, double accY, double angle) {

        super(entityId, assetId, size, posX, posY, angle);
        this.timeStamp = timeStamp;
        this.speedX = speedX;
        this.speedY = speedY;
        this.accX = accX;
        this.accY = accY;
    }


    public DBodyInfoDTO(
            int entityId, String assetId, int size,
            double posX, double posY, double angle) {

        super(entityId, assetId, size, posX, posY, angle);
        this.timeStamp = 0;
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
