package compositegrid;

import compositegrid.coordinates.FractionalTriangleCoordinate;
import compositegrid.coordinates.HexagonCoordinate;
import compositegrid.coordinates.TriangleCoordinate;
import compositegrid.coordinates.TriangleDirection;
import compositegrid.grid.CompositeGrid;
import compositegrid.ui.GridSelectionListener;
import compositegrid.ui.HexagonGridPane;

import javax.swing.*;
import java.awt.*;

public class CompositeGridApp implements GridSelectionListener {
    private JFrame frame = new JFrame("Demo");
    private CompositeGrid<String, String, String> demoGrid = new CompositeGrid<String, String, String>(100, 100, String.class, String.class, String.class);
    private HexagonGridPane triangleGridPane = new HexagonGridPane(1200, 800, demoGrid, null);

    public CompositeGridApp(){
        buildGUI();
        triangleGridPane.addGridSelectionListener(this);
    }

    private void buildGUI() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(triangleGridPane, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(panel);
        frame.pack();
    }

    @Override
    public void vertexSelected(TriangleCoordinate coordinate) {
        if (demoGrid.isVertexInBounds(coordinate)){
            var v = demoGrid.getTriangleCell(coordinate).getVertex();
            setOverlaySelection(v);
        }
    }

    @Override
    public void edgeSelected(TriangleCoordinate coordinate, TriangleDirection direction) {
        if (demoGrid.isEdgeInBounds(coordinate, direction)){
            Object e = demoGrid.getTriangleCell(coordinate).getEdge(direction);
            setOverlaySelection(e);
        }
    }

    @Override
    public void hexagonSelected(HexagonCoordinate coordinate) {
        if (demoGrid.isInBounds(coordinate)){
            var h = demoGrid.getHexagonCell(coordinate).getHexagon();
            setOverlaySelection(h);
        }
    }

    @Override
    public void gridSelected(FractionalTriangleCoordinate coordinate) {
    }

    //store selection object in renderer so the selected grid component can be highlighted
    private void setOverlaySelection(Object selection) {
        triangleGridPane.getOverlayRenderer().setSelection(selection);
        triangleGridPane.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CompositeGridApp();
            }
        });
    }
}
