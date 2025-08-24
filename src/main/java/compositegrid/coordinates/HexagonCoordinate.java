package compositegrid.coordinates;

import java.awt.*;

public class HexagonCoordinate {
    public final int x, y, z;

    public HexagonCoordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public HexagonCoordinate add(HexagonCoordinate other) {
        return new HexagonCoordinate(x + other.x, y + other.y, z + other.z);
    }

    public HexagonCoordinate subtract(HexagonCoordinate other) {
        return new HexagonCoordinate(x - other.x, y - other.y, z - other.z);
    }

    public HexagonCoordinate direction(HexagonDirection direction) {
        return new HexagonCoordinate(x + direction.dx, y + direction.dy, z + direction.dz);
    }

    public HexagonCoordinate[] neighbours() {
        HexagonCoordinate[] neighbours = new HexagonCoordinate[6];

        for (int i = 0; i < 6; i++)
            neighbours[i] = direction(HexagonDirection.values()[i]);

        return neighbours;
    }

    public FractionalHexagonCoordinate toFractional() {
        return new FractionalHexagonCoordinate(x, y, z);
    }

    public Point toOffset() {
        return CoordinateConversion.toOffset(this);
    }

    public TriangleCoordinate convert() {
        return CoordinateConversion.toTriangle(this);
    }

    @Override
    public int hashCode() {
        //taken from Point.hashCode()
        long bits = Double.doubleToLongBits(x);
        bits ^= Double.doubleToLongBits(y) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    public static HexagonCoordinate fromOffset(int x, int y) {
        return CoordinateConversion.toHexagon(x, y);
    }
}
