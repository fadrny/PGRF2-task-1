package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;
import transforms.Vec2D;
import transforms.Vec3D;

public class Sphere extends Solid {
    public Sphere() {
        setName("Sphere");
        setBaseColor(new Col(0xe71d36));

        int stacks = 16;
        int slices = 16;
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

                Vec3D normal = new Vec3D(x, y, z).normalized().orElse(new Vec3D(0, 0, 1));
                vertexBuffer.add(new Vertex(x, y, z, new Col(1.0, 1.0, 1.0),
                        normal, new Vec2D(u, v)));
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
