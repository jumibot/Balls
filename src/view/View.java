package view;


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
import java.util.ArrayList;
import javax.swing.JFrame;


public class View extends JFrame implements MouseWheelListener, ActionListener, ComponentListener {

    private Controller controller = null;
    private final ControlPanel controlPanel;
    private final Renderer renderer;

    private final Dimension worldDimension;


    /**
     * CONSTRUCTOR
     */
    public View(Dimension worldDim) {
        this.worldDimension = worldDim;

        this.controlPanel = new ControlPanel(this);

        this.renderer = new Renderer(this, this.worldDimension);
        this.createFrame();
    }


    /**
     * PUBLIC
     */
    public void activate() {
        this.renderer.activate();
    }


    public void setController(Controller controller) {
        this.controller = controller;
    }


    /**
     * PROTECTED
     */
    protected ArrayList<RenderInfoDTO> getRenderInfo() {
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
        container.add(this.renderer, c);
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
