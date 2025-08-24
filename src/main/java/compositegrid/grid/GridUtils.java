package compositegrid.grid;

import compositegrid.coordinates.*;

public class GridUtils {
    /** Returns the 6 adjacent vertex cells around a coordinate  */
    public static Object[] getVerticeCellNeighbours(CompositeGrid grid, TriangleCoordinate coordinate) {
        TriangleCell[] neighbours = new TriangleCell[6];
        int index = 0;

        for (TriangleCoordinate neighbour: coordinate.neighbours()){
            if (grid.isVertexInBounds(neighbour))
                neighbours[index] = grid.getTriangleCell(neighbour);
            index++;
        }

        return neighbours;
    }

    /** Populates the given array with the 6 adjacent vertex objects around a coordinate  */
    public static Object[] getVerticeNeighboursOnly(TriangleCell cell, Object[] vertices) {
        for (int i = 0; i < 6; i++){
            if (cell.getNeighbours()[i] != null)
                vertices[i] = cell.getNeighbours()[i].getVertex();
        }

        return vertices;
    }

    /** Populates the given array with the 6 border edge objects of a hexagon  */
    public static Object[] getBorderEdges(CompositeGrid grid, HexagonCoordinate coordinate, Object[] edges) {
        TriangleCoordinate triCoord = CoordinateConversion.toTriangle(coordinate);

        for (int i = 0; i < 6; i++){
            TriangleDirection vertexDirection = TriangleDirection.values()[i];
            TriangleDirection edgeDirection = vertexDirection.rotate(2);
            TriangleCoordinate currentCoord = triCoord.direction(vertexDirection);

            if (grid.isEdgeInBounds(currentCoord, edgeDirection))
                edges[i] = grid.getTriangleCell(currentCoord).getEdge(edgeDirection);
        }

        return edges;
    }

    /** Returns the 6 neighbouring hexagon cells of a hexagon  */
    public static Object[] getHexagonNeighbours(CompositeGrid grid, HexagonCoordinate coordinate) {
        HexagonCell[] neighbours = new HexagonCell[6];
        int index = 0;

        for (HexagonDirection direction: HexagonDirection.values()){
            HexagonCoordinate neighbour = coordinate.direction(direction);
            if (grid.isInBounds(neighbour))
                neighbours[index] = grid.getHexagonCell(neighbour);

            index++;
        }

        return neighbours;
    }
}
