package compositegrid.ui;

import javax.swing.*;

public class GridOverlayBar extends JMenuBar implements GridOverlayView {
    private final JCheckBoxMenuItem subMenuItemCoordinatesTriangle;
    private final JCheckBoxMenuItem subMenuItemCoordinatesHexagon;
    private final JCheckBoxMenuItem subMenuItemViewVertices;
    private final JCheckBoxMenuItem subMenuItemViewEdges;
    private final JCheckBoxMenuItem subMenuItemViewHexagons;

    public GridOverlayBar(){
        JMenu menuView=new JMenu("View");
        JMenu menuCoordinates=new JMenu("Coordinates");
        this.subMenuItemViewVertices=new JCheckBoxMenuItem("Vertices", false);
        this.subMenuItemViewEdges=new JCheckBoxMenuItem("Edges");
        this.subMenuItemViewHexagons=new JCheckBoxMenuItem("Hexagons", false);
        menuView.add(subMenuItemViewVertices);
        menuView.add(subMenuItemViewEdges);
        menuView.add(subMenuItemViewHexagons);
        this.subMenuItemCoordinatesTriangle=new JCheckBoxMenuItem("Triangle");
        this.subMenuItemCoordinatesHexagon=new JCheckBoxMenuItem("Hexagon");
        menuCoordinates.add(subMenuItemCoordinatesTriangle);
        menuCoordinates.add(subMenuItemCoordinatesHexagon);
        add(menuView);
        add(menuCoordinates);
    }

    @Override
    public boolean displayTriangleCoordinates(){
        return subMenuItemCoordinatesTriangle.isSelected();
    }

    @Override
    public boolean displayHexagonCoordinates(){
        return subMenuItemCoordinatesHexagon.isSelected();
    }

    @Override
    public boolean displayVertices() {
        return subMenuItemViewVertices.isSelected();
    }

    @Override
    public boolean displayEdges() {
        return subMenuItemViewEdges.isSelected();
    }

    @Override
    public boolean displayHexagons() {
        return subMenuItemViewHexagons.isSelected();
    }
}
