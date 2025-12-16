package world;


public class PositionItemDto extends ItemDto {

    // Geometry
    public final double posX;
    public final double posY;


    public PositionItemDto(
            String assetId, double size, double angle, 
            double posX, double posY) {

        super(assetId, size, angle);

        this.posX = posX;
        this.posY = posY;
    }
}
