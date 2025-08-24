package proceduralrails.grid;

import compositegrid.coordinates.TriangleCoordinate;
import compositegrid.coordinates.TriangleDirection;

public interface TrackGridListener {
    public void railPlaced(TriangleCoordinate coordinate, TriangleDirection direction);
    public void railRemoved(TriangleCoordinate coordinate, TriangleDirection direction);
}
