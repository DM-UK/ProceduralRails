package compositegrid.coordinates;

import java.awt.*;

public class TriangleCoordinate {
    public final int x, y, z;

    public TriangleCoordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public TriangleCoordinate add(TriangleCoordinate other) {
        return new TriangleCoordinate(x + other.x, y + other.y, z + other.z);
    }

    public TriangleCoordinate subtract(TriangleCoordinate other) {
        return new TriangleCoordinate(x - other.x, y - other.y, z - other.z);
    }

    public TriangleCoordinate direction(TriangleDirection direction) {
        return new TriangleCoordinate(x + direction.dx, y + direction.dy, z + direction.dz);
    }

    public TriangleCoordinate[] neighbours() {
        TriangleCoordinate[] neighbours = new TriangleCoordinate[6];

        for (int i = 0; i < 6; i++)
            neighbours[i] = direction(TriangleDirection.values()[i]);

        return neighbours;
    }

    public FractionalTriangleCoordinate toFractional() {
        return new FractionalTriangleCoordinate(x, y, z);
    }

    public Point toOffset() {
        return CoordinateConversion.toOffset(this);
    }

    public HexagonCoordinate convert() {
        return CoordinateConversion.toHexagon(this);
    }

    @Override
    public int hashCode() {
        //taken from Point.hashCode()
        long bits = Double.doubleToLongBits(x);
        bits ^= Double.doubleToLongBits(y) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }


    public int hashCode(TriangleDirection direction) {
        TriangleCoordinate coordinate = this;

        //maintain continuity, a SW edge from 1 vertex is a NW edge from the other end
        if (direction.ordinal() >= 3){
            coordinate = coordinate.direction(direction);
            direction = direction.rotate(3);
        }

        return (coordinate.hashCode() ^ direction.hashCode());
    }

    public static TriangleCoordinate fromOffset(int x, int y) {
        return CoordinateConversion.toTriangle(x, y);
    }
}
