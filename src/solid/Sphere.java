package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;

public class Sphere extends Solid {
    public Sphere() {
        setName("Sphere");
        setBaseColor(new Col(0xe71d36));

        int outerSegments = 28;
        int horizontalSegments = 20;
        int verticalSegments = 20;

        int outerStart = vertexBuffer.size();
        addEllipse(390, 305, 95, 95, 0.50, outerSegments);
        addLoopIndices(outerStart, outerSegments);

        int horizontalStart = vertexBuffer.size();
        addEllipse(390, 305, 95, 35, 0.47, horizontalSegments);
        addLoopIndices(horizontalStart, horizontalSegments);

        int verticalStart = vertexBuffer.size();
        addEllipse(390, 305, 35, 95, 0.53, verticalSegments);
        addLoopIndices(verticalStart, verticalSegments);

        partBuffer.add(new Part(TopologyType.LINES, 0, outerSegments + horizontalSegments + verticalSegments));
    }

    private void addEllipse(double centerX, double centerY, double radiusX, double radiusY, double z, int segments) {
        for (int i = 0; i < segments; i++) {
            double angle = (2.0 * Math.PI * i) / segments;
            double x = centerX + Math.cos(angle) * radiusX;
            double y = centerY + Math.sin(angle) * radiusY;
            vertexBuffer.add(new Vertex(x, y, z));
        }
    }

    private void addLoopIndices(int startIndex, int segments) {
        for (int i = 0; i < segments; i++) {
            int next = startIndex + ((i + 1) % segments);
            addIndices(startIndex + i, next);
        }
    }
}
