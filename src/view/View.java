package view;


import _images.Images;
import assets.AssetCatalog;
import assets.AssetInfo;
import assets.Assets;
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
import java.util.List;
import javax.swing.JFrame;
import worlds.BackgroundDef;
import worlds.DecoratorDef;
import worlds.DynamicBodyDef;
import worlds.StaticBodyDef;
import worlds.WorldDefinition;


public class View extends JFrame implements MouseWheelListener, ActionListener, ComponentListener {

    private Controller controller = null;
    private ControlPanel controlPanel;
    private Renderer renderer;
    private Dimension worldDimension;

    private BufferedImage background;
    private Images spaceDecorators = new Images("");
    private Images sBodyImages = new Images("");
    private Images dBodyImage = new Images("");


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

        if (this.worldDimension == null) {
            throw new IllegalArgumentException("View dimensions not setted");
        }

        if (this.background == null) {
            throw new IllegalArgumentException("Background image not setted");
        }
        if (this.dBodyImage == null) {
            throw new IllegalArgumentException("Dynamic body images no setted");
        }
        if (this.sBodyImages == null) {
            throw new IllegalArgumentException("Static body images no setted");
        }
        if (this.spaceDecorators == null) {
            throw new IllegalArgumentException("Space Decorators not setted");
        }

        this.renderer.SetViewDimension(this.worldDimension);

        this.renderer.setImages(
                this.background, this.dBodyImage,
                this.sBodyImages, this.spaceDecorators);

        this.renderer.activate();
        this.pack();
    }


    public void loadAssets(Assets assets, WorldDefinition world) {
        this.loadBackground(assets.backgrounds, world.background);

        this.loadDynamicImages(assets.solidBodies, world.asteroids);
        this.loadDynamicImages(assets.weapons, world.misils);
        this.loadDynamicImages(assets.spaceship, world.spaceships);

        this.loadStaticImages(assets.gravityBodies, world.gravityBodies);
        this.loadStaticImages(assets.weapons, world.bombs);

        this.loadDecoratorImages(assets.spaceDecors, world.spaceDecorators);
        this.loadDecoratorImages(assets.solidBodies, world.labs);
    }


    public void loadBackground(AssetCatalog backs, BackgroundDef backDef) {

        this.background = Images.loadBufferedImage(
                backs.getPath(), backs.get(backDef.assetId).fileName);
    }


    public void loadDynamicImages(AssetCatalog catalog, List<DynamicBodyDef> bodyDef) {
        String fileName, path, uri;
        AssetInfo aInfo;

        for (DynamicBodyDef body : bodyDef) {
            path = catalog.getPath();
            aInfo = catalog.get(body.assetId);

            if (aInfo == null) {
                System.out.println("Resource info <" + body.assetId + "> not found in catalog");
            } else {

                fileName = catalog.get(body.assetId).fileName;
                this.dBodyImage.add(body.assetId, path + fileName);
            }
        }
    }


    public void loadStaticImages(AssetCatalog catalog, List<StaticBodyDef> bodyDef) {
        String uri;

        for (StaticBodyDef body : bodyDef) {
            uri = catalog.getPath() + catalog.get(body.assetId).fileName;
            this.sBodyImages.add(body.assetId, uri);
        }
    }


    public void loadDecoratorImages(AssetCatalog catalog, List<DecoratorDef> bodyDef) {
        String uri;

        for (DecoratorDef body : bodyDef) {
            uri = catalog.getPath() + catalog.get(body.assetId).fileName;
            this.spaceDecorators.add(body.assetId, uri);
        }
    }


    public void setController(Controller controller) {
        this.controller = controller;
    }


    public void setDimension(Dimension worldDim) {
        this.worldDimension = worldDim;
    }


    /**
     * PROTECTED
     */
    protected ArrayList<DBodyRenderInfoDTO> getDBodyRenderInfo() {
        if (this.controller == null) {
            throw new IllegalArgumentException("Controller not setted");
        }

        return this.controller.getDBodyRenderInfo();
    }


    /**
     * PRIVATE
     */
    private void addRendererCanva(Container container) {
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
        this.addRendererCanva(panel);

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
