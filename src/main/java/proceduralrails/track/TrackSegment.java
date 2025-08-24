package proceduralrails.track;

import midpointdisplacement.MidpointDisplacedPath;
import midpointdisplacement.MidpointDisplacement;
import proceduralrails.track.path.StraightTrackPath;
import proceduralrails.track.path.TrackPath;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/** A segment of track that is responsible for the creating and containing sleeper, rail and trackbed Shape objects through a given TrackPath */
public class TrackSegment implements Track{
    private final Shape[] rails;
    private final Shape[] sleepers;
    private final Shape trackbed;
    private final TrackPath centralPath;
    private final TrackPath[] railPaths;
    private final TrackConfig trackConfig;
    private final int seed;//for unique random properties of each segment, currently only used in constructing a trackbed

    public TrackSegment(TrackPath centralPath, TrackConfig trackConfig, int seed) {
        this.centralPath = centralPath;
        this.trackConfig = trackConfig;
        this.seed = seed;
        RailBuilder railBuilder = new RailBuilder();
        this.railPaths = railBuilder.railsPaths;
        this.rails = railBuilder.rails;
        this.sleepers = new SleeperBuilder().buildSleepers();
        this.trackbed = new TrackbedBuilder().buildTrackbed();
    }

    @Override
    public Shape[] getRails() { return rails; }

    @Override
    public Shape[] getSleepers() { return sleepers; }

    @Override
    public Shape getTrackbed() {
        return trackbed;
    }

    public TrackPath getCentralPath() {
        return centralPath;
    }

    public int getSeed() {
        return seed;
    }

    public TrackConfig getTrackConfig() {
        return trackConfig;
    }

    public TrackPath[] getRailPaths() {
        return railPaths;
    }

    public class RailBuilder {
        private final TrackPath[] railsPaths;
        private final Shape[] rails;

        public RailBuilder(){
            int n = trackConfig.getNumberOfRails();
            railsPaths = new TrackPath[n];
            rails = new Shape[n];

            for (int i = 0; i < n; i++) {
                //each rail is the same as the central path +/- a given offset
                railsPaths[i] = centralPath.offset(trackConfig.getRailOffset(i));
                //overlap tweak to avoid gaps between segments
                rails[i] = railsPaths[i].createPath(-0.025, 1.025, trackConfig.getRailThickness());
            }
        }
    }

    public class SleeperBuilder {
        //interpolate each sleeper
        public Shape[] buildSleepers() {
            int n = (int) Math.ceil(centralPath.getPathLength() / trackConfig.getMaximumSleeperSpacing());
            Shape[] sleepers = new Shape[n];

            for (int i = 0; i < n; i++) {
                double t = (double) i / (n - 1);
                Point2D p = centralPath.getPoint(t);
                double angle = centralPath.getAngle(t);
                sleepers[i] = createRotatedRectangle(p, angle, trackConfig.getSleeperWidth(), trackConfig.getSleeperLength());
            }

            return sleepers;
        }

        private static Path2D createRotatedRectangle(Point2D centre, double angle, double width, double length) {
            Rectangle2D rect = new Rectangle2D.Double(-length/2, -width/2, length, width);
            Path2D path = new Path2D.Double(rect);

            AffineTransform transform = new AffineTransform();
            transform.translate(centre.getX(), centre.getY());
            transform.rotate(angle);

            return (Path2D) path.createTransformedShape(transform);
        }
    }

    public class TrackbedBuilder {
        private MidpointDisplacement midpointDisplacement = new MidpointDisplacement(2, 9, 1);
        private MidpointDisplacedPath trackbedPath = new MidpointDisplacedPath(midpointDisplacement, MidpointDisplacedPath.COMPOSITE_BEZIER_CURVE, getSeed());

        public Shape buildTrackbed() {
            double maxSegLength = trackConfig.getTrackbedMaxSegmentLength();
            double halfWidth = trackConfig.getTrackbedWidth() / 2.0;

            //left/right border
            TrackPath leftEdge = centralPath.offset( halfWidth);
            TrackPath rightEdge = centralPath.offset(-halfWidth);

            Point2D leftStart = leftEdge.getPoint(-trackConfig.getTrackbedEndWidthFactor());
            Point2D leftEnd  = leftEdge.getPoint(1 + trackConfig.getTrackbedEndWidthFactor());
            Point2D rightStart = rightEdge.getPoint(-trackConfig.getTrackbedEndWidthFactor());
            Point2D rightEnd = rightEdge.getPoint(1 + trackConfig.getTrackbedEndWidthFactor());

            //top/bottom border
            TrackPath topEdge = new StraightTrackPath(leftEnd, rightEnd);
            TrackPath bottomEdge = new StraightTrackPath(leftStart, rightStart);

            trackbedPath.moveTo(leftStart.getX(), leftStart.getY());

            //left -> top -> right -> bottom
            //right and bottom in reverse to preserve line drawing continuity
            addDisplacedLine(leftEdge, maxSegLength, false);
            addDisplacedLine(topEdge, maxSegLength, false);
            addDisplacedLine(rightEdge, maxSegLength, true);
            addDisplacedLine(bottomEdge, maxSegLength, true);
            return trackbedPath;
        }

        private void addDisplacedLine(TrackPath track, double maximumSegmentLength, boolean reversed) {
            int numSegments = (int) Math.ceil(track.getPathLength() / maximumSegmentLength);

            for (int i = 1; i <= numSegments; i++) {
                double t = (double) i / numSegments;
                if (reversed)
                    t = 1.0 - t;

                Point2D p = track.getPoint(t);
                trackbedPath.displacedLineTo(p.getX(), p.getY());
            }
        }
    }
}
