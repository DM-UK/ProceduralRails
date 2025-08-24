package proceduralrails.ui;

import jbasicform.form.JBasicForm;
import jbasicform.slider.DoubleSlider;
import proceduralrails.track.TrackConfig;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class TrackConfigForm extends JBasicForm implements TrackConfig {
    private final DoubleSlider maximumSleeperSpacingSlider;
    private final DoubleSlider sleeperLengthSlider;
    private final DoubleSlider sleeperWidthSlider;
    private final DoubleSlider trackSpacingSlider;
    private final DoubleSlider trackThicknessSlider;
    private final JSlider numberOfRailsSlider;
    private final DoubleSlider trackbedWidthSlider;
    private final DoubleSlider maximumTrackbedSpacingSlider;
    private final DoubleSlider trackbedEndWidthFactorSlider;

    public TrackConfigForm() {
        maximumSleeperSpacingSlider = addSliderField("Maximum sleeper spacing", 0.0, 50.0, 12.0);
        sleeperLengthSlider = addSliderField("Sleeper length", 0.0, 100.0, 36.0);
        sleeperWidthSlider = addSliderField("Sleeper width", 0.0, 25.0, 6.0);
        trackSpacingSlider = addSliderField("Rail spacing", 0.0, 30.0, 21.0);
        trackThicknessSlider = addSliderField("Rail thickness", 0.0, 20.0, 3);
        numberOfRailsSlider = addSliderField("Number of rails", 1, 10, 2);
        trackbedWidthSlider = addSliderField("Trackbed width", 0.0, 150.0, 44.0);
        trackbedEndWidthFactorSlider = addSliderField("Trackbed end width factor", 0.0, 1.0, 0.45);
        maximumTrackbedSpacingSlider = addSliderField("Max trackbed seg length", 0.0, 150.0, 10);
        setBorder(new TitledBorder("Track Configuration"));
    }

    @Override
    public double getMaximumSleeperSpacing() {
        return maximumSleeperSpacingSlider.getDoubleValue();
    }

    @Override
    public double getSleeperLength() {
        return sleeperLengthSlider.getDoubleValue();
    }

    @Override
    public double getSleeperWidth() {
        return sleeperWidthSlider.getDoubleValue();
    }

    @Override
    public double getRailSpacing() {
        return trackSpacingSlider.getDoubleValue();
    }

    @Override
    public double getRailThickness() {
        return trackThicknessSlider.getDoubleValue();
    }

    @Override
    public double getTrackbedWidth() {
        return trackbedWidthSlider.getDoubleValue();
    }

    @Override
    public double getTrackbedEndWidthFactor() {
        return trackbedEndWidthFactorSlider.getDoubleValue();
    }

    @Override
    public double getTrackbedMaxSegmentLength() {
        return maximumTrackbedSpacingSlider.getDoubleValue();
    }

    @Override
    public int getNumberOfRails() {
        return numberOfRailsSlider.getValue();
    }
}