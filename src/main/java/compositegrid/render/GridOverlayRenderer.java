package compositegrid.render;

import compositegrid.coordinates.HexagonCoordinate;
import compositegrid.coordinates.TriangleCoordinate;
import compositegrid.coordinates.TriangleDirection;
import compositegrid.grid.CompositeGrid;
import compositegrid.grid.HexagonCell;
import compositegrid.grid.TriangleCell;
import compositegrid.ui.GridOverlayView;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

/**Shows vertex/edge/hexagon, as well as coordinate values.**/
public class GridOverlayRenderer<H, V, E>  extends AbstractGridRenderer<H, V, E> {
    private GridOverlayView debugView;
    private Font bigFont = new Font("Serif", Font.BOLD, 100);
    private Font smallFont = new Font("Serif", Font.BOLD, 15);
    private float[] dashPattern = {5, 5};
    private Stroke dashedStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, dashPattern, 0);
    private Object selection;

    public GridOverlayRenderer(CompositeGrid<H, V, E> grid, int canvasWidthPixels, int canvasHeightPixels, double edgeLength, int vertexDiameter, double edgeThicknessDebugView, GridOverlayView debugView) {
        super(grid, canvasWidthPixels, canvasHeightPixels, edgeLength, vertexDiameter, edgeThicknessDebugView);
        this.debugView = debugView;
    }

    @Override
    protected void firstPaint(Graphics2D g2d) {

    }

    @Override
    protected void lastPaint(Graphics2D g2d) {

    }

    @Override
    protected void drawHexagon(Graphics2D g2d, HexagonCoordinate coordinate, HexagonCell<H, V, E> cell, Shape hexagon, Point2D vertexPosition, int passNumber) {
        if (debugView.displayHexagons()){
            if (cell != null && selection == cell.getHexagon())
                g2d.setColor(Color.red);
            else if (cell != null)
                //g2d.setColor(getColorFromSeed(cell.hashCode()));
            g2d.setColor(new Color(255, 255, 255, 10));
            else
                g2d.setColor(Color.black);

            Shape hexagonShape = constructHexagon(coordinate);
            g2d.fill(hexagonShape);
        }
        if (debugView.displayHexagonCoordinates()) {
            if (compositeGrid.isInBounds(coordinate))
                g2d.setColor(Color.black);
            else
                g2d.setColor(Color.red);
            g2d.setFont(bigFont);
            g2d.drawString(coordinate.x + "," + coordinate.y, (int) vertexPosition.getX() - 70, (int) vertexPosition.getY() + 30);
            //g2d.drawString(coordinate.hashCode()%1000+"",(int) vertexPosition.getX() - 70,(int) vertexPosition.getY() + 30);
        }
    }

    @Override
    protected void drawHexagonBorderEdge(Graphics2D g2d, TriangleCoordinate coordinate, E edge, TriangleDirection direction, Point2D lineStart, Point2D lineFinish, int passNumber) {
        if (debugView.displayHexagons()){
            if (edge != null && selection == edge)
                g2d.setColor(Color.red);
            else
                g2d.setColor(Color.black);
            Shape edgeShape = constructEdge(coordinate, direction);
            g2d.setStroke(new BasicStroke(2));
            g2d.fill(edgeShape);
        }

    }

    @Override
    protected void drawEdge(Graphics2D g2d, TriangleCoordinate coordinate, Object edge, TriangleDirection direction, Point2D lineStart, Point2D lineFinish, int passNumber) {
        if (edge != null && selection == edge)
            g2d.setColor(Color.red);
        else
            g2d.setColor(Color.black);

        if (debugView.displayEdges()){
                g2d.setStroke(dashedStroke);
                g2d.drawLine((int) lineStart.getX(), (int) lineStart.getY(), (int) lineFinish.getX(), (int) lineFinish.getY());
        }
    }

    @Override
    protected void drawVertices(Graphics2D g2d, TriangleCoordinate coordinate, TriangleCell<V, E> cell, Point2D vertexPosition, int z) {
        if (debugView.displayVertices()){
            if (cell != null && selection == cell.getVertex())
                g2d.setColor(Color.red);
            else
                g2d.setColor(Color.white);

            Shape vertexShape = constructVertex(coordinate);
            g2d.fill(vertexShape);
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.black);
            g2d.draw(vertexShape);
        }

        if (debugView.displayTriangleCoordinates()) {
            if (compositeGrid.getTriangleGrid().isVertexInBounds(coordinate))
                g2d.setColor(Color.black);
            else
                g2d.setColor(Color.red);
            g2d.setFont(smallFont);
            g2d.drawString(coordinate.x + "," + coordinate.y, (int) vertexPosition.getX() - 8, (int) vertexPosition.getY() + 5);
            //g2d.drawString(coordinate.hashCode()%10000+"",(int) vertexPosition.getX() - 70,(int) vertexPosition.getY() + 30);

        }
    }

    @Override
    protected void drawHexagonCentreVertice(Graphics2D g2d, TriangleCoordinate coordinate, TriangleCell<V, E> cell, Point2D vertexPosition, int z) {

    }

    public static Color getColorFromSeed(int seed) {
        Random rand = new Random(seed);
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        return new Color(r, g, b, 255);
    }

    public void setSelection(Object selection) {
        this.selection = selection;
    }
}
