# **ProceduralRails**

Java demonstration of procedurally generated 2D rail track over a triangle lattice grid.

Features: 
- Straight and curved track segments.

- Connections/junctions.

- Configurable dimensions for sleepers, rails, and trackbed.

- Basic animation system.


A more technical description on the drawing of curves is provided [here](TECHNICAL.md).

![](/src/main/resources/image20250822191138.png)

Track segments are represented using the Edge structure of a [CompositeHexagonGrid](https://github.com/DM-UK/CompositeHexagonGrid).

All constitute parts of the track (sleepers, rails, trackbed) are separate AWT Shape objects. Parameters relating to their geometric properties are configurable.

The use of Shape entities allowed for a basic animation system when placing/removing track.

The project can be run by cloning the repo and executing the [demo](https://github.com/DM-UK/ProceduralRails/blob/master/src/main/java/proceduralrails/ProceduralRailsDemo.java).

**Showing the triangle mesh/hexagon structure**
![](/src/main/resources/image20250822191323.png)


**Identical track placement with different track properties**
![](/src/main/resources/image20250823223702.png)

**Demo screenshot**
![](/src/main/resources/image20250823224806.png)
