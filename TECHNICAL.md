
I shall detail here the steps involved in drawing a curve between two edges, while the geometry is fairly trivial, it can be frustrating to implement if not perfectly clear. 

### Curves

![[Pasted image 20250822155812.png]]
![](/src/main/resources/image20250822155812.png)

First attempt. The arc is too big...

![[Pasted image 20250822162314.png]]
![](/src/main/resources/image20250822162314.png)

Much better! Now let's show how we calculate it:

![[Pasted image 20250822171028.png]]
![](/src/main/resources/image20250822171028.png)

The arc centre is the third vertex of an equilateral triangle between the Edge (dotted line) midpoints. The radius of the arc is the length of a triangles edge (or the radius of the circle inscribed to the hexagon) .

### Half Edges

![[Pasted image 20250822183027.png]]
![](/src/main/resources/image20250822183027.png)

We can represent each **Edge** as a pair of half-edges. One half-edge is paired with its twin (always forming a straight connection). At its other end, the half-edge connects to the next half-edge from an adjacent **Edge**.

### Connections

For every junction that is required we simply create one new half edge, only paying attention to connections of 60 degrees (1 turn around around a vertex). 

![[Pasted image 20250822185927.png]]
![](/src/main/resources/image20250822185927.png)