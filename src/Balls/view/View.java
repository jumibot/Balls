/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Balls.view;


import Balls.controller.Controller;
import Balls.dto.VisualBallCatalogDto;
import Helpers.Position;
import Images.dto.ImageDto;
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
import javax.swing.JFrame;


/**
 *
 * @author juanm
 */
public class View extends JFrame implements MouseWheelListener, ActionListener, ComponentListener {

    private Controller controller = null;
    private ControlPanel controlPanel;
    private Viewer viewer;

    private Images ballImages;
    private Images backgroundImages;
    private ImageDto background;

    private final int GAME_PIX_HEIGHT = 700;
    private final int GAME_PIX_WIDTH = 1300;


    /**
     * CONSTRUCTOR
     */
    public View() {
        this.loadBallImages();
        this.loadBackgrounds();
        this.background = this.backgroundImages.getRamdomImageDto();

        this.controlPanel = new ControlPanel(this);

        this.viewer = new Viewer(this, GAME_PIX_HEIGHT, GAME_PIX_WIDTH, this.background);

        this.createFrame();
        this.viewer.activate();
    }


    /**
     * PUBLIC
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }


    /**
     * PROTECTED
     */
    protected Position getBallPosition(int id) {
        return this.controller.getBallPosition(id);
    }


    protected VisualBallCatalogDto getVisualBallCatalog() {
        return this.controller.getVisualBallCatalog();
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


    private void loadBackgrounds() {
        this.ballImages = new Images("src/tg/images/assets/");
        this.ballImages.addImageToManifest("background-1.png");
        this.ballImages.addImageToManifest("background-2.png");
        this.ballImages.addImageToManifest("background-3.png");
        this.ballImages.addImageToManifest("background-4.png");
        this.ballImages.addImageToManifest("background-5.png");
    }


    private void loadBallImages() {
        this.ballImages = new Images("src/tg/images/assets/");
        this.ballImages.addImageToManifest("asteroid-1-mini.png");
        this.ballImages.addImageToManifest("asteroid-2-mini.png");
        this.ballImages.addImageToManifest("spaceship-1.png");
        this.ballImages.addImageToManifest("spaceship-2.png");
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
