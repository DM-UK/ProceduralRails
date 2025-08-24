package compositegrid.ui;

import compositegrid.coordinates.*;
import compositegrid.grid.CompositeGrid;
import compositegrid.render.AbstractGridRenderer;
import compositegrid.render.GridOverlayRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/** A panel implementation that encapsulates the rendering and mouse input of a CompositeGrid.
 *  Render logic is delegated to the provided AbstractGridRenderer.
 *  Grid selection (mouse clicks) events can be subscribed to by the addGridSelectionListener() method.
 * */
public class HexagonGridPane extends JLayeredPane {
    private final static double DEFAULT_SCROLL_SPEED = 0.035;
    private final static int DEFAULT_SCREEN_EDGE_THRESHOLD = 100;
    private final static int DEFAULT_VERTEX_DIAMETER = 30;
    private final static double DEFAULT_EDGE_THICKNESS = 4;
    private final static double DEFAULT_EDGE_LENGTH = 90;

    protected double scrollSpeed = DEFAULT_SCROLL_SPEED;
    protected int edgeThreshold = DEFAULT_SCREEN_EDGE_THRESHOLD;
    protected double edgeLength = DEFAULT_EDGE_LENGTH;
    protected int vertexDiameter = DEFAULT_VERTEX_DIAMETER;
    protected double edgeThickness = DEFAULT_EDGE_THICKNESS;

    protected AbstractGridRenderer renderer;
    private EdgeScrollListener edgeScrollListener = new EdgeScrollListener();;
    private SelectionMouseAdapter selectionMouseAdapter = createSelectionListener();

    //Grid overlay shows vertex/edge/hexagon, as well as coordinate values.
    private GridOverlayRenderer overlayRenderer;
    private GridOverlayBar gridOverlayBar = new GridOverlayBar();
    private ArrayList<GridSelectionListener> gridSelectionListeners = new ArrayList();

    private int canvasWidth;
    private int canvasHeight;
    private boolean disableScrolling;

    public HexagonGridPane(int canvasWidth, int canvasHeight, CompositeGrid grid, AbstractGridRenderer renderer) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.renderer = renderer;

        if (renderer != null)
            this.edgeLength = renderer.getEdgeLength();

        overlayRenderer = new GridOverlayRenderer(grid, canvasWidth, canvasHeight, edgeLength, vertexDiameter, edgeThickness, gridOverlayBar);

        //if no renderer is provided make the renderer the overlay one
        if (renderer == null)
            this.renderer = overlayRenderer;

        //ensure renderer is the same size as this panel
        this.renderer.setCanvasWidth(canvasWidth);
        this.renderer.setCanvasHeight(canvasHeight);

        setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        addMouseListener(edgeScrollListener);
        addMouseMotionListener(edgeScrollListener);
        addMouseListener(selectionMouseAdapter);
        add(gridOverlayBar);
        setCameraPosition(new FractionalTriangleCoordinate(3, 3, -6));
        setLayout(null);
        setOpaque(false);
        gridOverlayBar.setBounds(0, 0, canvasWidth, 25);
    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, canvasWidth, canvasHeight);
        renderer.render(g2d);
        overlayRenderer.render(g2d);
    }

    protected SelectionMouseAdapter createSelectionListener() {
        return new SelectionMouseAdapter();
    }

    public void addGridSelectionListener(GridSelectionListener listener) {
        gridSelectionListeners.add(listener);
    }

    private void notifyGridSelectionListenersOfGridSelection(FractionalTriangleCoordinate coordinate) {
        for (GridSelectionListener listener: gridSelectionListeners)
            listener.gridSelected(coordinate);
    }

    private void notifyGridSelectionListenersOfVertexSelection(TriangleCoordinate coordinate){
        for (GridSelectionListener listener: gridSelectionListeners)
            listener.vertexSelected(coordinate);
    }

    private void notifyGridSelectionListenersOfEdgeSelection(TriangleCoordinate coordinate, TriangleDirection direction){
        for (GridSelectionListener listener: gridSelectionListeners)
            listener.edgeSelected(coordinate, direction);
    }

    private void notifyGridSelectionListenersOfHexagonSelection(HexagonCoordinate coordinate){
        for (GridSelectionListener listener: gridSelectionListeners)
            listener.hexagonSelected(coordinate);
    }

    private void setCameraPosition(FractionalTriangleCoordinate coordinate) {
        renderer.setCameraPosition(coordinate);
        overlayRenderer.setCameraPosition(coordinate);
    }

    private FractionalTriangleCoordinate getCameraPosition() {
        return renderer.getCameraPosition();
    }

    public GridOverlayRenderer getOverlayRenderer() {
        return overlayRenderer;
    }

    public AbstractGridRenderer getRenderer() {
        return renderer;
    }

    public void disableScrolling() {
        disableScrolling = true;
    }
    
    public void enableScrolling() {
        disableScrolling = false;
    }
    public class SelectionMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            FractionalTriangleCoordinate coordinate = renderer.screenPixelToGrid(e.getX(), e.getY());
            notifyGridSelectionListenersOfGridSelection(coordinate);

            if (checkForVertexClick(coordinate, e.getPoint())) {
                repaint();
                return;
            }
            if (checkForEdgeClick(coordinate, e.getPoint())) {
                repaint();
                return;
            }
            if (checkForHexagonClick(coordinate, e.getPoint())) {
                repaint();
                return;
            }
            repaint();
        }


        private boolean checkForVertexClick(FractionalTriangleCoordinate coordinate, Point mouseClick) {
            TriangleCoordinate nearestVertex = coordinate.rounded();
            Shape vertexShape = renderer.constructVertex(nearestVertex);

            if (renderer.getCompositeGrid().getTriangleGrid().isVertexInBounds(nearestVertex))
                if (vertexShape.contains(mouseClick)){
                    notifyGridSelectionListenersOfVertexSelection(nearestVertex);
                    return true;
                }

            return false;
        }

        private boolean checkForEdgeClick(FractionalTriangleCoordinate coordinate, Point mouseClick) {
            TriangleCoordinate nearestVertex = coordinate.rounded();

            for (TriangleDirection direction: TriangleDirection.values()){
                if (renderer.getCompositeGrid().getTriangleGrid().isEdgeInBounds(nearestVertex, direction)){
                    Shape vertexShape = renderer.constructEdge(nearestVertex, direction);

                    if (vertexShape.contains(mouseClick)) {
                        notifyGridSelectionListenersOfEdgeSelection(nearestVertex, direction);
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean checkForHexagonClick(FractionalTriangleCoordinate coordinate, Point mouseClick) {
            HexagonCoordinate nearestHexagon = coordinate.convert().rounded();
            Shape nearestHexagonShape = renderer.constructHexagon(nearestHexagon);

            if (renderer.getCompositeGrid().isInBounds(nearestHexagon)) {
                if (nearestHexagonShape.contains(mouseClick)) {
                    notifyGridSelectionListenersOfHexagonSelection(nearestHexagon);
                    return true;
                }
            }

            for (HexagonDirection direction: HexagonDirection.values()){
                HexagonCoordinate neighbourHexagon = nearestHexagon.direction(direction);

                if (renderer.getCompositeGrid().isInBounds(nearestHexagon)) {
                    Shape neighbourHexagonShape = renderer.constructHexagon(neighbourHexagon);

                    if (neighbourHexagonShape.contains(mouseClick)) {
                        notifyGridSelectionListenersOfHexagonSelection(neighbourHexagon);
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public class EdgeScrollListener extends MouseAdapter
    {
        private FractionalTriangleCoordinate horizontalScrollSpeed = FractionalTriangleCoordinate.fromOffset(scrollSpeed, 0);
        private FractionalTriangleCoordinate verticalScrollSpeed = FractionalTriangleCoordinate.fromOffset(0, scrollSpeed);
        private Point point;

        public EdgeScrollListener()
        {
            new Timer(20, a -> scroll()).start();
        }
        @Override
        public void mouseMoved(MouseEvent e) {
            point = e.getPoint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            point = e.getPoint();
        }

        @Override
        public void mouseExited(MouseEvent e){
            point = null;
        }

        private void scroll() {
            if (point == null || disableScrolling)
                return;

            FractionalTriangleCoordinate newPosition = getCameraPosition();
            boolean update = false;

            if (point.x < edgeThreshold){
                newPosition = newPosition.subtract(horizontalScrollSpeed);
                update = true;
            }
            else if (point.x > canvasWidth - edgeThreshold){
                newPosition = newPosition.add(horizontalScrollSpeed);
                update = true;
            }

            if (point.y < edgeThreshold){
                newPosition = newPosition.subtract(verticalScrollSpeed);
                update = true;
            }
            else if (point.y > canvasHeight - edgeThreshold){
                newPosition = newPosition.add(verticalScrollSpeed);
                update = true;
            }

            if (update){
                setCameraPosition(newPosition);
                repaint();
            }
        }
    }
}
