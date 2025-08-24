package proceduralrails.animations;


public interface TrackAnimationListener {
    void animationFinished(TrackAnimation trackAnimation);

    void entitySpawned(TrackAnimation trackAnimation, String entityName);

    void entityInFinalPosition(TrackAnimation trackAnimation, String entityName);
}
