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

        // front (z=-0.5)
        Vec3D nFront = new Vec3D(0, 0, -1);
        v(-0.5,-0.5,-0.5, w,nFront, 0,0); v( 0.5,-0.5,-0.5, w,nFront, 1,0);
        v( 0.5, 0.5,-0.5, w,nFront, 1,1); v(-0.5, 0.5,-0.5, w,nFront, 0,1);
        
        // back (z=0.5)
        Vec3D nBack = new Vec3D(0, 0, 1);
        v( 0.5,-0.5, 0.5, w,nBack, 0,0); v(-0.5,-0.5, 0.5, w,nBack, 1,0);
        v(-0.5, 0.5, 0.5, w,nBack, 1,1); v( 0.5, 0.5, 0.5, w,nBack, 0,1);
        
        // bottom (y=-0.5)
        Vec3D nBottom = new Vec3D(0, -1, 0);
        v(-0.5,-0.5, 0.5, w,nBottom, 0,0); v( 0.5,-0.5, 0.5, w,nBottom, 1,0);
        v( 0.5,-0.5,-0.5, w,nBottom, 1,1); v(-0.5,-0.5,-0.5, w,nBottom, 0,1);
        
        // top (y=0.5)
        Vec3D nTop = new Vec3D(0, 1, 0);
        v(-0.5, 0.5,-0.5, w,nTop, 0,0); v( 0.5, 0.5,-0.5, w,nTop, 1,0);
        v( 0.5, 0.5, 0.5, w,nTop, 1,1); v(-0.5, 0.5, 0.5, w,nTop, 0,1);
        
        // left (x=-0.5)
        Vec3D nLeft = new Vec3D(-1, 0, 0);
        v(-0.5,-0.5, 0.5, w,nLeft, 0,0); v(-0.5,-0.5,-0.5, w,nLeft, 1,0);
        v(-0.5, 0.5,-0.5, w,nLeft, 1,1); v(-0.5, 0.5, 0.5, w,nLeft, 0,1);
        
        // right (x=0.5)
        Vec3D nRight = new Vec3D(1, 0, 0);
        v( 0.5,-0.5,-0.5, w,nRight, 0,0); v( 0.5,-0.5, 0.5, w,nRight, 1,0);
        v( 0.5, 0.5, 0.5, w,nRight, 1,1); v( 0.5, 0.5,-0.5, w,nRight, 0,1);

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
