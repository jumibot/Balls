
package worlds;

public class DecoratorDef {

    public final String assetId;
    public final StaticShapeType shapeType;
    public final int layer;

    // Geometry
    public final double pos_x;
    public final double pos_y;
    public final double size;
    public final double rotationDeg;

    public DecoratorDef(
            String assetId, StaticShapeType shapeType, int layer,
            double pos_x, double pos_y, double size, double rotationDeg) {

        this.assetId = assetId;
        this.shapeType = shapeType;
        this.layer = layer;
        
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.size = size;
        this.rotationDeg = rotationDeg;
    }
}
    
