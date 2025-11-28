package assets;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class AssetCatalog {

    private final String path;
    private final Map<String, AssetInfo> assetsById = new HashMap<>();
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
                new AssetInfo(assetId, fileName, type, intensity));
    }


    public AssetInfo get(String assetId) {
        AssetInfo aInfo = assetsById.get(assetId);
        return aInfo;
    }


    public ArrayList<String> getAssetIds() {
        return new ArrayList(this.assetsById.keySet());
    }
    


    public String getPath() {
        return this.path;
    }


    public String randomId() {
        List<String> keys = new ArrayList<>(this.assetsById.keySet());

        if (keys.isEmpty()) {
            throw new IllegalStateException("AssetCatalog vacío");
        }
        int index = this.rnd.nextInt(keys.size());
        return keys.get(index);
    }


    public String randomId(AssetType type) {

        // Filtrar solo los ids del tipo solicitado
        List<String> filtered = new ArrayList<>();

        for (Map.Entry<String, AssetInfo> entry : assetsById.entrySet()) {
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

        List<String> filtered = new ArrayList<>();

        for (AssetInfo info : assetsById.values()) {
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
