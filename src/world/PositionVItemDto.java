package world;


public class PositionVItemDto extends VItemDto {

    // Geometry
    public final double posX;
    public final double posY;


    public PositionVItemDto(
            String assetId, double size, double angle, 
            double posX, double posY) {

        super(assetId, size, angle);

        this.posX = posX;
        this.posY = posY;
    }
}
