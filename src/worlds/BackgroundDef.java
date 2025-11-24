package worlds;

public class BackgroundDef {

    public final String assetId;
    public final double scrollSpeedX;
    public final double scrollSpeedY;


    public BackgroundDef(
            String assetId,
            double scrollSpeedX,
            double scrollSpeedY) {

        this.assetId = assetId;
        this.scrollSpeedX = scrollSpeedX;
        this.scrollSpeedY = scrollSpeedY;
    }
}