package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;

public class Sphere extends Solid {
    public Sphere() {
        setName("Sphere");
        setBaseColor(new Col(0xe71d36));

        int stacks = 10;
        int slices = 10;
        double radius = 0.5;

        for (int i = 0; i <= stacks; i++) {
            double v = (double) i / stacks;
            double phi = v * Math.PI;

            for (int j = 0; j <= slices; j++) {
                double u = (double) j / slices;
                double theta = u * 2 * Math.PI;

                double x = radius * Math.sin(phi) * Math.cos(theta);
                double y = radius * Math.sin(phi) * Math.sin(theta);
                double z = radius * Math.cos(phi);

                double r = Math.max(0, Math.min(1, x + 0.5));
                double g = Math.max(0, Math.min(1, y + 0.5));
                double b = Math.max(0, Math.min(1, z + 0.5));

                vertexBuffer.add(new Vertex(x, y, z, new Col(r, g, b)));
            }
        }

        int lineCount = 0;
        for (int i = 0; i <= stacks; i++) {
            for (int j = 0; j < slices; j++) {
                int a = i * (slices + 1) + j;
                int b = a + 1;
                addIndices(a, b);
                lineCount++;
            }
        }
        
        for (int i = 0; i < stacks; i++) {
            for (int j = 0; j <= slices; j++) {
                int a = i * (slices + 1) + j;
                int b = a + (slices + 1);
                addIndices(a, b);
                lineCount++;
            }
        }

        partBuffer.add(new Part(TopologyType.LINES, 0, lineCount));

        int triStart = getIndexBuffer().size();
        int numTriangles = 0;
        
        for (int i = 0; i < stacks; i++) {
            for (int j = 0; j < slices; j++) {
                int a = i * (slices + 1) + j;
                int b = a + 1;
                int c = a + (slices + 1);
                int d = c + 1;
                
                addIndices(a, b, c);
                addIndices(b, d, c);
                numTriangles += 2;
            }
        }
        
        partBuffer.add(new Part(TopologyType.TRIANGLES, triStart, numTriangles));
    }
}
