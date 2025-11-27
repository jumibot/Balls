package worlds;


public class DynamicBodyDef {

    public final String assetId;
    public final StaticShapeType shapeType;

    // Geometry
    public final double size;


    public DynamicBodyDef(
            String assetId, StaticShapeType shapeType,
            double size) {

        this.assetId = assetId;
        this.shapeType = shapeType;

        this.size = size;
    }
}
