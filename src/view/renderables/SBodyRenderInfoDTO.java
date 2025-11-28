package view.renderables;


import java.awt.Color;


public class SBodyRenderInfoDTO {

    public final int entityId;
    public final String assetId;
    public final int size;
    public final double posX;
    public final double posY;
    public final double angle;


    public SBodyRenderInfoDTO(
            int entityId, String assetId, int size,
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
