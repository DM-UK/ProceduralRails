package compositegrid.coordinates;

/** Utility class for calculations in coordinate space */
public class CoordinateUtils {
    public static boolean fallsOnHexagonCentre(TriangleCoordinate coordinate) {
        return (hexPatternOffset(coordinate) == 0);
    }

    public static int hexPatternOffset(TriangleCoordinate coordinate) {
        //some sort of pattern occurs as the result of this equation!!!
        //see accompanying screenshot in resources folder for clarity
        //0 = centre of hexagon
        //1 = lands on the w/ne/se border of a hexagon
        //2 = lands on the nw/e/sw border of a hexagon
        return Math.floorMod(coordinate.y - coordinate.x - 1, 3);
    }

    public static boolean fallsOnHexagonBorder(TriangleCoordinate coordinate, TriangleDirection direction) {
        if (fallsOnHexagonCentre(coordinate))
            return false;

        TriangleCoordinate neighbour = coordinate.direction(direction);
        return !fallsOnHexagonCentre(neighbour);
    }

    /** Returns the 2 adjacent hexagon coordinates to an edge. This will return the same 2 coordinates if it's an internal edge */
    public static HexagonCoordinate[] getAdjacentHexagonsOfEdge(TriangleCoordinate startVertex, TriangleDirection direction) {
        //if edge starts on hexagon centre return hexagon
        if (fallsOnHexagonCentre(startVertex))
            return new HexagonCoordinate[]{startVertex.convert(), startVertex.convert()};

        //if edge finishes on hexagon centre return hexagon
        TriangleCoordinate endVertex = startVertex.direction(direction);
        if (fallsOnHexagonCentre(endVertex))
            return new HexagonCoordinate[]{endVertex.convert(), endVertex.convert()};

        //calculate the two adjacent hexagon centre by rotating -1 and +1 around the vertex
        TriangleCoordinate hexagonCentreA = startVertex.direction(direction.rotate(-1));
        TriangleCoordinate hexagonCentreB = startVertex.direction(direction.rotate(+1));

        return new HexagonCoordinate[]{hexagonCentreA.convert(), hexagonCentreB.convert()};
    }

    /** Returns the 3 neighbouring hexagon coordinates of a vertex. This will return the same 3 coordinates if it's a hexagon centre   */
    public static HexagonCoordinate[] getAdjacentHexagonsOfVertex(TriangleCoordinate triCoord) {
        //see accompanying screenshot in resources folder for clarity

        int hexPatternOffset = hexPatternOffset(triCoord);

        //is centre, so convert and return a single hexagon
        if (hexPatternOffset == 0)
            return new HexagonCoordinate[]{triCoord.convert(), triCoord.convert(), triCoord.convert()};

        //is border, so has 3 adjacent hexagons
        HexagonCoordinate[] adjacentHexagons = new HexagonCoordinate[3];
        TriangleDirection currentDirection = null;

        //determine which border pattern it follows
        if (hexPatternOffset == 1)
            currentDirection = TriangleDirection.EAST;
        else if (hexPatternOffset == 2)//guaranteed
            currentDirection = TriangleDirection.WEST;

        //calculate each adjacent hexagon centre by rotating (+2 direction) around the vertex
        for (int i = 0 ; i < 3; i++){
            TriangleCoordinate adjacentHexagonCentre = triCoord.direction(currentDirection);
            adjacentHexagons[i] = adjacentHexagonCentre.convert();
            currentDirection = currentDirection.rotate(2);
        }

        return adjacentHexagons;
    }
}
