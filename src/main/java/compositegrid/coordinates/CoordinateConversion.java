package compositegrid.coordinates;

import java.awt.*;
import java.awt.geom.Point2D;

/** Utility class for conversions between the triangle/hexagonal/offset coordinate systems*/
public class CoordinateConversion {
    /* Note on Coordinate classes: It's probably simpler to read/maintain and have 4 Coordinate classes with duplicate code rather than abstracting out their shared logic as a superclass.
    Keeping conversion logic here will allow for easy changes to conversion systems.
    Logic is for flat topped/bottomed triangles and flat topped hexagons. Any change would require recalculations */

    //hex -> tri
    public static TriangleCoordinate toTriangle(HexagonCoordinate coordinate){
        int x = coordinate.x - coordinate.y;
        int y = 2 * coordinate.y + coordinate.x + 1;
        return new TriangleCoordinate(x, y, -x-y);
    }

    //hex -> tri
    public static FractionalTriangleCoordinate toFractionalTriangle(FractionalHexagonCoordinate coordinate){
        double x = coordinate.x - coordinate.y;
        double y = 2 * coordinate.y + coordinate.x + 1;
        return new FractionalTriangleCoordinate(x, y, -x-y);
    }

    //tri -> hex
    public static HexagonCoordinate toHexagon(TriangleCoordinate coordinate){
        int b = (coordinate.y - coordinate.x - 1) / 3;
        int a = b + coordinate.x;
        return new HexagonCoordinate(a, b, -a -b);    }

    //tri -> hex
    public static FractionalHexagonCoordinate toFractionalHexagon(FractionalTriangleCoordinate coordinate){
        double b = (coordinate.y - coordinate.x - 1) / 3;
        double a = b + coordinate.x;
        return new FractionalHexagonCoordinate(a, b, -a -b);
    }

    //evenr to cube
    public static TriangleCoordinate toTriangle(int x, int y){
        int a = x - (y + (y & 1)) / 2;
        int b = y;
        int c = -a - b;
        return new TriangleCoordinate(a, b, c);
    }

    //evenr to cube
    public static FractionalTriangleCoordinate toFractionalTriangle(double x, double y){
        double a = x - (y + ((int)Math.abs(y) & 1)) / 2;
        double b = y;
        double c = -a - b;
        return new FractionalTriangleCoordinate(a, b, c);
    }

    //oddq to cube
    public static HexagonCoordinate toHexagon(int x, int y){
        int a = x;
        int b = y - (x - (x&1)) / 2;
        int c = -a - b;
        return new HexagonCoordinate(a, b, c);
    }

    //oddq to cube
    public static FractionalHexagonCoordinate toFractionalHexagon(double x, double y){
        double a = x;
        double b = y - (x - ((int)Math.abs(x) & 1)) / 2;
        double c = -a - b;
        return new FractionalHexagonCoordinate(a, b, c);
    }

    //cube to evenr
    public static Point toOffset(TriangleCoordinate coordinate){
        int offSetX = coordinate.x + (coordinate.y + (coordinate.y & 1)) / 2;
        int offSetY = coordinate.y;
        return new Point(offSetX, offSetY);
    }

    //cube to evenr
    public static Point2D toOffset(FractionalTriangleCoordinate coordinate){
        double offSetX = coordinate.x + (coordinate.y + ((int)Math.abs(coordinate.y) & 1)) / 2;
        double offSetY = coordinate.y;
        return new Point2D.Double(offSetX, offSetY);
    }

    //cube to oddq
    public static Point toOffset(HexagonCoordinate coordinate) {
        int offSetX = coordinate.x;
        int offSetY = coordinate.y + (coordinate.x - (coordinate.x & 1)) / 2;
        return new Point(offSetX, offSetY);
    }

    //cube to oddq
    public static Point2D toOffset(FractionalHexagonCoordinate coordinate){
        double offSetX = coordinate.x;
        double offSetY = coordinate.y + (coordinate.x - ((int)coordinate.x & 1)) / 2;
        return new Point2D.Double(offSetX, offSetY);
    }

    //the number of vertices wide in a hexagon grid
    public static int vertexWidth(int hexagonWidth) {
        return (3 * hexagonWidth + 1) / 2;
    }

    //the number of vertices heigh in a hexagon grid
    public static int vertexHeight(int hexagonHeight) {
        return (hexagonHeight * 2) + 2;
    }
}
