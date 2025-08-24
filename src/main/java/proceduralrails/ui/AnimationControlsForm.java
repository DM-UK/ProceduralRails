package proceduralrails.ui;

import proceduralrails.animations.TrackAnimationControls;
import jbasicform.form.JBasicForm;
import jbasicform.slider.DoubleSlider;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class AnimationControlsForm extends JBasicForm implements TrackAnimationControls {
    private final DoubleSlider sleeperVelocitySlider;
    private final JSlider sleeperSpawnIntervalSlider;
    private final DoubleSlider railVelocitySlider;
    private final JSlider railSpawnIntervalSlider;
    private final DoubleSlider destructionVelocity;
    private final JSlider railPiecesSlider;

    public AnimationControlsForm(){
        sleeperSpawnIntervalSlider = addSliderField("Sleeper spawn interval", 1, 1000, 80);
        sleeperVelocitySlider = addSliderField("Sleeper velocity", 0.0, 2.0, 0.75);
        railSpawnIntervalSlider = addSliderField("Rail spawn interval", 1, 1000, 100);
        railVelocitySlider = addSliderField("Rail velocity", 0.0, 2.0, 0.6);
        railPiecesSlider = addSliderField("Rail pieces", 1, 10, 3);
        destructionVelocity = addSliderField("Destroy velocity", 0.0, 2.0, 0.1);
        setBorder(new TitledBorder("Rail Animation Controls"));
    }

    @Override
    public double getSleeperVelocity() {
        return sleeperVelocitySlider.getDoubleValue();
    }

    @Override
    public int getSleeperSpawnInterval() {
        return sleeperSpawnIntervalSlider.getValue();
    }

    @Override
    public double getRailVelocity() {
        return railVelocitySlider.getDoubleValue();
    }

    @Override
    public int getRailSpawnInterval() {
        return railSpawnIntervalSlider.getValue();
    }

    @Override
    public double getDestructionVelocity() {
        return destructionVelocity.getDoubleValue();
    }

    @Override
    public int getRailPieces() {
        return railPiecesSlider.getValue();
    }
}
