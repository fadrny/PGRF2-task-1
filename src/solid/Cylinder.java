package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;

public class Cylinder extends Solid {
    public Cylinder() {
        setName("Cylinder");
        setBaseColor(new Col(0xff9f1c));

        int sides = 24;
        double centerX = 590;
        double topY = 220;
        double bottomY = 400;
        double radiusX = 75;
        double radiusY = 24;
        double zTop = 0.42;
        double zBottom = 0.58;

        for (int i = 0; i < sides; i++) {
            double angle = (2.0 * Math.PI * i) / sides;
            double x = centerX + Math.cos(angle) * radiusX;
            double y = topY + Math.sin(angle) * radiusY;
            vertexBuffer.add(new Vertex(x, y, zTop));
        }

        for (int i = 0; i < sides; i++) {
            double angle = (2.0 * Math.PI * i) / sides;
            double x = centerX + Math.cos(angle) * radiusX;
            double y = bottomY + Math.sin(angle) * radiusY;
            vertexBuffer.add(new Vertex(x, y, zBottom));
        }

        for (int i = 0; i < sides; i++) {
            int next = (i + 1) % sides;
            addIndices(i, next);
            addIndices(i + sides, next + sides);
            addIndices(i, i + sides);
        }

        partBuffer.add(new Part(TopologyType.LINES, 0, sides * 3));
    }
}
