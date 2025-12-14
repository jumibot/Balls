package view;


import view.renderables.DBodyInfoDTO;
import _images.Images;
import assets.AssetCatalog;
import assets.AssetInfoDTO;
import assets.ProjectAssets;
import controller.Controller;
import controller.EngineState;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import view.renderables.EntityInfoDTO;
import world.BackgroundDto;
import world.VItemDto;
import world.WorldDefinition;


/**
 * View ----
 *
 * Swing top-level window that represents the presentation layer of the engine.
 * This class wires together: - The rendering surface (Renderer) - Asset loading
 * and image catalogs (Images) - User input (KeyListener) and command dispatch
 * to the Controller
 *
 * Architectural role ------------------ View is a thin façade over rendering +
 * input: - It does not simulate anything. - It does not own world state. - It
 * communicates with the model exclusively through the Controller.
 *
 * The Renderer pulls dynamic snapshots every frame (via View -> Controller),
 * while static/decorator snapshots are pushed into the View/Renderer only when
 * they change (to avoid redundant per-frame updates for entities that do not
 * move).
 *
 * Lifecycle --------- Construction: - Creates the ControlPanel (UI controls, if
 * any). - Creates the Renderer (Canvas). - Builds the JFrame layout and
 * attaches the key listener.
 *
 * Activation (activate()): - Validates mandatory dependencies (dimensions,
 * background, image catalogs). - Injects view dimensions and images into the
 * Renderer. - Starts the Renderer thread (active rendering loop).
 *
 * Asset management --------------- loadAssets(...) loads and registers all
 * visual resources required by the world: - Background image (single
 * BufferedImage). - Dynamic body sprites (ships, asteroids, missiles, etc.). -
 * Static body sprites (gravity bodies, bombs, etc.). - Decorator sprites
 * (parallax / space decor).
 *
 * The View stores catalogs as Images collections, which are later converted
 * into GPU/compatible caches inside the Renderer (ImageCache).
 *
 * Engine state delegation ----------------------- View exposes getEngineState()
 * as a convenience bridge for the Renderer. The render loop can stop or pause
 * based on Controller-owned engine state.
 *
 * Input handling -------------- Keyboard input is captured at the rendering
 * Canvas level (Renderer is focusable and receives the KeyListener) and
 * translated into high-level Controller commands: - Thrust on/off (forward uses
 * positive thrust; reverse thrust is handled as negative thrust, and both are
 * stopped via the same thrustOff command). - Rotation left/right and rotation
 * off. - Fire: handled as an edge-triggered action using fireKeyDown to prevent
 * key repeat from generating continuous shots while SPACE is held.
 *
 * Focus and Swing considerations ------------------------------
 *
 * The Renderer is the focus owner for input. Focus is requested after the frame
 * becomes visible using SwingUtilities.invokeLater(...) to improve reliability
 * with Swing’s event dispatch timing.
 *
 * Threading considerations ------------------------ Swing is single-threaded
 * (EDT), while rendering runs on its own thread. This class keeps its
 * responsibilities minimal: - It only pushes static/decorator updates when
 * needed. - Dynamic snapshot pulling is done inside the Renderer thread through
 * View -> Controller getters.
 *
 * Design goals ------------ - Keep the View as a coordinator, not a state
 * holder. - Keep rendering independent and real-time (active rendering). -
 * Translate user input into controller commands cleanly and predictably.
 *
 *
 */
public class View extends JFrame implements KeyListener {

    private Controller controller = null;
    private ControlPanel controlPanel;
    private Renderer renderer;
    private Dimension worldDimension;

    private BufferedImage background;
    private Images images = new Images("");

    private String localPlayerId;

    private boolean fireKeyDown = false;


    /**
     * CONSTRUCTOR
     */
    public View() {
        this.controlPanel = new ControlPanel(this);

        this.renderer = new Renderer(this);

        this.createFrame();
    }


    /**
     * PUBLIC
     */
    public void activate() {
        if (this.worldDimension == null) {
            throw new IllegalArgumentException("View dimensions not setted");
        }

        if (this.background == null) {
            throw new IllegalArgumentException("Background image not setted");
        }

        if (this.images.getSize() <= 0) {
            throw new IllegalArgumentException("Dynamic body images no setted");
        }

        this.renderer.SetViewDimension(this.worldDimension);

        this.renderer.setImages(this.background, this.images);

        this.renderer.activate();
        this.pack();
    }


    public EngineState getEngineState() {
        return this.controller.getEngineState();
    }


    public void loadAssets(ProjectAssets assets, WorldDefinition world) {
        this.loadBackground(assets.catalog, world.backgroundDef);

        this.loadImages(assets.catalog, world.asteroidsDef);
        this.loadImages(assets.catalog, world.bullets);
        this.loadImages(assets.catalog, world.spaceshipsDef);
        this.loadImages(assets.catalog, world.gravityBodiesDef);
        this.loadImages(assets.catalog, world.bombsDef);
        this.loadImages(assets.catalog, world.spaceDecoratorsDef);
    }


    public void loadBackground(AssetCatalog backs, BackgroundDto backDef) {
        this.background = Images.loadBufferedImage(
                backs.getPath(), backs.get(backDef.assetId).fileName);
    }


    public void loadImages(AssetCatalog catalog, List bodyDef) {
        String fileName;
        String assetId; 
        String path = catalog.getPath();
        AssetInfoDTO assetInfo;

        for (Object body : bodyDef) {
            assetId = ((VItemDto)body).assetId;
            assetInfo = catalog.get(assetId);

            if (assetInfo == null) {
                System.out.println("Resource info <" + assetId + "> not found in catalog");
            } else {

                fileName = catalog.get(assetId).fileName;
                this.images.add(assetId, path + fileName);
            }
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
        this.renderer.addKeyListener(this);

        this.pack();
        this.setVisible(true);
        SwingUtilities.invokeLater(() -> this.renderer.requestFocusInWindow());

    }


    /**
     * OVERRIDES
     */
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
            case KeyEvent.VK_W:
                this.controller.playerThrustOn(this.localPlayerId);
                break;

            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_X:
                this.controller.playerReverseThrust(this.localPlayerId);
                break;

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                this.controller.playerRotateLeftOn(this.localPlayerId);
                break;

            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                this.controller.playerRotateRightOn(this.localPlayerId);
                break;

            case KeyEvent.VK_SPACE:
                if (!this.fireKeyDown) {  // Discard autoreptition PRESS
                    this.fireKeyDown = true;
                    this.controller.playerFire(this.localPlayerId);
                }
                break;

            case KeyEvent.VK_1:
                this.controller.selectNextWeapon(this.localPlayerId);
                break;
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
            case KeyEvent.VK_A:
                this.controller.playerThrustOff(this.localPlayerId);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_Z:
                this.controller.playerThrustOff(this.localPlayerId);
                break;
            case KeyEvent.VK_LEFT:
                this.controller.playerRotateOff(this.localPlayerId);
                break;
            case KeyEvent.VK_RIGHT:
                this.controller.playerRotateOff(this.localPlayerId);
                break;
            case KeyEvent.VK_SPACE:
                fireKeyDown = false;                  // << permite el siguiente disparo
                break;
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
        // No usado
    }
}
