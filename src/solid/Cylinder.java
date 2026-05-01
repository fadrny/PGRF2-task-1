package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;

public class Cylinder extends Solid {
    public Cylinder() {
        setName("Cylinder");
        setBaseColor(new Col(0xff9f1c));

        int sides = 20;
        double radius = 0.5;
        double height = 1.0;
        double halfHeight = height / 2.0;

        for (int i = 0; i < sides; i++) {
            double angle = (2.0 * Math.PI * i) / sides;
            double x = Math.cos(angle) * radius;
            double y = Math.sin(angle) * radius;
            double r = Math.max(0, Math.min(1, x + 0.5));
            double g = Math.max(0, Math.min(1, y + 0.5));
            double bTop = Math.max(0, Math.min(1, halfHeight + 0.5));
            vertexBuffer.add(new Vertex(x, y, halfHeight, new Col(r, g, bTop)));
        }

        for (int i = 0; i < sides; i++) {
            double angle = (2.0 * Math.PI * i) / sides;
            double x = Math.cos(angle) * radius;
            double y = Math.sin(angle) * radius;
            double r = Math.max(0, Math.min(1, x + 0.5));
            double g = Math.max(0, Math.min(1, y + 0.5));
            double bBottom = Math.max(0, Math.min(1, -halfHeight + 0.5));
            vertexBuffer.add(new Vertex(x, y, -halfHeight, new Col(r, g, bBottom)));
        }

        for (int i = 0; i < sides; i++) {
            int next = (i + 1) % sides;
            addIndices(i, next);
            addIndices(i + sides, next + sides);
            addIndices(i, i + sides);
        }

        partBuffer.add(new Part(TopologyType.LINES, 0, sides * 3));

        int triStart = getIndexBuffer().size();
        int numTriangles = 0;
        
        for (int i = 0; i < sides; i++) {
            int next = (i + 1) % sides;
            addIndices(i, next, i + sides);
            addIndices(next, next + sides, i + sides);
            numTriangles += 2;
        }
        
        for (int i = 1; i < sides - 1; i++) {
            addIndices(0, i, i + 1);
            numTriangles++;
        }
        for (int i = 1; i < sides - 1; i++) {
            addIndices(sides, sides + i, sides + i + 1);
            numTriangles++;
        }

        partBuffer.add(new Part(TopologyType.TRIANGLES, triStart, numTriangles));
    }
}
