package assets;


import java.util.HashMap;
import java.util.Map;


public class AssetCatalog {

    private final String path;
    private final Map<String, AssetInfo> assetsById = new HashMap<>();


    /**
     * CONSTRUCTORS
     */
    public AssetCatalog(String path) {
        this.path = path;

    }


    /**
     * PUBLIC
     */
    public void register(String assetId, String fileName,
            AssetType type, AssetIntensity intensity) {

        this.assetsById.put(
                assetId,
                new AssetInfo(assetId, fileName, type, intensity));
    }


    public AssetInfo get(String assetId) {
        return assetsById.get(assetId);
    }


    public boolean exists(String assetId) {
        return assetsById.containsKey(assetId);
    }
}
