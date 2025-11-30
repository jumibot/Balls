package world;


public class StaticBodyDef {

    public final String assetId;

    // Geometry
    public final double pos_x;
    public final double pos_y;
    public final double size;
    public final double rotationDeg;


    public StaticBodyDef(
            String assetId, double pos_x, double pos_y, 
            double size, double rotationDeg) {

        this.assetId = assetId;

        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.size = size;
        this.rotationDeg = rotationDeg;
    }
}
