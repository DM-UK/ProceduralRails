package compositegrid.ui;

import compositegrid.coordinates.FractionalTriangleCoordinate;
import compositegrid.coordinates.HexagonCoordinate;
import compositegrid.coordinates.TriangleCoordinate;
import compositegrid.coordinates.TriangleDirection;

public interface GridSelectionListener {
    void vertexSelected(TriangleCoordinate coordinate);
    void edgeSelected(TriangleCoordinate coordinate, TriangleDirection direction);
    void hexagonSelected(HexagonCoordinate coordinate);
    void gridSelected(FractionalTriangleCoordinate coordinate);
}
