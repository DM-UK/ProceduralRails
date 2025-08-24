package compositegrid.coordinates;

import java.awt.geom.Point2D;

public class FractionalHexagonCoordinate {
    public final double x, y, z;

    public FractionalHexagonCoordinate(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public FractionalHexagonCoordinate add(FractionalHexagonCoordinate other) {
        return new FractionalHexagonCoordinate(x + other.x, y + other.y, z + other.z);
    }

    public FractionalHexagonCoordinate subtract(FractionalHexagonCoordinate other) {
        return new FractionalHexagonCoordinate(x - other.x, y - other.y, z - other.z);
    }

    public FractionalHexagonCoordinate direction(HexagonDirection direction) {
        return new FractionalHexagonCoordinate(x + direction.dx, y + direction.dy, z + direction.dz);
    }

    public HexagonCoordinate rounded() {
        int rx = (int) Math.round(x);
        int ry = (int) Math.round(y);
        int rz = (int) Math.round(z);

        double dx = Math.abs(rx - x);
        double dy = Math.abs(ry - y);
        double dz = Math.abs(rz - z);

        if (dx > dy && dx > dz) rx = -ry - rz;
        else if (dy > dz) ry = -rx - rz;
        else rz = -rx - ry;

        return new HexagonCoordinate(rx, ry, rz);
    }

    public HexagonCoordinate floored() {
        int fx = (int) Math.floor(x);
        int fy = (int) Math.floor(y);
        int fz = (int) (-fx - fy);
        return new HexagonCoordinate(fx, fy, fz);
    }

    public Point2D toOffset() {
        return CoordinateConversion.toOffset(this);
    }

    public FractionalTriangleCoordinate convert() {
        return CoordinateConversion.toFractionalTriangle(this);
    }

    @Override
    public int hashCode() {
        //taken from Point2D.Double.hashCode()
        long bits = Double.doubleToLongBits(x);
        bits ^= Double.doubleToLongBits(y) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    public static FractionalHexagonCoordinate fromOffset(double x, double y) {
        return CoordinateConversion.toFractionalHexagon(x, y);
    }
}
