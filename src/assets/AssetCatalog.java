package assets;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class AssetCatalog {

    private final String path;
    private final Map<String, AssetInfoDTO> assetsById = new HashMap<>();
    private Random rnd = new Random();


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
                new AssetInfoDTO(assetId, fileName, type, intensity));
    }


    public AssetInfoDTO get(String assetId) {
        AssetInfoDTO aInfo = assetsById.get(assetId);
        return aInfo;
    }


    public ArrayList<String> getAssetIds() {
        return new ArrayList(this.assetsById.keySet());
    }


    public String getPath() {
        return this.path;
    }


    public String randomId(AssetType type) {
        if (type == null) {
            throw new IllegalStateException("Asset type is null!");
        }

        // Filtrar solo los ids del tipo solicitado
        List<String> filtered = new ArrayList<>();

        for (Map.Entry<String, AssetInfoDTO> entry : assetsById.entrySet()) {
            if (entry.getValue().type == type) {
                filtered.add(entry.getKey());
            }
        }

        if (filtered.isEmpty()) {
            throw new IllegalStateException("No hay assets del tipo " + type
                    + " en el catálogo: " + path);
        }

        return filtered.get(rnd.nextInt(filtered.size()));
    }


    public String randomId(AssetType type, AssetIntensity intensity) {
        if (type == null) {
            throw new IllegalStateException("Asset type is null!");
        }

        List<String> filtered = new ArrayList<>();

        for (AssetInfoDTO info : assetsById.values()) {
            if (info.type == type && info.intensity == intensity) {
                filtered.add(info.assetId);
            }
        }

        if (filtered.isEmpty()) {
            return randomId(type); // fallback
        }

        return filtered.get(rnd.nextInt(filtered.size()));
    }


    public boolean exists(String assetId) {
        return assetsById.containsKey(assetId);
    }
}
