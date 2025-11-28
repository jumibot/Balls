package view.fx;


import view.DBodyRenderable;



/**
 *
 * @author juanm
 */
public class Spin extends Fx {

    private double degreesInc;


    public Spin(double degreesInc, long delayMillis) {
        super();

        this.init(degreesInc, delayMillis);
    }


    /**
     * PRIVATES
     */
    private void init(double degreesInc, long delayMillis) {
        this.setAnimationType(FxTyoe.SPIN_AND_SCALE);
        this.setDelayMillis(delayMillis);
        this.degreesInc = degreesInc;
        this.setName("Spin/Scale");
    }


    @Override
    public void run() {
        DBodyRenderable renderable = this.getRenderable();
        FxImage voImage = this.getVOImage();

        if (renderable == null || voImage == null) {
            return; // =======================================================>
        }

        while (true) {

            if (true) {
                voImage.rotate(this.degreesInc);
            }

            try {
                /* Descansar */
                Thread.sleep(this.getDelayMillis());
            } catch (InterruptedException ex) {
                System.err.println("ERROR Sleeping in the ball thread! (Spinning) · " + ex.getMessage());
            }
        }
    }
}
