package world;


public class StaticBodyDef {

    public final String assetId;

    // Geometry
    public final double posX;
    public final double posY;
    public final double size;
    public final double angle;


    public StaticBodyDef(
            String assetId, double posX, double posY, 
            double size, double angle) {

        this.assetId = assetId;

        this.posX = posX;
        this.posY = posY;
        this.size = size;
        this.angle = angle;
    }
}
