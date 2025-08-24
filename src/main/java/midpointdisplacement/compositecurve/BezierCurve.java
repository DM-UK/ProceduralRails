package midpointdisplacement.compositecurve;

import java.awt.geom.Point2D;

/**
 * Represents a cubic Bézier curve segment defined by four control points.
 * A Bézier curve is defined by:
 * - A start point
 * - An end point
 * - Two control points that influence the curvature
 */
public class BezierCurve {
    public final Point2D.Double start;
    public final Point2D.Double finish;
    public final Point2D.Double control1;
    public final Point2D.Double control2;

    public BezierCurve(Point2D.Double start, Point2D.Double finish, Point2D.Double control1, Point2D.Double control2) {
        this.start = start;
        this.finish = finish;
        this.control1 = control1;
        this.control2 = control2;
    }
}
