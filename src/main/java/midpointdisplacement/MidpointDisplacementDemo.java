package midpointdisplacement;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class MidpointDisplacementDemo {
    public MidpointDisplacementDemo()
    {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Composite Bezier Curve Demo");
        frame.setSize(1200, 800);
        JPanel panel = createPreviewPane();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JPanel createPreviewPane() {
        return new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setStroke(new BasicStroke(1.0f));
                int seed = (int) System.currentTimeMillis();

                for (int y = 0; y < 5; y++) {
                    Point2D start = new Point2D.Double(50, 100 + y * 150);
                    Point2D finish = new Point2D.Double(500, 100 + y * 150);
                    drawDisplacedPath(g2d, 6, 60, 0.6 + (y*0.2d), start, finish, seed);

                    start = new Point2D.Double(550, start.getY());
                    finish = new Point2D.Double(1000, finish.getY());
                    drawDisplacedPath(g2d, 6, 140, 0.6 + (y*0.2d), start, finish, seed);
                }
            }
        };
    }

    private void drawDisplacedPath(Graphics2D g2d, int steps, int maximumDisplacement, double roughness, Point2D start, Point2D finish, int seed) {
        MidpointDisplacedPath displacedPath = new MidpointDisplacedPath(steps, maximumDisplacement, roughness, seed, MidpointDisplacedPath.COMPOSITE_BEZIER_CURVE);
        displacedPath.moveTo(start.getX(), start.getY());
        displacedPath.displacedLineTo(finish.getX(), finish.getY());
        g2d.draw(displacedPath);
        String rounded = String.format("%.1f", roughness);
        g2d.drawString("roughness = "+rounded, (int) start.getX(), (int) (start.getY() - 25));
        g2d.drawString("maximumDisplacement = "+maximumDisplacement, (int) start.getX(), (int) (start.getY() - 40));
    }

    public static void main(String[] args) {
        new MidpointDisplacementDemo();
    }
}
