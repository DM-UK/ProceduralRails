package proceduralrails;

import proceduralrails.animations.TrackAnimationCreator;
import proceduralrails.animations.TrackAnimator;
import compositegrid.ui.HexagonGridPane;
import proceduralrails.grid.ObservableTrackGrid;
import proceduralrails.render.GridRenderer;
import proceduralrails.render.TrackEdgeProvider;
import proceduralrails.ui.AnimationControlsForm;
import proceduralrails.ui.RenderOptionsForm;
import proceduralrails.ui.TrackConfigForm;

import javax.swing.*;
import java.awt.*;

public class ProceduralRailsDemo {
    //grid
    private ObservableTrackGrid trackGrid = new ObservableTrackGrid(100, 100);
    private GridSelectionHandler gridSelectionHandler = new GridSelectionHandler(trackGrid);

    //swing/ui
    private JFrame frame = new JFrame("Procedural Rails");
    private TrackConfigForm trackConfigForm = new TrackConfigForm();
    private AnimationControlsForm animationControlsForm = new AnimationControlsForm();
    private RenderOptionsForm renderOptionsForm = new RenderOptionsForm();

    //track
    private TrackEdgeProvider trackEdgeProvider = new TrackEdgeProvider(trackGrid, trackConfigForm);

    //renderer
    private GridRenderer gridRenderer = new GridRenderer(trackGrid, 1200, 1000, 100, 0, 27, trackEdgeProvider, renderOptionsForm);

    //more swing
    private HexagonGridPane pane = new HexagonGridPane(1200, 1000, trackGrid, gridRenderer);

    //animation
    private TrackAnimationCreator animationCreator = new TrackAnimationCreator(animationControlsForm);
    private TrackAnimator animator = new TrackAnimator(pane, trackEdgeProvider, animationCreator);

    public ProceduralRailsDemo(){
        buildGUI();
        startRenderLoop();
        //controller for edge clicks
        pane.addGridSelectionListener(gridSelectionHandler);
        //subscribe to the grid to create animations
        trackGrid.addListener(animator);
        //render animations in loop
        gridRenderer.setAnimator(animator);
        //set screen bounds for rail removal animation
        animationCreator.setScreenBounds(0, 0, pane.getWidth(), pane.getHeight());
    }

    private void buildGUI() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        JPanel sidebarWrapper = new JPanel();
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.add(trackConfigForm);
        sidebar.add(animationControlsForm);
        sidebar.add(renderOptionsForm);

        sidebarWrapper.add(sidebar);
        mainContainer.add(sidebarWrapper, BorderLayout.WEST);
        mainContainer.add(pane, BorderLayout.CENTER);

        frame.add(mainContainer);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    private void startRenderLoop() {
        int delay = 1000 / 60;
        Timer timer = new Timer(delay, e -> pane.repaint());
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProceduralRailsDemo());
    }
}