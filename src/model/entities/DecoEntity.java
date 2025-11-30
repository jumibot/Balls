package model.entities;


import view.renderables.DBodyInfoDTO;
import view.renderables.EntityInfoDTO;


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
    public DecoEntity(String assetId, double size, double x, double y, double angle) {
        super(assetId, size);
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
    public EntityInfoDTO buildEntityInfo() {
        // Misma política que StaticBody: si no está viva, no se pinta
        if (this.getState() == EntityState.DEAD
                || this.getState() == EntityState.STARTING) {
            return null;
        }

        return new EntityInfoDTO(
                this.getEntityId(),
                this.assetId,
                this.size,
                this.x, this.y,
                this.angle);
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
