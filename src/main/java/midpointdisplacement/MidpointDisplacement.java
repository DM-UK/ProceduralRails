package midpointdisplacement;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implements the midpoint displacement algorithm of a line.
 * Generation via the generateMidpoints() methods
 */
public class MidpointDisplacement {
    private final int steps; // Number of recursive steps
    private final double maximumDisplacement; // Initial displacement magnitude
    private final double roughness; // Roughness factor for displacement

    /** Initialize displacement parameters ready for generating midpoints. */
    public MidpointDisplacement(int steps, double maximumDisplacement, double roughness) {
        this.steps = steps;
        this.maximumDisplacement = maximumDisplacement;
        this.roughness = roughness;
    }

    /** Generates a list of points forming a midpoint displaced line from the given start and end points using the default RNG */
    public List<Point2D> generateMidpoints(Point2D lineStart, Point2D lineFinish) {
        return generateMidpoints(lineStart, lineFinish, (int) (Math.random() * Integer.MAX_VALUE));
    }

    /** Generates a list of points forming a midpoint displaced line from the given start and end points. */
    public List<Point2D> generateMidpoints(Point2D lineStart, Point2D lineFinish, int seed) {
        if (lineStart == null || lineFinish == null) {
            throw new IllegalArgumentException("lineStart and lineFinish cannot be null");
        }

        // setup parameters for the initial call to displace().

        List<Point2D> displacedMidpoints = new ArrayList<>();
        Random rng = new Random(seed);

        // convert to Vector2D for easier manipulation
        Vector2D start = new Vector2D(lineStart.getX(), lineStart.getY());
        Vector2D finish = new Vector2D(lineFinish.getX(), lineFinish.getY());

        displacedMidpoints.add(start); // remember to add the starting point

        // recursively apply displacement
        displace(displacedMidpoints, start, finish, rng, steps, maximumDisplacement);

        displacedMidpoints.add(finish); // remember to add the ending point
        return displacedMidpoints;
    }

    /** Recursively displaces midpoints along the segment. */
    private void displace(List<Point2D> points, Vector2D lineStart, Vector2D lineFinish, Random rng, int steps, double maximumDisplacement) {
        if (steps == 0 || maximumDisplacement <= 0) {
            return;
        }

        // Step 1: Calculate the midpoint of the current segment
        Vector2D midPoint = lineStart.getAdded(lineFinish).getDivided(2);

        // Step 2: Determine the perpendicular displacement direction
        Vector2D directionVector = lineFinish.getSubtracted(lineStart);
        Vector2D normalizedPerpendicular = directionVector.getPerp().getNormalized();

        // Step 3: Apply a random displacement along the perpendicular direction
        double displacement = rng.nextDouble(-maximumDisplacement, maximumDisplacement);
        Vector2D newPoint = normalizedPerpendicular.getMultiplied(displacement).getAdded(midPoint);

        // Step 4: Reduce displacement magnitude for next recursion
        maximumDisplacement = maximumDisplacement * Math.pow(2, -roughness);
        steps--;

        // Step 5: Recursively process the left and right segments
        displace(points, lineStart, newPoint, rng, steps, maximumDisplacement);
        points.add(newPoint); // Add the new midpoint after processing left segment
        displace(points, newPoint, lineFinish, rng, steps, maximumDisplacement);
    }
}
