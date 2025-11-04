package balls.view;


import balls.controller.Controller;
import Images.ImageDTO;
import Images.Images;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import javax.swing.JFrame;


/**
 *
 * @author juanm
 */
public class View extends JFrame implements MouseWheelListener, ActionListener, ComponentListener {

    private Controller controller = null;
    private ControlPanel controlPanel;
    private Viewer viewer;

    private Images objectImages;
    private Images backgroundImages;
    private ImageDTO background;

    private final int GAME_PIX_HEIGHT = 1000;
    private final int GAME_PIX_WIDTH = 1000;


    /**
     * CONSTRUCTOR
     */
    public View() {
        this.objectImages = this.loadObjectImages();
        this.backgroundImages = this.loadBackgroundImages();
        this.background = this.backgroundImages.getRamdomImageDto();

        this.controlPanel = new ControlPanel(this);

        this.viewer = new Viewer(this, GAME_PIX_HEIGHT, GAME_PIX_WIDTH, this.background);

        this.createFrame();
    }


    /**
     * PUBLIC
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void activate() {
        this.viewer.activate();
    }

    /**
     * PROTECTED
     */
    protected ArrayList<RenderableObject> getRenderableObjects() {
        if (this.controller == null) {
            System.err.println("Controller is null. Can not get renderable objects · View ");
            return null;
        }

        return this.controller.getRenderableObjects();
    }


    /**
     * PRIVATE
     */
    private void addViewer(Container container) {
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1F;
        c.weighty = 0;
        c.gridheight = 10;
        c.gridwidth = 8;
        container.add(this.viewer, c);
    }


    private void createFrame() {
        Container panel;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());

        panel = this.getContentPane();

        // Add components to panel
        this.addViewer(panel);

        panel.addMouseWheelListener(this);

        this.pack();
        this.setVisible(true);

        this.addComponentListener(this);
    }


    private Images loadBackgroundImages() {
        Images images = new Images("src/images/assets/");
        images.addImageToManifest("background-1.png");
        images.addImageToManifest("background-2.jpeg");
        images.addImageToManifest("background-3.jpeg");
        images.addImageToManifest("background-4.jpeg");
        images.addImageToManifest("background-5.jpg");

        return images;
    }


    private Images loadObjectImages() {
        Images images = new Images("src/images/assets/");
        images.addImageToManifest("asteroid-1-mini.png");
        images.addImageToManifest("asteroid-2-mini.png");
        images.addImageToManifest("spaceship-1.png");
        images.addImageToManifest("spaceship-2.png");

        return images;
    }


    /**
     * OVERRIDES
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
    }


    @Override
    public void actionPerformed(ActionEvent ae) {
    }


    @Override
    public void componentResized(ComponentEvent ce) {
    }


    @Override
    public void componentMoved(ComponentEvent ce) {
    }


    @Override
    public void componentShown(ComponentEvent ce) {
    }


    @Override
    public void componentHidden(ComponentEvent ce) {
    }

}
