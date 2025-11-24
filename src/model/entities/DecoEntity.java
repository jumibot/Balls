package model.entities;


import view.RenderInfoDTO;


/**
 *
 * @author juanm
 */
public class DecoEntity extends AbstractEntity {

    private final double x;
    private final double y;
    private final double angle;


    /**
     * CONSTRUCTORS
     */
    public DecoEntity(int imageId, int size, double x, double y, double angle) {
        super(imageId, size);
        this.x = x;
        this.y = y;
        this.angle = angle;
    }


    /**
     * PUBLICS
     */
    @Override
    public synchronized void activate() {
        super.activate();
        this.setState(EntityState.ALIVE);
    }


    @Override
    public RenderInfoDTO buildRenderInfo() {
        // Misma política que StaticBody: si no está viva, no se pinta
        if (this.getState() == EntityState.DEAD
                || this.getState() == EntityState.STARTING) {
            return null;
        }

        return new RenderInfoDTO(
                this.getId(),
                this.imageId,
                this.size,
                this.getColor(),
                0, // timeStamp
                this.x,
                this.y,
                0.0, // speed_x
                0.0, // speed_y
                0.0, // acc_x
                0.0, // acc_y
                this.angle
        );
    }


    // Getters por si algún sistema de FX quiere consultarlos
    public double getX() {
        return x;
    }


    public double getY() {
        return y;
    }


    public double getAngle() {
        return angle;
    }
}
