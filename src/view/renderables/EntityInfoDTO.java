package view.renderables;


public class EntityInfoDTO {

    public final int entityId;
    public final String assetId;
    public final double size;
    public final double posX;
    public final double posY;
    public final double angle;


    public EntityInfoDTO(
            int entityId, String assetId, double size,
            double posX, double posY, double angle) {

        this.entityId = entityId;
        this.assetId = assetId;
        this.size = size;
        this.posX = posX;
        this.posY = posY;
        this.angle = angle;
    }


    @Override
    public String toString() {
        return "SBody RenderInfo<" + this.entityId
                + "> p(" + this.posX + "," + this.posY + ")";
    }
}
