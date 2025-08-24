package midpointdisplacement.compositecurve;

import midpointdisplacement.Vector2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a composite Bezier curve formed by multiple individual Bezier curves.
 * This class constructs smooth transitions between given points.
 *
 * The calculations for control points are based on the algorithm described at:
 * https://math.stackexchange.com/questions/2871559/formula-or-algorithm-to-draw-curved-lines-between-points/4207568#4207568
 */
public class CompositeBezierCurve {
    private final List<BezierCurve> curves = new ArrayList<>();

    /** Constructs a CompositeBezierCurve from a list of points.*/
    public CompositeBezierCurve(List<Point2D> mainPoints) {
        createCurves(mainPoints);
    }

    private void createCurves(List<Point2D> mainPoints) {
        //A
        Vector2D previousPoint = new Vector2D(mainPoints.get(0));
        //B
        Vector2D currentPoint = new Vector2D(mainPoints.get(1));

        //AB vector
        Vector2D controlPointVector1 = currentPoint.getSubtracted(previousPoint);

        for (int i = 2; i < mainPoints.size(); i++) {
            //C
            Vector2D nextPoint = new Vector2D(mainPoints.get(i));
            //AC vector
            Vector2D controlPointVector2 = nextPoint.getSubtracted(previousPoint);
            calculateControlPoints(previousPoint, currentPoint, controlPointVector1, controlPointVector2);

            //reassign paramaters for next iteration
            controlPointVector1 = controlPointVector2;
            previousPoint = currentPoint;
            currentPoint = nextPoint;
        }

        // Handle the last segment
        // (E, F, EF vector, EF vector)
        calculateControlPoints(previousPoint, currentPoint, controlPointVector1, controlPointVector1);
    }

    private void calculateControlPoints(Vector2D previousPoint, Vector2D currentPoint, Vector2D controlPointVector1, Vector2D controlPointVector2) {
        // 1/3 of AB added to point A
        Vector2D controlPoint1 = previousPoint.getAdded(controlPointVector1.getDivided(3));
        // 1/3 of AC subtracted from point B
        Vector2D controlPoint2 = currentPoint.getSubtracted(controlPointVector2.getDivided(3));
        BezierCurve curve = new BezierCurve(previousPoint, currentPoint, controlPoint1, controlPoint2);
        curves.add(curve);
    }

    /** Retrieves the list of Bezier curves that make up this composite curve. */
    public List<BezierCurve> getCurves() {
        return curves;
    }
}
