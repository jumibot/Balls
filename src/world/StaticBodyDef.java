package world;


public class StaticBodyDef {

    public final String assetId;

    // Geometry
    public final double posX;
    public final double posY;
    public final double size;
    public final double angle;


    public StaticBodyDef(
            String assetId, double pos_x, double pos_y, 
            double size, double angle) {

        this.assetId = assetId;

        this.posX = pos_x;
        this.posY = pos_y;
        this.size = size;
        this.angle = angle;
    }
}
