package view;


import view.renderables.DBodyInfoDTO;
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import view.renderables.EntityInfoDTO;
import world.BackgroundDef;
import world.DecoratorDef;
import world.DynamicBodyDef;
import world.StaticBodyDef;
import world.WorldDefinition;


public class View extends JFrame implements MouseWheelListener,
        ActionListener, ComponentListener, KeyListener {

    private Controller controller = null;
    private ControlPanel controlPanel;
    private Renderer renderer;
    private Dimension worldDimension;

    private BufferedImage background;
    private Images spaceDecorators = new Images("");
    private Images sBodyImages = new Images("");
    private Images dBodyImage = new Images("");

    private String localPlayerId;


    /**
     * CONSTRUCTOR
     */
    public View() {
        this.controlPanel = new ControlPanel(this);
        this.controlPanel = new ControlPanel(this);

        this.renderer = new Renderer(this);

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
        this.loadBackground(assets.backgrounds, world.backgroundDef);

        this.loadDynamicImages(assets.solidBodies, world.asteroidsDef);
        this.loadDynamicImages(assets.weapons, world.misilsDef);
        this.loadDynamicImages(assets.spaceship, world.spaceshipsDef);
        this.loadStaticImages(assets.gravityBodies, world.gravityBodiesDef);
        this.loadStaticImages(assets.weapons, world.bombsDef);
        this.loadDecoratorImages(assets.spaceDecors, world.spaceDecoratorsDef);
    }


    public void loadBackground(AssetCatalog backs, BackgroundDef backDef) {
        this.background = Images.loadBufferedImage(
                backs.getPath(), backs.get(backDef.assetId).fileName);
    }


    public void loadDecoratorImages(AssetCatalog catalog, List<DecoratorDef> decoList) {
        AssetInfo assetInfo;

        for (DecoratorDef deco : decoList) {
            assetInfo = catalog.get(deco.assetId);

            if (assetInfo == null) {
                System.err.println("Resource info <" + deco.assetId + "> not found in catalog");
            } else {
                this.spaceDecorators.add(
                        deco.assetId,
                        catalog.getPath() + catalog.get(deco.assetId).fileName);
            }
        }
    }


    public void loadDynamicImages(AssetCatalog catalog, List<DynamicBodyDef> bodyDef) {
        String fileName, path, uri;
        AssetInfo assetInfo;

        for (DynamicBodyDef body : bodyDef) {
            path = catalog.getPath();
            assetInfo = catalog.get(body.assetId);

            if (assetInfo == null) {
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


    public void setController(Controller controller) {
        this.controller = controller;
    }


    public void setDimension(Dimension worldDim) {
        this.worldDimension = worldDim;
    }


    public void setLocalPlayer(String localPlayerId) {
        this.localPlayerId = localPlayerId;
    }


    public void updateSBodyInfo(ArrayList<EntityInfoDTO> bodiesInfo) {
        this.renderer.updateSBodyRenderables(bodiesInfo);
    }


    public void updateDecoratorsInfo(ArrayList<EntityInfoDTO> decosInfo) {
        this.renderer.updateDecoratosRenderables(decosInfo);
    }


    /**
     * PROTECTED
     */
    protected ArrayList<DBodyInfoDTO> getDBodyInfo() {
        if (this.controller == null) {
            throw new IllegalArgumentException("Controller not setted");
        }

        return this.controller.getDBodyInfo();
    }


    protected ArrayList<EntityInfoDTO> getSBodyInfo() {
        if (this.controller == null) {
            throw new IllegalArgumentException("Controller not setted");
        }

        return this.controller.getSBodyInfo();
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
        this.renderer.setFocusable(true);
        this.renderer.requestFocusInWindow();   // Para que reciba el foco de teclado
        this.renderer.addKeyListener(this);

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


    @Override
    public void keyPressed(KeyEvent e) {
        if (this.localPlayerId == null) {
            System.out.println("Local player not setted!");
            return;
        }

        if (this.controller == null) {
            System.out.println("Controller not set yet");
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                controller.playerThrustOn(this.localPlayerId);
                break;
            case KeyEvent.VK_DOWN:
                this.controller.playerReverseThrust(this.localPlayerId);
                break;
            case KeyEvent.VK_LEFT:
                controller.playerRotateLeftOn(this.localPlayerId);
                break;
            case KeyEvent.VK_RIGHT:
                controller.playerRotateRightOn(this.localPlayerId);
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        if (this.localPlayerId == null) {
            System.out.println("Local player not setted!");
            return;
        }

        if (this.controller == null) {
            System.out.println("Controller not set yet");
            return;
        }
               
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                this.controller.playerThrustOff(localPlayerId);
                break;
            case KeyEvent.VK_DOWN:
                this.controller.playerThrustOff(localPlayerId);
                break;
            case KeyEvent.VK_LEFT:
                this.controller.playerRotateOff(localPlayerId);
                break;
            case KeyEvent.VK_RIGHT:
                this.controller.playerRotateOff(localPlayerId);
                break;
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
        // No usado
    }
}
