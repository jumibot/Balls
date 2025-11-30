package world;


public class DynamicBodyDef {

    public final String assetId;

    // Geometry
    public final double size;


    public DynamicBodyDef(String assetId, double size) {
        this.assetId = assetId;
        this.size = size;
    }
}
