package midpointdisplacement;

import midpointdisplacement.compositecurve.BezierCurve;
import midpointdisplacement.compositecurve.CompositeBezierCurve;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

/** Path2D with functionality to generate displaced lines from points */
public class MidpointDisplacedPath extends Path2D.Double {
    public static final int STRAIGHT_EDGED = 0;
    public static final int COMPOSITE_BEZIER_CURVE = 1;

    private int edgeConnectionType;
    private MidpointDisplacement midpointDisplacement;
    private Random rng;

    public MidpointDisplacedPath(int steps, int maximumDisplacement, double roughness, int seed, int edgeConnectionType) {
        this(new MidpointDisplacement(steps, maximumDisplacement, roughness), edgeConnectionType, seed);
    }

    public MidpointDisplacedPath(MidpointDisplacement midpointDisplacement, int edgeConnectionType, int rngSeed) {
        if (midpointDisplacement == null | edgeConnectionType > 1) {
            throw new IllegalArgumentException("Invalid Arguments");
        }
        this.midpointDisplacement = midpointDisplacement;
        this.edgeConnectionType = edgeConnectionType;
        this.rng = new Random(rngSeed);
    }

    /** Adds points to the path by drawing lines from current point to the displaced midpoints */
    public void displacedLineTo(double x, double y){
        if (edgeConnectionType == STRAIGHT_EDGED)
            midpointDisplacedStraightLineTo(x, y);
        else
            midpointDisplacedCurveLineTo(x, y);
    }

    private void midpointDisplacedStraightLineTo(double x, double y){
        Point2D from = getCurrentPoint();
        Point2D to = new Point2D.Double(x, y);
        List<Point2D> points = midpointDisplacement.generateMidpoints(from, to, rng.nextInt());
        for (Point2D p : points) {
            super.lineTo(p.getX(), p.getY());
        }
    }

    private void midpointDisplacedCurveLineTo(double x, double y){
        Point2D from = getCurrentPoint();
        Point2D to = new Point2D.Double(x, y);
        List<Point2D> points = midpointDisplacement.generateMidpoints(from, to, rng.nextInt());
        CompositeBezierCurve curvedPoints = new CompositeBezierCurve(points);

        for (BezierCurve curve : curvedPoints.getCurves()) {
            curveTo(curve.control1.x, curve.control1.y,
                    curve.control2.x, curve.control2.y,
                    curve.finish.x, curve.finish.y);
        }
    }
}