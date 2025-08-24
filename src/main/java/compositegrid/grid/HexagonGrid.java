package compositegrid.grid;

import compositegrid.coordinates.HexagonCoordinate;

public interface HexagonGrid<H> {
    H getHexagon(HexagonCoordinate coordinate);

    boolean isInBounds(HexagonCoordinate coordinate);

    int getHexagonWidth();

    int getHexagonHeight();
}
