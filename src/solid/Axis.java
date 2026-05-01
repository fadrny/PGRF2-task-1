package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;

public class Axis extends Solid {
    public Axis(String name, double dx, double dy, double dz, Col color) {
        setName(name);
        setBaseColor(color);

        double h = 0.15;  // šipka - velikost základny
        double back = 0.25; // jak daleko od špičky začíná základna

        // 0: origin
        vertexBuffer.add(new Vertex(0, 0, 0, color));
        // 1: tip
        vertexBuffer.add(new Vertex(dx, dy, dz, color));

        // 4 body základny pyramidy šipky
        if (dx != 0) {
            double bx = dx - Math.signum(dx) * back;
            vertexBuffer.add(new Vertex(bx,  h, 0, color)); // 2
            vertexBuffer.add(new Vertex(bx, -h, 0, color)); // 3
            vertexBuffer.add(new Vertex(bx, 0,  h, color)); // 4
            vertexBuffer.add(new Vertex(bx, 0, -h, color)); // 5
        } else if (dy != 0) {
            double by = dy - Math.signum(dy) * back;
            vertexBuffer.add(new Vertex( h, by, 0, color)); // 2
            vertexBuffer.add(new Vertex(-h, by, 0, color)); // 3
            vertexBuffer.add(new Vertex(0, by,  h, color)); // 4
            vertexBuffer.add(new Vertex(0, by, -h, color)); // 5
        } else {
            double bz = dz - Math.signum(dz) * back;
            vertexBuffer.add(new Vertex( h, 0, bz, color)); // 2
            vertexBuffer.add(new Vertex(-h, 0, bz, color)); // 3
            vertexBuffer.add(new Vertex(0,  h, bz, color)); // 4
            vertexBuffer.add(new Vertex(0, -h, bz, color)); // 5
        }

        // čára od originu k základně šipky
        addIndices(0, 1);
        partBuffer.add(new Part(TopologyType.LINES, 0, 1));

        // 4 trojúhelníky tvořící pyramidu šipky (tip=1, base=2,3,4,5)
        int triStart = getIndexBuffer().size();
        addIndices(
            1, 2, 4,
            1, 4, 3,
            1, 3, 5,
            1, 5, 2
        );
        partBuffer.add(new Part(TopologyType.TRIANGLES, triStart, 4));
    }
}
