package compositegrid.grid;

import compositegrid.coordinates.TriangleCoordinate;
import compositegrid.coordinates.TriangleDirection;

public class TriangleCell<V, E> {
    private final TriangleCoordinate coordinate;
    private TriangleCell<V, E>[] neighbours;
    private final V vertex;
    private final E[] edges;

    public TriangleCell(TriangleCoordinate coordinate, V vertex, E[] edges) {
        this.coordinate = coordinate;
        this.vertex = vertex;
        this.edges = edges;
    }

    /** The coordinate of this triangle cell/vertex */
    public TriangleCoordinate getCoordinate() {
        return coordinate;
    }

    /** The actual vertex object */
    public V getVertex() {
        return vertex;
    }

    /** Array containing the 6 vertex objects adjacent to this vertex. */
    public TriangleCell<V, E>[] getNeighbours() {
        return neighbours;
    }

    /** The adjacent cell in the given direction. cell will be null if outside bounds of grid  */
    public TriangleCell<V, E> getNeighbour(TriangleDirection direction) {
        return neighbours[direction.ordinal()];
    }

    /** Array containing the surrounding edge objects. Array element will be null if outside bounds of grid  */
    public E[] getEdges() {
        return edges;
    }

    /** The edge object from the vertex in the given direction. edge will be null if outside bounds of grid  */
    public E getEdge(TriangleDirection direction){
        return edges[direction.ordinal()];
    }

    void setNeighbours(TriangleCell<V, E>[] neighbours) {
        this.neighbours = neighbours;
    }
}
