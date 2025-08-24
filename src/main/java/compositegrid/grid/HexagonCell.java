package compositegrid.grid;

import compositegrid.coordinates.HexagonCoordinate;
import compositegrid.coordinates.HexagonDirection;

public class HexagonCell<H, V, E> {
    private HexagonCell<H, V, E>[] neighbours;
    private final HexagonCoordinate coordinate;
    private final H hexagon;
    private final V centre;
    private final V[] vertices;
    private final E[] internalEdges;
    private final E[] borderEdges;

    public HexagonCell(HexagonCoordinate coordinate, H hexagon, V centre, V[] vertices, E[] internalEdges, E[] borderEdges){
        this.coordinate = coordinate;
        this.hexagon = hexagon;
        this.centre = centre;
        this.vertices = vertices;
        this.internalEdges = internalEdges;
        this.borderEdges = borderEdges;
    }

    /** The coordinate of this hexagon cell */
    public HexagonCoordinate getCoordinate() {
        return coordinate;
    }

    /** Array containing the six adjacent cells. Array element will be null if outside bounds of grid  */
    public HexagonCell<H, V, E>[] getNeighbours() {
        return neighbours;
    }

    /** The adjacent cell in the given direction. null if outside bounds of grid  */
    public HexagonCell<H, V, E> getNeighbour(HexagonDirection direction) {
        return neighbours[direction.ordinal()];
    }

    /** The actual hexagon tile object */
    public H getHexagon() {
        return hexagon;
    }

    /** Array containing the 6 vertex objects around the centre vertex (ie. located along the hexagon edge.) */
    public V[] getVertices() {
        return vertices;
    }

    /** The vertex in the given direction */
    public V getVertex(HexagonDirection direction) {
        return vertices[direction.ordinal()];
    }

    /** The vertex object located in the centre */
    public V getCentreVertex() {
        return centre;
    }

    /** Array containing the six edge objects along the internal edges. */
    public E[] getInternalEdges() {
        return internalEdges;
    }

    /** The internal edge in the given direction */
    public E getInternalEdge(HexagonDirection direction) {
        return internalEdges[direction.ordinal()];
    }

    /** Array containing the six edge objects along the hexagon edge. */
    public E[] getBorderEdges() {
        return borderEdges;
    }

    /** The border edge in the given direction */
    public E getBorderEdge(HexagonDirection direction) {
        return borderEdges == null ? null : borderEdges[direction.ordinal()];
    }

    void setNeighbours(HexagonCell<H, V, E>[] neighbours) {
        this.neighbours = neighbours;
    }
}
