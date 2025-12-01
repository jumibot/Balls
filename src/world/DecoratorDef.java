
package world;

public class DecoratorDef {

    public final String assetId;
    public final int layer;

    // Geometry
    public final double pos_x;
    public final double pos_y;
    public final double size;
    public final double angle;

    public DecoratorDef(
            String assetId, int layer,
            double pos_x, double pos_y, double size, double angle) {

        this.assetId = assetId;
        this.layer = layer;
        
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.size = size;
        this.angle = angle;
    }
}
    
