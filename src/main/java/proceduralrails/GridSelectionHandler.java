package proceduralrails;

import compositegrid.coordinates.FractionalTriangleCoordinate;
import compositegrid.coordinates.HexagonCoordinate;
import compositegrid.coordinates.TriangleCoordinate;
import compositegrid.coordinates.TriangleDirection;
import compositegrid.ui.GridSelectionListener;
import proceduralrails.grid.ObservableTrackGrid;
import proceduralrails.grid.TrackGrid;

public class GridSelectionHandler implements GridSelectionListener {
    private final TrackGrid grid;

    public GridSelectionHandler(ObservableTrackGrid trackGrid) {
        this.grid = trackGrid;
    }

    @Override
    public void vertexSelected(TriangleCoordinate coordinate) {

    }

    @Override
    public void edgeSelected(TriangleCoordinate coordinate, TriangleDirection direction) {
        if (grid.isEdgeInBounds(coordinate, direction)){
            if (!grid.hasTrack(coordinate, direction))
                grid.placeRail(coordinate, direction);
            else
                grid.destroyRail(coordinate, direction);
        }
    }

    @Override
    public void hexagonSelected(HexagonCoordinate coordinate) {

    }

    @Override
    public void gridSelected(FractionalTriangleCoordinate coordinate) {

    }
}
