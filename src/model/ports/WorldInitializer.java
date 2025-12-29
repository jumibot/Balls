package model.ports;

import assets.AssetCatalog;

public interface WorldInitializer {

    public void loadAssets(AssetCatalog assets);

    public void addStaticBody(String assetId, double size, double posX, double posY, double angle);

    public void addDecorator(String assetId, double size, double posX, double posY, double angle);
}
