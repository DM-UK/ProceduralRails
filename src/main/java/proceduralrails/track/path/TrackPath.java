package proceduralrails.track.path;

import java.awt.*;
import java.awt.geom.Point2D;

/** Defines the geometry along a section of track */
public interface TrackPath {
    Point2D getPoint(double t);
    double getAngle(double t);
    double getPathLength();
    TrackPath offset(double perpendicularOffset);
    Shape createPath(double thickness);
    Shape createPath(double fractionStart, double fractionStop, double thickness);
}
