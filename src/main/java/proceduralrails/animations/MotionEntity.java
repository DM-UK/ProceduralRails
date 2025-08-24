package proceduralrails.animations;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/* Represents a single animated entity */
public class MotionEntity {
    private final String id;
    private final Shape startShape;
    private final Shape endShape;
    private final double startRotation;
    private final double totalRotation;
    private final long spawnTime;
    private final long placementTime;

    private MotionEntityState state;
    private Shape currentShape;

    public MotionEntity(String id, Shape shape, Point2D startPos, Point2D endPos, long spawnTime, double velocity) {
        this.id = id;
        this.startRotation = Math.random() * Math.PI * 2;
        this.totalRotation = Math.PI * 2; // one full spin

        // Translate shape to start position
        this.startShape = transformShape(shape, -startRotation,
                startPos.getX() - shape.getBounds2D().getCenterX(),
                startPos.getY() - shape.getBounds2D().getCenterY());

        // Translate shape to end position
        this.endShape = transformShape(shape, 0,
                endPos.getX() - shape.getBounds2D().getCenterX(),
                endPos.getY() - shape.getBounds2D().getCenterY());

        this.spawnTime = spawnTime;
        double distance = startPos.distance(endPos);
        long travelTime = (long) (distance / velocity);
        this.placementTime = spawnTime + travelTime;

        this.state = MotionEntityState.UNSPAWNED;
        this.currentShape = startShape;
    }

    public MotionEntityState updateState(long currentTime) {
        if (state == MotionEntityState.FINAL_POSITION) return null;

        if (currentTime >= placementTime) {
            state = MotionEntityState.FINAL_POSITION;
            currentShape = endShape;
            return MotionEntityState.FINAL_POSITION;
        } else if (currentTime >= spawnTime && state == MotionEntityState.UNSPAWNED) {
            state = MotionEntityState.MOVING;
            return MotionEntityState.MOVING;
        }

        return null;
    }

    /** Updates position/rotation based on current state and time */
    public void updatePosition(long currentTime) {
        if (state != MotionEntityState.MOVING) return;

        double fraction = (double)(currentTime - spawnTime) / (placementTime - spawnTime);
        fraction = Math.min(Math.max(fraction, 0), 1);

        double startX = startShape.getBounds2D().getCenterX();
        double startY = startShape.getBounds2D().getCenterY();
        double endX = endShape.getBounds2D().getCenterX();
        double endY = endShape.getBounds2D().getCenterY();

        double tx = (endX - startX) * fraction;
        double ty = (endY - startY) * fraction;
        double rotation = startRotation + totalRotation * fraction;

        currentShape = transformShape(startShape, rotation, tx, ty);
    }

    private Shape transformShape(Shape shape, double rotation, double tx, double ty) {
        AffineTransform transform = new AffineTransform();
        transform.translate(tx, ty);
        transform.rotate(rotation, shape.getBounds2D().getCenterX(), shape.getBounds2D().getCenterY());
        return transform.createTransformedShape(shape);
    }

    public Shape getShape() {
        return currentShape;
    }

    public String getID() {
        return id;
    }

    public MotionEntityState getState() {
        return state;
    }
}