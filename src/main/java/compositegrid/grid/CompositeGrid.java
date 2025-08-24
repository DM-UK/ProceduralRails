package compositegrid.grid;

import compositegrid.GenericsUtils;
import compositegrid.coordinates.CoordinateConversion;
import compositegrid.coordinates.HexagonCoordinate;
import compositegrid.coordinates.TriangleCoordinate;
import compositegrid.coordinates.TriangleDirection;

import java.awt.*;

public class CompositeGrid<H, V, E> implements TriangleGrid<V, E>, HexagonGrid<H> {
    /** Dimensions in number of vertices */
    private final int gridVerticesWidth;
    private final int gridVerticesHeight;
    /** Dimensions in number of hexagons */
    private final int hexagonWidth;
    private final int hexagonHeight;
    /** For use in creating objects on each vertex/edge/hexagon */
    private final Class<?> vertexClass;
    private final Class<?> edgeClass;
    private final Class<?> hexagonClass;
    /** Container holding a triangle and it's adjacent graph relationships  */
    private TriangleCell<V,E>[][] triangleCells;
    /** Container holding a hexagon and it's adjacent graph relationships  */
    private HexagonCell<H, V, E>[][] hexagonCells;

    public CompositeGrid(int hexagonWidth, int hexagonHeight, Class vertexClass, Class edgeClass, Class hexagonClass) {
        this.hexagonWidth = hexagonWidth;
        this.hexagonHeight = hexagonHeight;
        this.gridVerticesWidth = getVertexWidth();
        this.gridVerticesHeight = getVertexHeight();
        this.vertexClass = vertexClass;
        this.edgeClass = edgeClass;
        this.hexagonClass = hexagonClass;
        buildTriangleGrid();
        buildHexagonGrid();
    }

    private void buildTriangleGrid() {
        triangleCells = new TriangleCell[gridVerticesHeight][gridVerticesWidth];

        for (int y = 0; y < gridVerticesHeight; y++) {
            for (int x = 0; x < gridVerticesWidth; x++) {
                TriangleCoordinate coordinate = TriangleCoordinate.fromOffset(x, y);
                //create empty arrays to pass to util functions
                E[] edges = (E[]) GenericsUtils.createArray(edgeClass, 6);
                //actual vertex object
                V vertex = (V) GenericsUtils.createInstance(vertexClass);

                for (int i = 0; i < 6; i++) {
                    TriangleDirection dir = TriangleDirection.values()[i];
                    TriangleCoordinate neighbor = coordinate.direction(dir);

                    if (isVertexInBounds(neighbor)) {
                        TriangleCell<V, E> neighbourCell = getTriangleCell(neighbor);

                        //check if the neighbouring vertex has been created
                        if (neighbourCell == null)
                            edges[i] = (E) GenericsUtils.createInstance(edgeClass);
                        else
                            edges[i] = neighbourCell.getEdge(dir.opposite());
                    }
                }

                triangleCells[y][x] = new TriangleCell<>(coordinate, vertex, edges);
            }
        }

        //populate neighbours (unable to do this on first pass)
        for (int y = 0; y < gridVerticesHeight; y++) {
            for (int x = 0; x < gridVerticesWidth; x++) {
                TriangleCoordinate coordinate = TriangleCoordinate.fromOffset(x, y);
                TriangleCell<V, E>[] neighbours = (TriangleCell<V, E>[]) GridUtils.getVerticeCellNeighbours(this, coordinate);
                getTriangleCell(coordinate).setNeighbours(neighbours);
            }
        }
    }

    private void buildHexagonGrid() {
        hexagonCells = new HexagonCell[hexagonHeight][hexagonWidth];

        for (int y = 0; y < hexagonHeight; y++) {
            for (int x = 0; x < hexagonWidth; x++) {
                //create empty arrays to pass to util functions
                V[] emptyVerticesArray = (V[]) GenericsUtils.createArray(vertexClass, 6);
                E[] emptyEdgesArray =  (E[]) GenericsUtils.createArray(edgeClass, 6);

                HexagonCoordinate hexCoord = HexagonCoordinate.fromOffset(x, y);
                //actual hexagon tile
                H hexagon = (H)GenericsUtils.createInstance(hexagonClass);
                //triangle cell located at the hexagon centre
                TriangleCell<V, E> triCell = getTriangleCell(hexCoord.convert());
                //vertex located at the hexagon centre
                V centre = (V) triCell.getVertex();
                //6 vertices surrounding the hexagon centre (ie. along the hexagon edge)
                V[] vertices = (V[]) GridUtils.getVerticeNeighboursOnly(triCell, emptyVerticesArray);
                //6 edges along the hexagon edge
                E[] externalEdges = (E[]) GridUtils.getBorderEdges(this, hexCoord, emptyEdgesArray);
                //6 (triangle) edges around the hexagon centre vertex
                E[] internalEdges = triCell.getEdges();
                hexagonCells[y][x] = new HexagonCell<>(hexCoord, hexagon, centre, vertices, internalEdges, externalEdges);
            }
        }

        //populate neighbours (unable to do this on first pass)
        for (int y = 0; y < hexagonHeight; y++) {
            for (int x = 0; x < hexagonWidth; x++) {
                HexagonCoordinate hexCoord = HexagonCoordinate.fromOffset(x, y);
                HexagonCell<H, V, E>[] neighbours = (HexagonCell<H, V, E>[]) GridUtils.getHexagonNeighbours(this, hexCoord);
                getHexagonCell(hexCoord).setNeighbours(neighbours);
            }
        }
    }

    /** Returns a TriangleCell. Caller is required to check if cell is in bounds */
    @Override
    public TriangleCell<V, E> getTriangleCell(TriangleCoordinate coordinate) {
        Point offset = coordinate.toOffset();
        return triangleCells[offset.y][offset.x];
    }

    /** Returns a TriangleCell. Caller is required to check if cell is in bounds */
    public HexagonCell<H, V, E> getHexagonCell(HexagonCoordinate coordinate) {
        Point offset = coordinate.toOffset();
        return hexagonCells[offset.y][offset.x];
    }

    /** Returns an H hexagon object. Caller is required to check if hexagon is in bounds */
    @Override
    public H getHexagon(HexagonCoordinate coordinate) {
        return getHexagonCell(coordinate).getHexagon();
    }

    /** Determines whether a coordinate falls within the grid */
    @Override
    public boolean isVertexInBounds(TriangleCoordinate coordinate) {
        Point offset = coordinate.toOffset();
        return offset.y >= 0 && offset.y < gridVerticesHeight && offset.x >= 0 && offset.x < gridVerticesWidth;
    }

    /** Determines whether an edge falls within the grid */
    @Override
    public boolean isEdgeInBounds(TriangleCoordinate coordinate, TriangleDirection direction) {
        TriangleCoordinate edgeFinish = coordinate.direction(direction);
        return isVertexInBounds(coordinate) && isVertexInBounds(edgeFinish);
    }
    @Override
    /** Determines whether an hexagon falls within the grid */
    public boolean isInBounds(HexagonCoordinate coordinate) {
        Point offset = coordinate.toOffset();
        return offset.y >= 0 && offset.y < hexagonHeight && offset.x >= 0 && offset.x < hexagonWidth;
    }

    @Override
    public int getVertexWidth() {
        return CoordinateConversion.vertexWidth(hexagonHeight);
    }

    @Override
    public int getVertexHeight() {
        return CoordinateConversion.vertexHeight(hexagonHeight);
    }

    @Override
    public int getHexagonWidth() {
        return hexagonWidth;
    }

    @Override
    public int getHexagonHeight() {
        return hexagonHeight;
    }

    public TriangleGrid<V, E> getTriangleGrid(){
        return this;
    }

    public HexagonGrid<H> getHexagonGrid(){
        return this;
    }
}
