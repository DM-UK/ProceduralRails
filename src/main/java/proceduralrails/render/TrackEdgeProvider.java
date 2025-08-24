package proceduralrails.render;

import compositegrid.coordinates.TriangleCoordinate;
import compositegrid.coordinates.TriangleDirection;

import proceduralrails.grid.TrackGrid;
import proceduralrails.track.TrackConfig;
import proceduralrails.track.TrackSegment;
import proceduralrails.track.path.CurvedTrackPath;
import proceduralrails.track.path.StraightTrackPath;
import proceduralrails.track.path.TrackPath;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/* Responsible for generating TrackSegments along the edge of a TriangleGrid. Each edge is divided into two half-edges */
public class TrackEdgeProvider {
    private static final double THIRTY_DEGREES = Math.toRadians(30);
    private final TrackGrid grid;
    private final TrackConfig config;

    public TrackEdgeProvider(TrackGrid grid, TrackConfig config){
        this.grid = grid;
        this.config = config;
    }

    //create a list of half edge TrackSegments of a WHOLE edge
    public List<TrackSegment> twoHalfEdges(TriangleCoordinate coordinate, TriangleDirection direction, Point2D wholeTrackSegmentStart, Point2D wholeTrackSegmentFinish) {
        List<TrackSegment> oneEndOfEdge = halfEdges(coordinate, direction, wholeTrackSegmentStart, wholeTrackSegmentFinish);
        //remember to flip start/finish. since we want the half edge of the other end of the edge
        List<TrackSegment> otherEndOfEdge = halfEdges(coordinate.direction(direction), direction.opposite(), wholeTrackSegmentFinish, wholeTrackSegmentStart);
        oneEndOfEdge.addAll(otherEndOfEdge);
        return oneEndOfEdge;
    }

    //create a list of half edge TrackSegments containing the track connections of a given half Edge. The finish point of the whole track must be given in the parameter in order to calculate the control point for arcs.
    public List<TrackSegment> halfEdges(TriangleCoordinate coordinate, TriangleDirection trackDirection, Point2D wholeTrackSegmentStart, Point2D wholeTrackSegmentFinish){
        List<TrackSegment> halfEdges = new ArrayList<>();
        Point2D halfEdgeFinish = new Point2D.Double(
                (wholeTrackSegmentStart.getX() + wholeTrackSegmentFinish.getX()) / 2,
                (wholeTrackSegmentStart.getY() + wholeTrackSegmentFinish.getY()) / 2
        );

        //check the 3 edges in the opposite direction of the track  and see if they have a connection
        for (int i=-1; i <= 1; i++) {
            TriangleDirection connectionDirection = trackDirection.opposite().rotate(i);

            if (grid.hasTrack(coordinate, connectionDirection)) {
                TrackPath centralPath;
                //if directly opposite we know it's a straight connection
                if (i == 0)
                    centralPath = new StraightTrackPath(wholeTrackSegmentStart, halfEdgeFinish);
                else {
                    //otherwise it curves anticlockwise i = -1, or clockwise i = +1
                    //reverse the direction (-i) as we are coming from opposite direction of the track?
                    Point2D arcCentre = calculateControlPoint(wholeTrackSegmentStart, wholeTrackSegmentFinish, -i);
                    centralPath = CurvedTrackPath.fromEndpoints(wholeTrackSegmentStart, halfEdgeFinish, arcCentre, -i, THIRTY_DEGREES, config.getRailThickness());
                }

                halfEdges.add(new TrackSegment(centralPath, config, coordinate.hashCode(connectionDirection)));
            }
        }

        //no connections - track terminates, add a straight segment
        if (halfEdges.isEmpty()){
            TrackPath centralPath = new StraightTrackPath(wholeTrackSegmentStart, halfEdgeFinish);
            halfEdges.add(new TrackSegment(centralPath, config, coordinate.hashCode(trackDirection.opposite())));
        }

        return halfEdges;
    }

    //third vertex of equilateral triangle
    public static Point2D calculateControlPoint(Point2D vertex1, Point2D vertex2, int direction) {
        double dx = vertex2.getX() - vertex1.getX();
        double dy = vertex2.getY() - vertex1.getY();

        //length of the side
        double length = Math.sqrt(dx * dx + dy * dy);

        //height of equilateral triangle
        double height = (Math.sqrt(3) / 2) * length;

        //midpoint
        double mx = (vertex1.getX() + vertex2.getX()) / 2;
        double my = (vertex1.getY() + vertex2.getY()) / 2;

        //unit vector perpendicular to the line
        double ux = -dy / length;
        double uy = dx / length;

        //third vertex
        double x3 = mx + direction * ux * height;
        double y3 = my + direction * uy * height;

        return new Point2D.Double(x3, y3);
    }
}
