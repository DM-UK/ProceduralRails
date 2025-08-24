package proceduralrails.animations;


import compositegrid.coordinates.TriangleCoordinate;
import compositegrid.coordinates.TriangleDirection;
import compositegrid.render.AbstractGridRenderer;
import compositegrid.ui.HexagonGridPane;
import proceduralrails.grid.TrackGridListener;
import proceduralrails.render.TrackEdgeProvider;
import proceduralrails.track.TrackSegment;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Responsible for the coordinating of track animations.
 * 1) Listens for placed/removal events and builds the corresponding animation.
 * 2) Updates and removes animations as they progress and finish.
 * Should probably seperate these if things get any more complex?
 */
public class TrackAnimator implements TrackAnimationListener, TrackGridListener {
    private final HexagonGridPane pane;
    private final TrackEdgeProvider trackEdgeProvider;
    private final TrackAnimationCreator animationCreator;
    private final Map<Integer, TrackAnimation> animations = new HashMap<>();

    public TrackAnimator(HexagonGridPane pane, TrackEdgeProvider trackEdgeProvider, TrackAnimationCreator animationCreator) {
        this.pane = pane;
        this.trackEdgeProvider = trackEdgeProvider;
        this.animationCreator = animationCreator;
    }

    public Map<Integer, TrackAnimation> getAnimations() {
        return animations;
    }

    public void updateAnimations(long t) {
        Iterator<TrackAnimation> it = animations.values().iterator();

        while (it.hasNext()) {
            TrackAnimation animation = it.next();
            animation.update(t);
            if (animation.isFinished())
                it.remove();
        }
    }

    @Override
    public void animationFinished(TrackAnimation trackAnimation) {
        if (animations.size() == 1)//last animation is notifying but hasnt yet been removed
            pane.enableScrolling();
    }

    @Override
    public void entitySpawned(TrackAnimation trackAnimation, String entityName) {

    }

    @Override
    public void entityInFinalPosition(TrackAnimation trackAnimation, String entityName) {

    }

    @Override
    public void railPlaced(TriangleCoordinate coordinate, TriangleDirection direction) {
        pane.disableScrolling();
        List<TrackSegment> segments = buildSegments(coordinate, direction);
        TrackAnimation animation = animationCreator.placeTrack(segments);
        addAnimation(animation, coordinate.hashCode(direction));
    }

    @Override
    public void railRemoved(TriangleCoordinate coordinate, TriangleDirection direction) {
        pane.disableScrolling();
        List<TrackSegment> segments = buildSegments(coordinate, direction);
        TrackAnimation animation = animationCreator.destroyTrack(segments);
        animations.put(coordinate.hashCode(direction), animation);
        animation.addListener(this);
    }

    private List<TrackSegment> buildSegments(TriangleCoordinate coordinate, TriangleDirection direction) {
        AbstractGridRenderer renderer = pane.getRenderer();
        Point2D from = renderer.getCameraAdjustedScreenPosition(coordinate.toFractional());
        Point2D to = renderer.getCameraAdjustedScreenPosition(
                coordinate.direction(direction).toFractional()
        );

        return trackEdgeProvider.twoHalfEdges(coordinate, direction, from, to);
    }

    private void addAnimation(TrackAnimation animation, int hash) {
        animations.put(hash, animation);
        animation.addListener(this);
    }
}
