package proceduralrails.track.path;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/** Defines the geometry along a section of straight track */
public class StraightTrackPath implements TrackPath {
    private final Point2D start;
    private final Point2D end;
    private final double dx;
    private final double dy;
    private final double length;
    private final double angle; // radians

    public StraightTrackPath(Point2D start, Point2D end) {
        this.start = start;
        this.end = end;
        dx = end.getX() - start.getX();
        dy = end.getY() - start.getY();
        length = Math.hypot(dx, dy);
        angle = Math.atan2(dy, dx);
    }

    @Override
    public Point2D getPoint(double t) {
        return new Point2D.Double(
                start.getX() + dx * t,
                start.getY() + dy * t
        );
    }

    @Override
    public double getAngle(double t) {
        return angle + Math.PI / 2;
    }

    @Override
    public double getPathLength() {
        return length;
    }

    @Override
    public TrackPath offset(double perpendicularOffset) {
        double offsetX = -Math.sin(angle) * perpendicularOffset;
        double offsetY =  Math.cos(angle) * perpendicularOffset;

        return new StraightTrackPath(
                new Point2D.Double(start.getX() + offsetX, start.getY() + offsetY),
                new Point2D.Double(end.getX() + offsetX,   end.getY() + offsetY));
    }

    @Override
    public Shape createPath(double thickness) {
        Path2D path = new Path2D.Double();
        path.moveTo(start.getX(), start.getY());
        path.lineTo(end.getX(), end.getY());
        return new BasicStroke((float) thickness).createStrokedShape(path);
    }

    @Override
    public Shape createPath(double fractionStart, double fractionStop, double thickness) {
        Point2D s = getPoint(fractionStart);
        Point2D e = getPoint(fractionStop);
        Path2D path = new Path2D.Double();
        path.moveTo(s.getX(), s.getY());
        path.lineTo(e.getX(), e.getY());
        return new BasicStroke((float) thickness).createStrokedShape(path);
    }
}