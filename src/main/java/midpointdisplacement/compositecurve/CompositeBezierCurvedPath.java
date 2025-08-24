package midpointdisplacement.compositecurve;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Constructs a smooth curve on a Path2D connecting multiple Bézier curves built from a CompositeBezierCurve
 */
public class CompositeBezierCurvedPath extends Path2D.Double{
    /** Constructs a composite Bézier curve path using the given list of points. */
    public CompositeBezierCurvedPath(List<Point2D> mainPoints){
        if (mainPoints == null || mainPoints.size() < 2) {
            throw new IllegalArgumentException("Point list cannot be null or fewer than 2 points");
        }

        // Move to the first point in the path
        Point2D firstPoint = mainPoints.getFirst();
        moveTo(firstPoint.getX(), firstPoint.getY());
        // Add Bézier curve segments
        addBezierCurves(mainPoints);
    }

    public void compositeCurveTo(double x, double y){
        //TODO!
    }

    private void addBezierCurves(List<Point2D> mainPoints) {
        CompositeBezierCurve compositeBezierCurve = new CompositeBezierCurve(mainPoints);

        for (BezierCurve curve: compositeBezierCurve.getCurves()){
            // Append a Bézier curve segment to this path
            curveTo(curve.control1.x, curve.control1.y,
                    curve.control2.x, curve.control2.y,
                    curve.finish.x, curve.finish.y);
        }
    }
}
