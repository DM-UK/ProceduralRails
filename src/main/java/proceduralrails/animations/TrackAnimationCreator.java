package proceduralrails.animations;

import proceduralrails.track.Track;
import proceduralrails.track.TrackSegment;
import proceduralrails.track.path.TrackPath;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.*;

/** Responsible for constructing TrackAnimations */
public class TrackAnimationCreator {
    private final TrackAnimationControls controls;
    private Point2D trackSpawnPosition = new Point2D.Double(0, 0);
    private Point2D railSpawnPosition = new Point2D.Double(0, 0);
    private Rectangle screenBounds;
    private final Random rand = new Random();

    public TrackAnimationCreator(TrackAnimationControls controls){
        this.controls = controls;
    }

    //for entities random target in destruction animation
    public void setScreenBounds(int x, int y, int width, int height){
        //+- to ensure it moves outside screen before deletion
        this.screenBounds = new Rectangle(x-100, y-100, width+200, height+200);
    }

    public TrackAnimation destroyTrack(List<TrackSegment> segments){
        TrackAnimation animation = new TrackAnimation();
        List<Shape> sleepers = createSleeperList(segments);
        List<Shape> rails = createRailList(segments, controls.getRailPieces());
        createDestructionMotionEntities(animation, "SLEEPER", sleepers, controls.getDestructionVelocity());
        createDestructionMotionEntities(animation, "RAIL", rails, controls.getDestructionVelocity());
        return animation;
    }

    public TrackAnimation placeTrack(List<TrackSegment> segments){
        TrackAnimation animation = new TrackAnimation();
        List<Shape> sleepers = createSleeperList(segments);
        List<Shape> rails = createRailList(segments, controls.getRailPieces());
        Collections.shuffle(sleepers);
        Collections.shuffle(rails);
        createSpawnEntities(animation, "SLEEPER", sleepers, trackSpawnPosition, controls.getSleeperSpawnInterval(), controls.getSleeperVelocity());
        createSpawnEntities(animation, "RAIL", rails, railSpawnPosition, controls.getRailSpawnInterval(), controls.getRailVelocity());
        return animation;
    }

    private void createSpawnEntities(TrackAnimation animation, String name, List<Shape> shapes, Point2D spawnPosition, long interval, double velocity) {
        long spawnTime = System.currentTimeMillis();
        for (Shape shape : shapes) {
            Point2D centre = new Point2D.Double(shape.getBounds2D().getCenterX(), shape.getBounds2D().getCenterY());
            MotionEntity entity = new MotionEntity(name, shape, spawnPosition, centre, spawnTime, velocity);
            animation.addEntity(entity);
            spawnTime = (long) (spawnTime + interval + Math.random() * 100);
        }
    }

    private void createDestructionMotionEntities(TrackAnimation animation, String name, List<Shape> shapes, double destructionVelocity) {
        long spawnTime = System.currentTimeMillis();
        for (Shape shape : shapes) {
            Point2D startPos = new Point2D.Double(shape.getBounds2D().getCenterX(), shape.getBounds2D().getCenterY());
            Point endPos = getRandomPointOnScreenEdge();
            MotionEntity entity = new MotionEntity(name, shape, startPos, endPos, spawnTime, destructionVelocity);
            animation.addEntity(entity);
        }
    }
    
    private Point getRandomPointOnScreenEdge() {
        return switch (rand.nextInt(4)) {
            case 0 -> new Point(
                    rand.nextInt(screenBounds.width) + screenBounds.x,
                    screenBounds.y
            );
            case 1 -> new Point(
                    rand.nextInt(screenBounds.width) + screenBounds.x,
                    screenBounds.y + screenBounds.height
            );
            case 2 -> new Point(
                    screenBounds.x,
                    rand.nextInt(screenBounds.height) + screenBounds.y
            );
            case 3 -> new Point(
                    screenBounds.x + screenBounds.width,
                    rand.nextInt(screenBounds.height) + screenBounds.y
            );
            default -> throw new IllegalStateException("Unexpected value");
        };
    }

    //helper method to aggregate sleeper Shape objects from multiple segments
    private static List<Shape> createSleeperList(List<TrackSegment> segments) {
        List<Shape> sleepers = new ArrayList<>();

        for (Track segment : segments)
            Collections.addAll(sleepers, segment.getSleepers());

        return sleepers;
    }

    //helper method to aggregate rail Shape objects from multiple segments
    private static List<Shape> createRailList(List<TrackSegment> segments) {
        List<Shape> rails = new ArrayList<>();

        for (Track segment : segments)
                rails.addAll(Arrays.asList(segment.getRails()));

        return rails;
    }

    //helper method to split into pieces and aggregate rail Shape objects from multiple segments
    private static List<Shape> createRailList(List<TrackSegment> segments, int numberOfPieces) {
        List<Shape> rails = new ArrayList<>();
        double step = 1.0 / numberOfPieces;

        for (TrackSegment segment : segments) {
            for (TrackPath railPath : segment.getRailPaths()) {
                double current = 0.0;
                for (int i = 0; i < numberOfPieces; i++) {
                    double start = current;
                    double stop = current + step;
                    Shape slice = railPath.createPath(start, stop, segment.getTrackConfig().getRailThickness());
                    rails.add(slice);
                    current += step;
                }
            }
        }

        return rails;
    }
}
