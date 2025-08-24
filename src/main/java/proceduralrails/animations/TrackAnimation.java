package proceduralrails.animations;

import java.util.ArrayList;
import java.util.List;

/** Represents a single track animation. Responsible for calling updatePosition(t) of its entities and for notifying listeners of its entities state changes */
public class TrackAnimation {
    private final List<MotionEntity> entities = new ArrayList<>();
    private final List<MotionEntity> finishedEntities = new ArrayList<>();
    private final List<TrackAnimationListener> listeners = new ArrayList<>();

    public void update(long t) {
        for (MotionEntity entity : entities) {
            MotionEntityState stateChange = entity.updateState(t);
            entity.updatePosition(t);

            if (stateChange != null) {
                notifyListenersOfStateChange(entity, stateChange);
                if (stateChange == MotionEntityState.FINAL_POSITION)
                    finishedEntities.add(entity);
            }
        }

        if (isFinished()) {
            for (TrackAnimationListener l : listeners)
                l.animationFinished(this);
        }
    }

    public void addEntity(MotionEntity e) {
        entities.add(e);
    }

    public void addListener(TrackAnimationListener l) {
        listeners.add(l);
    }

    public boolean isFinished() {
        return finishedEntities.size() == entities.size() && !entities.isEmpty();
    }

    private void notifyListenersOfStateChange(MotionEntity entity, MotionEntityState state) {
        for (TrackAnimationListener l : listeners){

        if (state == MotionEntityState.MOVING)
            l.entitySpawned(this, entity.getID());
        else if (state == MotionEntityState.FINAL_POSITION)
            l.entityInFinalPosition(this, entity.getID());
        }
    }

    public List<MotionEntity> getEntities() {
        return entities;
    }
}