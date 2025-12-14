package world;


public class BackgroundDto {

    public final String assetId;
    public final double scrollSpeedX;
    public final double scrollSpeedY;


    public BackgroundDto(String assetId, double scrollSpeedX, double scrollSpeedY) {

        this.assetId = assetId;
        this.scrollSpeedX = scrollSpeedX;
        this.scrollSpeedY = scrollSpeedY;
    }
}
