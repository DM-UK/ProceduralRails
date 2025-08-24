package proceduralrails.track;

public interface TrackConfig {
    double getMaximumSleeperSpacing();
    double getSleeperLength();
    double getSleeperWidth();
    double getRailSpacing();
    double getRailThickness();
    double getTrackbedWidth();
    double getTrackbedEndWidthFactor();
    double getTrackbedMaxSegmentLength();
    int getNumberOfRails();
    default double getRailOffset(int railIndex) {
        if (getNumberOfRails() == 1)
            return 0.0;

        double half = (getNumberOfRails() - 1) / 2.0;
        return (railIndex - half) * getRailSpacing();
    }
}
