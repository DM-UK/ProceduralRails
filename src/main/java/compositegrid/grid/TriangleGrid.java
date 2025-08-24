package compositegrid.grid;

import compositegrid.coordinates.TriangleCoordinate;
import compositegrid.coordinates.TriangleDirection;

public interface TriangleGrid<V, E> {
    TriangleCell<V, E> getTriangleCell(TriangleCoordinate coordinate);

    boolean isVertexInBounds(TriangleCoordinate coordinate);

    boolean isEdgeInBounds(TriangleCoordinate coordinate, TriangleDirection direction);

    int getVertexWidth();

    int getVertexHeight();
}
