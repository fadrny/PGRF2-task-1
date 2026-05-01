package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;
import transforms.Vec2D;
import transforms.Vec3D;

public class Cylinder extends Solid {
    public Cylinder() {
        setName("Cylinder");
        setBaseColor(new Col(0xff9f1c));

        int sides = 20;
        double radius = 0.5;
        double halfH = 0.5;
        Col c = new Col(1.0, 1.0, 1.0);
        Vec3D n = new Vec3D();

        // plášť – každý segment má vlastní 4 vrcholy s UV
        for (int i = 0; i < sides; i++) {
            double a0 = (2.0 * Math.PI * i) / sides;
            double a1 = (2.0 * Math.PI * (i + 1)) / sides;
            double u0 = (double) i / sides;
            double u1 = (double) (i + 1) / sides;

            Vec3D n0 = new Vec3D(Math.cos(a0), Math.sin(a0), 0).normalized().orElse(new Vec3D(1,0,0));
            Vec3D n1 = new Vec3D(Math.cos(a1), Math.sin(a1), 0).normalized().orElse(new Vec3D(1,0,0));

            vertexBuffer.add(new Vertex(Math.cos(a0)*radius, Math.sin(a0)*radius,  halfH, c, n0, new Vec2D(u0, 0)));
            vertexBuffer.add(new Vertex(Math.cos(a1)*radius, Math.sin(a1)*radius,  halfH, c, n1, new Vec2D(u1, 0)));
            vertexBuffer.add(new Vertex(Math.cos(a1)*radius, Math.sin(a1)*radius, -halfH, c, n1, new Vec2D(u1, 1)));
            vertexBuffer.add(new Vertex(Math.cos(a0)*radius, Math.sin(a0)*radius, -halfH, c, n0, new Vec2D(u0, 1)));
        }

        // čáry pláště
        for (int i = 0; i < sides; i++) {
            int b = i * 4;
            addIndices(b, b+1, b+1, b+2, b+2, b+3, b+3, b);
        }
        partBuffer.add(new Part(TopologyType.LINES, 0, sides * 4));

        // trojúhelníky pláště
        int sideTriStart = getIndexBuffer().size();
        for (int i = 0; i < sides; i++) {
            int b = i * 4;
            addIndices(b, b+1, b+2, b, b+2, b+3);
        }
        partBuffer.add(new Part(TopologyType.TRIANGLES, sideTriStart, sides * 2));

        // podstavy – středový vrchol + obvod
        Vec3D nTop = new Vec3D(0, 0, 1);
        Vec3D nBot = new Vec3D(0, 0, -1);

        int topCenter = vertexBuffer.size();
        vertexBuffer.add(new Vertex(0, 0, halfH, c, nTop, new Vec2D(0.5, 0.5)));
        for (int i = 0; i <= sides; i++) {
            double a = (2.0 * Math.PI * i) / sides;
            double u = 0.5 + 0.5 * Math.cos(a);
            double v = 0.5 + 0.5 * Math.sin(a);
            vertexBuffer.add(new Vertex(Math.cos(a)*radius, Math.sin(a)*radius, halfH, c, nTop, new Vec2D(u, v)));
        }

        int botCenter = vertexBuffer.size();
        vertexBuffer.add(new Vertex(0, 0, -halfH, c, nBot, new Vec2D(0.5, 0.5)));
        for (int i = 0; i <= sides; i++) {
            double a = (2.0 * Math.PI * i) / sides;
            double u = 0.5 + 0.5 * Math.cos(a);
            double v = 0.5 + 0.5 * Math.sin(a);
            vertexBuffer.add(new Vertex(Math.cos(a)*radius, Math.sin(a)*radius, -halfH, c, nBot, new Vec2D(u, v)));
        }

        // trojúhelníky podstav
        int capTriStart = getIndexBuffer().size();
        int capTris = 0;
        for (int i = 0; i < sides; i++) {
            addIndices(topCenter, topCenter + 1 + i, topCenter + 2 + i);
            addIndices(botCenter, botCenter + 2 + i, botCenter + 1 + i);
            capTris += 2;
        }
        partBuffer.add(new Part(TopologyType.TRIANGLES, capTriStart, capTris));
    }
}
