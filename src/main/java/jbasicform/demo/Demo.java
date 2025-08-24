package jbasicform.demo;

import javax.swing.*;

public class Demo {
    private JFrame frame = new JFrame("Demo");
    private JPanel exampleForm = new ExampleForm();

    public Demo() {
        buildGUI();
    }

    private void buildGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.add(exampleForm);
        frame.pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Demo();
            }
        });

    }
}
