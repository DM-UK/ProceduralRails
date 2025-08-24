package compositegrid.coordinates;

public enum TriangleDirection {
    WEST(-1, 0, 1),
    NORTHWEST(0, -1, 1),
    NORTHEAST(1, -1, 0),
    EAST(1, 0, -1),
    SOUTHEAST(0, 1, -1),
    SOUTHWEST(-1, 1, 0);

    public final int dx, dy, dz;

    TriangleDirection(int dx, int dy, int dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public TriangleDirection rotate(int n) {
        int currentIndex = this.ordinal();
        int newIndex = (currentIndex + n) % 6;
        if (newIndex < 0)
            newIndex += 6;
        return values()[newIndex];
    }

    public TriangleDirection opposite() {
        return rotate(3);
    }
}