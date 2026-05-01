package renderer;

import model.Part;
import model.Vertex;
import rasterize.LineRasterizer;
import rasterize.TriangleRasterizer;
import solid.Solid;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;
import transforms.Vec3D;

public class RendererSolid {
    private final LineRasterizer lineRasterizer;
    private final TriangleRasterizer triangleRasterizer;
    
    private Mat4 view = new Mat4Identity();
    private Mat4 projection = new Mat4Identity();
    private boolean wireframeMode = true;

    public RendererSolid(LineRasterizer lineRasterizer, TriangleRasterizer triangleRasterizer) {
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizer = triangleRasterizer;
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProjection(Mat4 projection) {
        this.projection = projection;
    }

    public void setWireframeMode(boolean wireframeMode) {
        this.wireframeMode = wireframeMode;
    }

    public void render(Solid solid) {
        Mat4 transformation = solid.getModelMatrix().mul(view).mul(projection);
        boolean isAxis = solid.getName().startsWith("Osa");

        for (Part part : solid.getPartBuffer()) {
            if (!isAxis && wireframeMode && part.getType() != model.TopologyType.LINES) continue;
            if (!isAxis && !wireframeMode && part.getType() != model.TopologyType.TRIANGLES) continue;

            switch (part.getType()) {
                case LINES:
                    lineRasterizer.setColor(solid.getBaseColor());
                    int index = part.getStartIndex();
                    for (int i = 0; i < part.getCount(); i++) {
                        int indexA = solid.getIndexBuffer().get(index++);
                        int indexB = solid.getIndexBuffer().get(index++);

                        Vertex a = solid.getVertexBuffer().get(indexA);
                        Vertex b = solid.getVertexBuffer().get(indexB);

                        transformAndRasterizeLine(a, b, transformation);
                    }
                    break;

                case TRIANGLES:
                    index = part.getStartIndex();
                    for (int i = 0; i < part.getCount(); i++) {
                        int indexA = solid.getIndexBuffer().get(index++);
                        int indexB = solid.getIndexBuffer().get(index++);
                        int indexC = solid.getIndexBuffer().get(index++);

                        Vertex a = solid.getVertexBuffer().get(indexA);
                        Vertex b = solid.getVertexBuffer().get(indexB);
                        Vertex c = solid.getVertexBuffer().get(indexC);

                        transformAndRasterizeTriangle(a, b, c, transformation);
                    }
                    break;
            }
        }
    }

    private void transformAndRasterizeTriangle(Vertex a, Vertex b, Vertex c, Mat4 transformation) {
        Point3D pA = a.getPosition().mul(transformation);
        Point3D pB = b.getPosition().mul(transformation);
        Point3D pC = c.getPosition().mul(transformation);

        if (pA.getW() <= 0 && pB.getW() <= 0 && pC.getW() <= 0) return;

        Vec3D vA = pA.dehomog().get();
        Vec3D vB = pB.dehomog().get();
        Vec3D vC = pC.dehomog().get();

        if (vA.getX() < -1 && vB.getX() < -1 && vC.getX() < -1) return;
        if (vA.getX() > 1 && vB.getX() > 1 && vC.getX() > 1) return;
        if (vA.getY() < -1 && vB.getY() < -1 && vC.getY() < -1) return;
        if (vA.getY() > 1 && vB.getY() > 1 && vC.getY() > 1) return;

        int w = lineRasterizer.getRaster().getWidth();
        int h = lineRasterizer.getRaster().getHeight();

        double x1 = (vA.getX() + 1) / 2 * (w - 1);
        double y1 = (1 - vA.getY()) / 2 * (h - 1);
        double x2 = (vB.getX() + 1) / 2 * (w - 1);
        double y2 = (1 - vB.getY()) / 2 * (h - 1);
        double x3 = (vC.getX() + 1) / 2 * (w - 1);
        double y3 = (1 - vC.getY()) / 2 * (h - 1);

        Vertex tA = a.withPosition(new Point3D(x1, y1, vA.getZ()));
        Vertex tB = b.withPosition(new Point3D(x2, y2, vB.getZ()));
        Vertex tC = c.withPosition(new Point3D(x3, y3, vC.getZ()));

        triangleRasterizer.rasterize(tA, tB, tC);
    }

    private void transformAndRasterizeLine(Vertex a, Vertex b, Mat4 transformation) {
        Point3D pA = a.getPosition().mul(transformation);
        Point3D pB = b.getPosition().mul(transformation);

        if (pA.getW() <= 0 && pB.getW() <= 0) return;
        if (pA.getW() <= 0 || pB.getW() <= 0) return;

        Vec3D vA = pA.dehomog().get();
        Vec3D vB = pB.dehomog().get();

        int w = lineRasterizer.getRaster().getWidth();
        int h = lineRasterizer.getRaster().getHeight();

        int x1 = (int) Math.round((vA.getX() + 1) / 2 * (w - 1));
        int y1 = (int) Math.round((1 - vA.getY()) / 2 * (h - 1));
        int x2 = (int) Math.round((vB.getX() + 1) / 2 * (w - 1));
        int y2 = (int) Math.round((1 - vB.getY()) / 2 * (h - 1));

        lineRasterizer.rasterize(x1, y1, x2, y2);
    }
}
