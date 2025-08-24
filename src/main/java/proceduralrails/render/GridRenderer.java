package proceduralrails.render;

import proceduralrails.animations.MotionEntity;
import proceduralrails.animations.TrackAnimation;
import proceduralrails.animations.TrackAnimator;
import compositegrid.coordinates.HexagonCoordinate;
import compositegrid.coordinates.TriangleCoordinate;
import compositegrid.coordinates.TriangleDirection;
import compositegrid.grid.HexagonCell;
import compositegrid.grid.TriangleCell;
import compositegrid.render.AbstractGridRenderer;
import proceduralrails.grid.GridEdge;
import proceduralrails.grid.TrackGrid;
import proceduralrails.track.TrackSegment;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class GridRenderer extends AbstractGridRenderer<Object, Object, GridEdge> {
    private final TrackGrid railGrid;
    private final TrackEdgeProvider trackEdgeProvider;
    private final RenderOptions renderOptions;
    private TrackRenderer trackRenderer = new TrackRenderer();
    private TrackAnimator animator;

    public GridRenderer(TrackGrid grid, int canvasWidthPixels, int canvasHeightPixels, double edgeLength, int vertexDiameter, double edgeThickness, TrackEdgeProvider trackEdgeProvider, RenderOptions renderOptions) {
        super(grid, canvasWidthPixels, canvasHeightPixels, edgeLength, vertexDiameter, edgeThickness);
        this.railGrid = grid;
        this.trackEdgeProvider = trackEdgeProvider;
        this.renderOptions = renderOptions;
    }

    public void setAnimator(TrackAnimator animator) {
        this.animator = animator;
    }

    @Override
    protected void firstPaint(Graphics2D g2d) {
        //increased quality?
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        animator.updateAnimations(System.currentTimeMillis());
    }

    @Override
    protected void lastPaint(Graphics2D g2d) {
        for (TrackAnimation animation: animator.getAnimations().values()){
            for (MotionEntity entity : animation.getEntities()) {
                //dont worry about seed. they'll be moving to fast to notice any repetition?
                if (entity.getID().equals("SLEEPER"))
                    trackRenderer.renderSleeper(g2d, entity.getShape(), 999);
                else if (entity.getID().equals("RAIL"))
                    trackRenderer.renderRail(g2d, entity.getShape(), 999);
            }
        }
    }

    @Override
    protected void drawHexagon(Graphics2D g2d, HexagonCoordinate coordinate, HexagonCell<Object, Object, GridEdge> cell, Shape hexagonShape, Point2D vertexPosition, int passNumber) {

    }

    @Override
    protected void drawEdge(Graphics2D g2d, TriangleCoordinate coordinate, GridEdge edge, TriangleDirection direction, Point2D lineStart, Point2D lineFinish, int passNumber) {

    }

    @Override
    protected void drawHexagonBorderEdge(Graphics2D g2d, TriangleCoordinate coordinate, GridEdge edge, TriangleDirection direction, Point2D lineStart, Point2D lineFinish, int passNumber) {

    }

    @Override
    protected void drawVertices(Graphics2D g2d, TriangleCoordinate coordinate, TriangleCell<Object, GridEdge> cell, Point2D vertexPosition, int passNumber) {
        //draw the track half edges surrounding a vertex. could just of easily done this in drawEdge() ?
        for (TriangleDirection direction: TriangleDirection.values()){
            if (triangleGrid.isEdgeInBounds(coordinate, direction))
                if (railGrid.hasTrack(coordinate, direction)){
                    //calculate position of end of edge
                    Point2D trackStop = getCameraAdjustedScreenPosition(coordinate.direction(direction).toFractional());
                    List<TrackSegment> segments = trackEdgeProvider.halfEdges(coordinate, direction, vertexPosition, trackStop);
                    boolean hasAnimation = animator.getAnimations().containsKey(coordinate.hashCode(direction));

                    //if theres an animation only draw the trackbed
                    for (TrackSegment segment: segments) {
                        if (hasAnimation) {
                            if (passNumber == 0 && renderOptions.displayTrackbed())
                                trackRenderer.renderTrackbed(g2d, segment.getTrackbed(), segment.getSeed());
                        }
                        else
                            drawSegment(g2d, segment, passNumber);
                    }
                }
        }
    }

    private void drawSegment(Graphics2D g2d, TrackSegment segment, int passNumber) {
        switch (passNumber){
            case 0 -> {
                if (renderOptions.displayTrackbed())
                    trackRenderer.renderTrackbed(g2d, segment.getTrackbed(), segment.getSeed());
            }

            case 1 -> {
                if (renderOptions.displaySleepers())
                    for (Shape sleeper: segment.getSleepers())
                        trackRenderer.renderSleeper(g2d, sleeper, segment.getSeed());
            }

            case 2 -> {
                if (renderOptions.displayRails())
                    for (Shape rail: segment.getRails())
                        trackRenderer.renderRail(g2d, rail, segment.getSeed());
            }
        }
    }

    @Override
    protected void drawHexagonCentreVertice(Graphics2D g2d, TriangleCoordinate coordinate, TriangleCell<Object, GridEdge> cell, Point2D vertexPosition, int passNumber) {

    }
}
