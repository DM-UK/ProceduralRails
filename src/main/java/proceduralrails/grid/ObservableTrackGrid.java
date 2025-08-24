package proceduralrails.grid;

import compositegrid.coordinates.TriangleCoordinate;
import compositegrid.coordinates.TriangleDirection;

import java.util.ArrayList;

public class ObservableTrackGrid extends TrackGrid {
    private ArrayList<TrackGridListener> listeners = new ArrayList<>();

    public ObservableTrackGrid(int hexagonWidth, int hexagonHeight) {
        super(hexagonWidth, hexagonHeight);
    }

    public boolean placeRail(TriangleCoordinate coordinate, TriangleDirection direction){
        boolean wasPlaced = super.placeRail(coordinate, direction);

        if (wasPlaced)
            for (TrackGridListener l: listeners)
                l.railPlaced(coordinate, direction);

        return wasPlaced;
    }

    public boolean destroyRail(TriangleCoordinate coordinate, TriangleDirection direction){
        boolean wasDestroyed = super.destroyRail(coordinate, direction);

        if (wasDestroyed)
            for (TrackGridListener l: listeners)
                l.railRemoved(coordinate, direction);

        return wasDestroyed;
    }

    public void addListener(TrackGridListener l){
        listeners.add(l);
    }
}
