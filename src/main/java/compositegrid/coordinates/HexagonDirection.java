package compositegrid.coordinates;

public enum HexagonDirection {
    NORTHWEST(-1, 0, 1),
    NORTH(0, -1, 1),
    NORTHEAST(1, -1, 0),
    SOUTHEAST(1, 0, -1),
    SOUTH(0, 1, -1),
    SOUTHWEST(-1, 1, 0);

    public final int dx, dy, dz;

    HexagonDirection(int dx, int dy, int dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public HexagonDirection rotate(int n) {
        int currentIndex = this.ordinal();
        int newIndex = (currentIndex + n) % 6;
        if (newIndex < 0)
            newIndex += 6;
        return values()[newIndex];
    }

    public HexagonDirection opposite() {
        return rotate(3);
    }
}