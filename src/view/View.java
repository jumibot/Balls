package view;


import _images.Images;
import _images.SpriteCache;
import controller.Controller;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;


public class View extends JFrame implements MouseWheelListener, ActionListener, ComponentListener {

    private Controller controller = null;
    private ControlPanel controlPanel;
    private Renderer renderer;

    private String assetsPath;
    private Images backgroundImages;
    private Images asteroidImages;
    private Images playerImages;
    private BufferedImage background;

    private Dimension worldDimension;


    /**
     * CONSTRUCTOR
     */
    public View() {
        this.controlPanel = new ControlPanel(this);
        this.renderer = new Renderer(this);
        this.controlPanel = new ControlPanel(this);

        this.createFrame();
    }


    /**
     * PUBLIC
     */
    public void activate() {
        if (this.worldDimension == null) {
            throw new IllegalArgumentException("Null world dimension");
        }

        if (this.assetsPath == null) {
            throw new IllegalArgumentException("Null path");
        }

        if (this.asteroidImages == null || this.background == null || this.playerImages == null) {
            throw new IllegalArgumentException("Null file list");
        }

        this.renderer.SetViewDimension(this.worldDimension);

        this.renderer.setAssets(background, this.asteroidImages, this.playerImages);
        this.renderer.activate();
        this.pack();
    }


    public void setAssets(
            String assetsPath,
            ArrayList<String> background,
            ArrayList<String> asteroid,
            ArrayList<String> player) {

        this.setAssetsPath(assetsPath);
        this.setBackgroundImages(background);
        this.setAsteroidImages(asteroid);
        this.setPlayerImages(player);
    }


    public void setAssetsPath(String assetsPath) {
        this.assetsPath = assetsPath;
    }


    public void setAsteroidImages(ArrayList<String> asteroid) {
        this.asteroidImages = new Images(this.assetsPath, asteroid);
    }


    public void setBackgroundImages(ArrayList<String> background) {
        this.backgroundImages = new Images(this.assetsPath, background);
        this.background = this.backgroundImages.getRamdomBufferedImage();
    }


    public void setController(Controller controller) {
        this.controller = controller;
    }


    public void setDimension(Dimension worldDim) {
        this.worldDimension = worldDim;
    }


    public void setPlayerImages(ArrayList<String> player) {
        this.playerImages = new Images(this.assetsPath, player);
    }


    /**
     * PROTECTED
     */
    protected ArrayList<RenderInfoDTO> getRenderInfo() {
        if (this.controller == null) {
            throw new IllegalArgumentException("Controller not setted");
        }

        return this.controller.getRenderableObjects();
    }


    /**
     * PRIVATE
     */
    private void addRenderer(Container container) {
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1F;
        c.weighty = 0;
        c.gridheight = 10;
        c.gridwidth = 8;
        container.add(this.renderer, c);
    }


    private void createFrame() {
        Container panel;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());

        panel = this.getContentPane();
        this.addRenderer(panel);

        panel.addMouseWheelListener(this);

        this.pack();
        this.setVisible(true);

        this.addComponentListener(this);
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

    /**
     * PRIVATES
     */
}
