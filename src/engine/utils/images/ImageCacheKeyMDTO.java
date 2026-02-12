package engine.utils.images;

public class ImageCacheKeyMDTO {

    public int angle;
    public String assetId;
    public int size;
    private int hash;

    public ImageCacheKeyMDTO(int angle, String assetId, int size) {
        this.angle = angle;
        this.assetId = assetId;
        this.size = size;
        this.hash = this.hash();

    }

    public void set(int angle, String assetId, int size) {
        this.angle = angle;
        this.assetId = assetId;
        this.size = size;
        this.hash = this.hash();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ImageCacheKeyMDTO other)) {
            throw new IllegalArgumentException("ImageCacheKeyDTO: equals: argument is not of type ImageCacheKeyDTO");
        }
        if (this.assetId == null) {
            throw new IllegalStateException("ImageCacheKeyDTO: equals: assetId is null");
        }

        return this.assetId == other.assetId ||
                (this.assetId.equals(other.assetId)
                        && this.angle == other.angle
                        && size == other.size);
    }

    public int hash() {
        this.hash = 17;
        this.hash = 31 * this.hash + angle;
        this.hash = 31 * this.hash + size;
        this.hash = 31 * this.hash + (assetId == null ? 0 : assetId.hashCode());
        return this.hash;
    }

    @Override
    public int hashCode() {
        return this.hash;
    }
}
