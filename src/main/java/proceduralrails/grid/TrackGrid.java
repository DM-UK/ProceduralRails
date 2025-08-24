package proceduralrails.grid;

import compositegrid.coordinates.TriangleCoordinate;
import compositegrid.coordinates.TriangleDirection;
import compositegrid.grid.CompositeGrid;

public class TrackGrid extends CompositeGrid<Object, Object, GridEdge> {
    public TrackGrid(int hexagonWidth, int hexagonHeight) {
        super(hexagonWidth, hexagonHeight, Object.class, GridEdge.class, Object.class);
    }

    public boolean placeRail(TriangleCoordinate coordinate, TriangleDirection direction) {
        if (isEdgeInBounds(coordinate, direction)) {
            GridEdge edge = getTriangleCell(coordinate).getEdge(direction);

            if (edge.hasTrack())
                return false;

            edge.setTrack(true);
            return true;
        }
        else
            return false;
    }

    public boolean destroyRail(TriangleCoordinate coordinate, TriangleDirection direction) {
        if (isEdgeInBounds(coordinate, direction)) {
            GridEdge edge = getTriangleCell(coordinate).getEdge(direction);
            if (!edge.hasTrack())
                return false;
            edge.setTrack(false);
            return true;
        }
        else
            return false;
    }

    public boolean hasTrack(TriangleCoordinate coordinate, TriangleDirection direction) {
        if (isEdgeInBounds(coordinate, direction)) {
            GridEdge edge = getTriangleCell(coordinate).getEdge(direction);
            return edge.hasTrack();
        }
        else
            return false;
    }
}
