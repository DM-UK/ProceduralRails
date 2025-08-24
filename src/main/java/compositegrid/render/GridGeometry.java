package compositegrid.render;

import compositegrid.coordinates.FractionalTriangleCoordinate;
import compositegrid.coordinates.TriangleCoordinate;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/** Provides geometric computations for a tri/hex grid */
public class GridGeometry {
    private final double edgeLength;
    private final double halfEdgeLength;
    private final double triangleHeight;
    private final Point2D[] vertices;
    private final Path2D hexagon;

    public GridGeometry(double edgeLength) {
        this.edgeLength = edgeLength;
        this.halfEdgeLength = edgeLength / 2.0;
        this.triangleHeight = Math.sqrt(3) * edgeLength / 2.0;
        this.vertices = createVertices();
        this.hexagon = createHexagon();
    }

    /** Convert from screen space to grid coordinates */
    public FractionalTriangleCoordinate gridPosition(double screenX, double screenY) {
        double gridB = screenY / triangleHeight;
        double gridA = (screenX - (gridB * halfEdgeLength)) / edgeLength;
        return new FractionalTriangleCoordinate(gridA, gridB, -gridA - gridB);
    }

    /** Convert from grid coordinates to screen space */
    public Point2D screenPosition(TriangleCoordinate triCoordinate) {
        double x = edgeLength * triCoordinate.x + halfEdgeLength * triCoordinate.y;
        double y = triangleHeight * triCoordinate.y;
        return new Point2D.Double(x, y);
    }

    /** Convert from grid coordinates to screen space */
    public Point2D screenPosition(FractionalTriangleCoordinate triCoordinate) {
        double x = edgeLength * triCoordinate.x + halfEdgeLength * triCoordinate.y;
        double y = triangleHeight * triCoordinate.y;
        return new Point2D.Double(x, y);
    }

    /** The 6 vertices of a hexagon (origin is hexagon centre) */
    public Point2D[] getVertices() {
        return vertices;
    }

    public double getTriangleHeight() {
        return triangleHeight;
    }

    /** Edge length of a triangle/hexagon side  */
    public double getEdgeLength() {
        return edgeLength;
    }

    /** Hexagon path (origin is hexagon centre) */
    public Path2D getHexagon() {
        return hexagon;
    }

    private Point2D[] createVertices() {
        return new Point2D.Double[]{
                new Point2D.Double(-edgeLength, 0),
                new Point2D.Double(-halfEdgeLength, -triangleHeight),
                new Point2D.Double(+halfEdgeLength, -triangleHeight),
                new Point2D.Double(+edgeLength, 0),
                new Point2D.Double(+halfEdgeLength, +triangleHeight),
                new Point2D.Double(-halfEdgeLength, +triangleHeight),
        };
    }

    private Path2D createHexagon() {
        //int to prevent rounding errors?
        Path2D path = new Path2D.Double();
        path.moveTo((int)Math.round(vertices[0].getX()), (int)Math.round(vertices[0].getY()));

        for  (int i = 1; i < 6; i++)
            path.lineTo((int)Math.round(vertices[i].getX()), (int)Math.round(vertices[i].getY()));

        path.closePath();
        return path;
    }
}
