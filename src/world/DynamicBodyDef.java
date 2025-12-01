package world;


public class DynamicBodyDef {

    public final String assetId;

    // Geometry
    public final double size;
    public final double angle;


    public DynamicBodyDef(String assetId, double size, double angle) {
        this.assetId = assetId;
        this.size = size;
        this.angle = angle;
    }
}
