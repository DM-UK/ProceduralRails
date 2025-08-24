package compositegrid.render;

import compositegrid.coordinates.*;
import compositegrid.grid.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public abstract class AbstractGridRenderer<H, V, E> {
    protected final CompositeGrid<H, V, E> compositeGrid;
    protected final HexagonGrid<H> hexagonGrid;//store seperately if we only need to expose the hexagon grid functionality
    protected final TriangleGrid<V, E> triangleGrid;//store seperately if we only need to expose the triangle grid functionality

    protected FractionalTriangleCoordinate cameraPosition;
    protected final GridGeometry geometry;
    protected int canvasWidthPixels;
    protected int canvasHeightPixels;
    //vertex width for rendering/mouse click detection
    protected final int vertexDiameter;
    //edge thickness for rendering/mouse click detection
    protected final double edgeThickness;

    public AbstractGridRenderer(CompositeGrid<H, V, E> grid, int canvasWidthPixels, int canvasHeightPixels, double edgeLength, int vertexDiameter, double edgeThickness) {
        this.vertexDiameter = vertexDiameter;
        this.edgeThickness = edgeThickness;
        this.compositeGrid = grid;
        this.triangleGrid = grid;
        this.hexagonGrid = grid;
        this.canvasWidthPixels = canvasWidthPixels;
        this.canvasHeightPixels = canvasHeightPixels;
        this.geometry = new GridGeometry(edgeLength);
    }

    protected abstract void firstPaint(Graphics2D g2d);
    protected abstract void lastPaint(Graphics2D g2d);
    /** Draws a hexagon at the provided coordinate. HexagonCell will be null it out of bounds   */
    protected abstract void drawHexagon(Graphics2D g2d, HexagonCoordinate coordinate, HexagonCell<H, V, E> cell, Shape hexagonShape, Point2D vertexPosition, int passNumber);
    /** Draws a edge at the provided coordinate. E will be null it out of bounds   */
    protected abstract void drawEdge(Graphics2D g2d, TriangleCoordinate coordinate, E edge, TriangleDirection direction, Point2D lineStart, Point2D lineFinish, int passNumber);
    /** Draws a hexagon edge at the provided coordinate. E will be null it out of bounds   */
    protected abstract void drawHexagonBorderEdge(Graphics2D g2d, TriangleCoordinate coordinate, E edge, TriangleDirection direction, Point2D lineStart, Point2D lineFinish, int passNumber);
    /** Draws a vertice at the provided coordinate. TriangleCell will be null it out of bounds   */
    protected abstract void drawVertices(Graphics2D g2d, TriangleCoordinate coordinate, TriangleCell<V, E> cell, Point2D vertexPosition, int z);
    /** Draws the vertice at the hexagon centre of the provided coordinate. TriangleCell will be null it out of bounds   */
    protected abstract void drawHexagonCentreVertice(Graphics2D g2d, TriangleCoordinate coordinate, TriangleCell<V,E> cell, Point2D vertexPosition, int z);

    public void render(Graphics2D g2d) {
        firstPaint(g2d);
        // top left/bottom right
        int minX = (int)(cameraPosition.toOffset().getX() - (canvasWidthPixels / geometry.getEdgeLength()) / 2) - 3;
        int maxX = (int)(cameraPosition.toOffset().getX() + (canvasWidthPixels / geometry.getEdgeLength()) / 2) + 3;
        int minY = (int)(cameraPosition.toOffset().getY() - (canvasHeightPixels / geometry.getTriangleHeight()) / 2) - 3;
        int maxY = (int)(cameraPosition.toOffset().getY() + (canvasHeightPixels / geometry.getTriangleHeight()) / 2) + 3;

        //iterate through every vertex on screen and call the drawing operation methods either directly or through the prepare() methods
        //looping through drawing operations preserves a visual z-ordering
        for (int z = 0; z < 3; z++){
            for (int drawingOperation = 0; drawingOperation < 4; drawingOperation++) {
                for (int y = minY; y < maxY; y++) {
                    for (int x = minX; x < maxX; x++) {
                        TriangleCell<V, E> triCell = null;
                        TriangleCoordinate triCoordinate = TriangleCoordinate.fromOffset(x, y);
                        Point2D vertexPosition = getCameraAdjustedScreenPosition(triCoordinate.toFractional());

                        if (compositeGrid.getTriangleGrid().isVertexInBounds(triCoordinate))
                            triCell = triangleGrid.getTriangleCell(triCoordinate);

                        switch (drawingOperation) {
                            case 0:
                                prepareHexagonDraw(g2d, triCoordinate, vertexPosition, z);
                                break;
                            case 1:
                                prepareDrawEdges(g2d, triCoordinate, triCell, vertexPosition, z);
                                break;
                            case 2:
                                drawVertices(g2d, triCoordinate, triCell, vertexPosition, z);
                                break;
                            case 3:
                                if (CoordinateUtils.fallsOnHexagonCentre(triCoordinate))
                                    drawHexagonCentreVertice(g2d, triCoordinate, triCell, vertexPosition, z);
                                break;
                        }
                    }
                }
            }
        }

        lastPaint(g2d);
    }

    /** Returns the screen position, from grid space, adjusted for camera  */
    public Point2D getCameraAdjustedScreenPosition(FractionalTriangleCoordinate coordinate) {
        Point2D cameraPixelOffset = getCameraPixelOffset();
        Point2D screenPosition = geometry.screenPosition(coordinate);

        return new Point2D.Double(
                screenPosition.getX() - cameraPixelOffset.getX(),
                screenPosition.getY() - cameraPixelOffset.getY()
        );
    }

    /** Represents total pixel offset to centre of screen  */
    public Point getCameraPixelOffset() {
        Point2D offset = geometry.screenPosition(cameraPosition);
        return new Point(
                (int) Math.round(offset.getX() - (canvasWidthPixels / 2.0)),
                (int) Math.round(offset.getY() - (canvasHeightPixels / 2.0))
        );
    }

    /** Returns the grid space, from screen position, adjusted for camera  */
    public FractionalTriangleCoordinate screenPixelToGrid(int screenX, int screenY) {
        var cameraPixelOffset = getCameraPixelOffset();
        return geometry.gridPosition(screenX + cameraPixelOffset.getX(), screenY + getCameraPixelOffset().getY());
    }

    private void prepareHexagonDraw(Graphics2D g2d, TriangleCoordinate triCoord, Point2D vertexPosition, int passNumber) {
        //only call if it falls on hexagon centre
        if (CoordinateUtils.fallsOnHexagonCentre(triCoord)) {
            HexagonCoordinate hexCoordinate = triCoord.convert();
            HexagonCell<H, V, E> hexCell = null;
            if (compositeGrid.isInBounds(hexCoordinate))
                hexCell = compositeGrid.getHexagonCell(hexCoordinate);

            drawHexagon(g2d, hexCoordinate, hexCell, constructHexagon(hexCoordinate), vertexPosition, passNumber);
        }
    }

    private void prepareDrawEdges(Graphics2D g2d, TriangleCoordinate coordinate, TriangleCell<V, E> cell, Point2D vertexPosition, int passNumber) {
        //only draw 3 edges
        for (int i = 0; i < 3; i++) {
            TriangleDirection direction = TriangleDirection.values()[i];
            Point2D lineFinish = new Point2D.Double(
                    vertexPosition.getX() + geometry.getVertices()[i].getX(),
                    vertexPosition.getY() + geometry.getVertices()[i].getY()
            );

            E edge = null;
            if (cell != null)
                edge = cell.getEdges()[i];

            drawEdge(g2d, coordinate, edge, direction, vertexPosition, lineFinish, passNumber);

            //might need to pull this to a drawing operation of its own if needed to preserve z-ordering
            if (CoordinateUtils.fallsOnHexagonBorder(coordinate, direction))
                drawHexagonBorderEdge(g2d, coordinate, edge, direction, vertexPosition, lineFinish, passNumber);
        }
    }

    /** Create a vertex shape (using this classes vertexDiameter) at the specified coordinate. Used in rendering and for mouse click detection */
    public Shape constructVertex(TriangleCoordinate coordinate) {
        Point2D p = getCameraAdjustedScreenPosition(coordinate.toFractional());
        return new Ellipse2D.Double(p.getX() - vertexDiameter / 2, p.getY() - vertexDiameter / 2, vertexDiameter, vertexDiameter);
    }

    /** Create a hexagon shape (using this classes edgeLength) at the specified coordinate. Used in rendering and for mouse click detection */
    public Shape constructHexagon(HexagonCoordinate coordinate) {
        Point2D p = getCameraAdjustedScreenPosition(coordinate.convert().toFractional());
        AffineTransform transform = AffineTransform.getTranslateInstance(p.getX(), p.getY());
        return transform.createTransformedShape(geometry.getHexagon());
    }

    /** Create an edge shape (using this classes edgeThickness) at the specified coordinate. Used in rendering and for mouse click detection */
    public Shape constructEdge(TriangleCoordinate coordinate, TriangleDirection direction) {
        Point2D edgeStart = getCameraAdjustedScreenPosition(coordinate.toFractional());
        Point2D edgeFinish = new Point2D.Double(
                edgeStart.getX() + geometry.getVertices()[direction.ordinal()].getX(),
                edgeStart.getY() + geometry.getVertices()[direction.ordinal()].getY()
        );

        Shape edge = new Line2D.Double(edgeStart.getX(), edgeStart.getY(), edgeFinish.getX(), edgeFinish.getY());
        return new BasicStroke((float) edgeThickness).createStrokedShape(edge);
    }

    public double getEdgeLength() {
        return  geometry.getEdgeLength();
    }

    public void setCanvasWidth(int canvasWidthPixels) {
        this.canvasWidthPixels = canvasWidthPixels;
    }

    public void setCanvasHeight(int canvasHeightPixels) {
        this.canvasHeightPixels = canvasHeightPixels;
    }

    public FractionalTriangleCoordinate getCameraPosition() {
        return cameraPosition;
    }


    public void setCameraPosition(FractionalTriangleCoordinate coordinate) {
        this.cameraPosition = coordinate;
    }

    public CompositeGrid<H, V, E> getCompositeGrid() {
        return compositeGrid;
    }

}
