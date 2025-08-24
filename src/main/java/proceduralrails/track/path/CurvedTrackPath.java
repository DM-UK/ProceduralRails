package proceduralrails.track.path;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

/** Defines the geometry along a section of curved track */
public class CurvedTrackPath implements TrackPath {
    private final Point2D trackStart;
    private final Point2D trackEnd;
    private final Point2D centre;
    private final double radius;
    private final double startAngle; // radians
    private final double extent;     // radians

    public CurvedTrackPath(Point2D trackStart, Point2D trackEnd, Point2D centre, double radius, double startAngle, double extent) {
        this.trackStart = trackStart;
        this.trackEnd = trackEnd;
        this.centre = centre;
        this.radius = radius;
        this.startAngle = startAngle;
        this.extent = extent;
    }

    @Override
    public Point2D getPoint(double t) {
        double angle = getAngle(t);
        double x = centre.getX() + radius * Math.cos(angle);
        double y = centre.getY() + radius * Math.sin(angle);
        return new Point2D.Double(x, y);
    }

    @Override
    public double getAngle(double t) {
        return startAngle + t * extent;
    }

    @Override
    public double getPathLength() {
        //arc perimeter
        double ratio = Math.abs(extent) / (2 * Math.PI);
        double circumference = 2 * Math.PI * radius;
        return circumference * ratio;
    }

    @Override
    public TrackPath offset(double perpendicularOffset) {
        return new CurvedTrackPath(trackStart, trackEnd, centre, radius + perpendicularOffset, startAngle, extent);
    }

    @Override
    public Shape createPath(double thickness) {
        Arc2D arc = createCircleArc(centre, radius, -getAngle(0), -extent);
        return new BasicStroke((float) thickness).createStrokedShape(arc);
    }

    @Override
    public Shape createPath(double fractionStart, double fractionStop, double thickness) {
        double scaledExtent = (fractionStop - fractionStart) * extent;
        Arc2D arc = createCircleArc(centre, radius, -getAngle(fractionStart), -scaledExtent);
        return new BasicStroke((float) thickness).createStrokedShape(arc);
    }

    public static Arc2D createCircleArc(Point2D centre, double arcRadius, double startAngle, double extent) {
        double startDegrees = Math.toDegrees(startAngle);
        double extentDegrees = Math.toDegrees(extent);
        return new Arc2D.Double(centre.getX() - arcRadius, centre.getY() - arcRadius, arcRadius * 2, arcRadius * 2, startDegrees, extentDegrees, Arc2D.OPEN);
    }

    public Point2D getCentre() {
        return centre;
    }

    public double getRadius() {
        return radius;
    }

    /** Helper method to create a CurvedTrackPath instance without knowing the radius and startAngle */
    public static CurvedTrackPath fromEndpoints(Point2D trackStart, Point2D trackEnd, Point2D arcCentre, int direction, double extent, double thickness) {
        double edgeDistance = trackStart.distance(trackEnd);
        double radius = edgeDistance * Math.sqrt(3); //circle inscribed inside a hexagon
        double deltaX = trackStart.getX() - arcCentre.getX();
        double deltaY = trackStart.getY() - arcCentre.getY();
        double startAngle = Math.atan2(deltaY, deltaX);
        return new CurvedTrackPath(trackStart, trackEnd, arcCentre, radius, startAngle, extent * direction);
    }
}