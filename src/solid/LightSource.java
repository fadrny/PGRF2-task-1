package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;
import transforms.Vec2D;
import transforms.Vec3D;

/**
 * Malá žlutá koule reprezentující zdroj světla ve scéně.
 * Vykresluje se vlastní barvou (osvětlení se na ni nepočítá).
 */
public class LightSource extends Solid {
    public LightSource() {
        setName("Light");
        setBaseColor(new Col(1.0, 1.0, 1.0));

        int stacks = 8;
        int slices = 8;
        double radius = 0.15;
        Col white = new Col(1.0, 1.0, 1.0);

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
                vertexBuffer.add(new Vertex(x, y, z, white, normal, new Vec2D(u, v)));
            }
        }

        // drátový model
        int lineCount = 0;
        for (int i = 0; i <= stacks; i++) {
            for (int j = 0; j < slices; j++) {
                int a = i * (slices + 1) + j;
                addIndices(a, a + 1);
                lineCount++;
            }
        }
        for (int i = 0; i < stacks; i++) {
            for (int j = 0; j <= slices; j++) {
                int a = i * (slices + 1) + j;
                addIndices(a, a + slices + 1);
                lineCount++;
            }
        }
        partBuffer.add(new Part(TopologyType.LINES, 0, lineCount));

        // trojúhelníky
        int triStart = getIndexBuffer().size();
        int triCount = 0;
        for (int i = 0; i < stacks; i++) {
            for (int j = 0; j < slices; j++) {
                int a = i * (slices + 1) + j;
                int b = a + 1;
                int c = a + slices + 1;
                int d = c + 1;
                addIndices(a, b, c, b, d, c);
                triCount += 2;
            }
        }
        partBuffer.add(new Part(TopologyType.TRIANGLES, triStart, triCount));
    }
}
