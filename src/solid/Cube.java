package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;
import transforms.Vec2D;
import transforms.Vec3D;

public class Cube extends Solid {
    public Cube() {
        setName("Cube");
        setBaseColor(new Col(0x4ecdc4));

        Col w = new Col(1.0, 1.0, 1.0);
        Vec3D n = new Vec3D();

        // front (z=-0.5)
        v(-0.5,-0.5,-0.5, w,n, 0,0); v( 0.5,-0.5,-0.5, w,n, 1,0);
        v( 0.5, 0.5,-0.5, w,n, 1,1); v(-0.5, 0.5,-0.5, w,n, 0,1);
        // back (z=0.5)
        v( 0.5,-0.5, 0.5, w,n, 0,0); v(-0.5,-0.5, 0.5, w,n, 1,0);
        v(-0.5, 0.5, 0.5, w,n, 1,1); v( 0.5, 0.5, 0.5, w,n, 0,1);
        // bottom (y=-0.5)
        v(-0.5,-0.5, 0.5, w,n, 0,0); v( 0.5,-0.5, 0.5, w,n, 1,0);
        v( 0.5,-0.5,-0.5, w,n, 1,1); v(-0.5,-0.5,-0.5, w,n, 0,1);
        // top (y=0.5)
        v(-0.5, 0.5,-0.5, w,n, 0,0); v( 0.5, 0.5,-0.5, w,n, 1,0);
        v( 0.5, 0.5, 0.5, w,n, 1,1); v(-0.5, 0.5, 0.5, w,n, 0,1);
        // left (x=-0.5)
        v(-0.5,-0.5, 0.5, w,n, 0,0); v(-0.5,-0.5,-0.5, w,n, 1,0);
        v(-0.5, 0.5,-0.5, w,n, 1,1); v(-0.5, 0.5, 0.5, w,n, 0,1);
        // right (x=0.5)
        v( 0.5,-0.5,-0.5, w,n, 0,0); v( 0.5,-0.5, 0.5, w,n, 1,0);
        v( 0.5, 0.5, 0.5, w,n, 1,1); v( 0.5, 0.5,-0.5, w,n, 0,1);

        // drátový model
        for (int f = 0; f < 6; f++) {
            int b = f * 4;
            addIndices(b, b+1, b+1, b+2, b+2, b+3, b+3, b);
        }
        partBuffer.add(new Part(TopologyType.LINES, 0, 24));

        // trojúhelníky
        int triStart = getIndexBuffer().size();
        for (int f = 0; f < 6; f++) {
            int b = f * 4;
            addIndices(b, b+1, b+2, b, b+2, b+3);
        }
        partBuffer.add(new Part(TopologyType.TRIANGLES, triStart, 12));
    }

    private void v(double x, double y, double z, Col c, Vec3D n, double u, double uv) {
        vertexBuffer.add(new Vertex(x, y, z, c, n, new Vec2D(u, uv)));
    }
}
