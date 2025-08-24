package proceduralrails.ui;

import jbasicform.form.JBasicForm;
import proceduralrails.render.RenderOptions;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class RenderOptionsForm extends JBasicForm implements RenderOptions {
    private final ButtonGroup trackbedRadioButton;
    private final ButtonGroup sleeperRadioButton;
    private final ButtonGroup railRadioButton;

    public RenderOptionsForm() {
        trackbedRadioButton = addTrueFalseField("Show track Bed", true);
        sleeperRadioButton = addTrueFalseField("Show sleepers", true);
        railRadioButton = addTrueFalseField("Show rails", true);
        setBorder(new TitledBorder("Display Options"));
    }

    @Override
    public boolean displayTrackbed() {
        return "True".equals(trackbedRadioButton.getSelection().getActionCommand());
    }

    @Override
    public boolean displaySleepers() {
        return "True".equals(sleeperRadioButton.getSelection().getActionCommand());
    }

    @Override
    public boolean displayRails() {
        return "True".equals(railRadioButton.getSelection().getActionCommand());
    }
}