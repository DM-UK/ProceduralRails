package compositegrid.coordinates;

import java.awt.geom.Point2D;

public class FractionalTriangleCoordinate {
    public final double x, y, z;

    public FractionalTriangleCoordinate(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public FractionalTriangleCoordinate add(FractionalTriangleCoordinate other) {
        return new FractionalTriangleCoordinate(x + other.x, y + other.y, z + other.z);
    }

    public FractionalTriangleCoordinate subtract(FractionalTriangleCoordinate other) {
        return new FractionalTriangleCoordinate(x - other.x, y - other.y, z - other.z);
    }

    public FractionalTriangleCoordinate direction(TriangleDirection direction) {
        return new FractionalTriangleCoordinate(x + direction.dx, y + direction.dy, z + direction.dz);
    }

    public TriangleCoordinate rounded() {
        int rx = (int) Math.round(x);
        int ry = (int) Math.round(y);
        int rz = (int) Math.round(z);

        double dx = Math.abs(rx - x);
        double dy = Math.abs(ry - y);
        double dz = Math.abs(rz - z);

        if (dx > dy && dx > dz) rx = -ry - rz;
        else if (dy > dz) ry = -rx - rz;
        else rz = -rx - ry;

        return new TriangleCoordinate(rx, ry, rz);
    }

    public TriangleCoordinate floored() {
        int fx = (int) Math.floor(x);
        int fy = (int) Math.floor(y);
        int fz = (int) (-fx - fy);
        return new TriangleCoordinate(fx, fy, fz);
    }

    public Point2D toOffset() {
        return CoordinateConversion.toOffset(this);
    }

    public FractionalHexagonCoordinate convert() {
        return CoordinateConversion.toFractionalHexagon(this);
    }

    @Override
    public int hashCode() {
        //taken from Point2D.Double.hashCode()
        long bits = Double.doubleToLongBits(x);
        bits ^= Double.doubleToLongBits(y) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    public static FractionalTriangleCoordinate fromOffset(double x, double y) {
        return CoordinateConversion.toFractionalTriangle(x, y);
    }
}
