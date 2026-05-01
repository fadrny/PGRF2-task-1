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
        int triPartIndex = 0;

        for (Part part : solid.getPartBuffer()) {
            if (!isAxis && wireframeMode && part.getType() != model.TopologyType.LINES) {
                if (part.getType() == model.TopologyType.TRIANGLES) triPartIndex++;
                continue;
            }
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
                    java.awt.image.BufferedImage tex = (triPartIndex == 0)
                            ? solid.getTexture()
                            : (solid.getTexture2() != null ? solid.getTexture2() : solid.getTexture());
                    boolean useTex = solid.isTextureEnabled() && tex != null;
                    triangleRasterizer.setTexture(tex, useTex);
                    triPartIndex++;

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
        Vertex tA = a.withPosition(a.getPosition().mul(transformation));
        Vertex tB = b.withPosition(b.getPosition().mul(transformation));
        Vertex tC = c.withPosition(c.getPosition().mul(transformation));

        // near-plane clip a rasterizace
        clipAndRasterize(tA, tB, tC);
    }

    private static final double NEAR_W = 0.001;

    private void clipAndRasterize(Vertex a, Vertex b, Vertex c) {
        double wA = a.getW(), wB = b.getW(), wC = c.getW();

        // kolik vrcholů je před kamerou
        int inside = (wA > NEAR_W ? 1 : 0) + (wB > NEAR_W ? 1 : 0) + (wC > NEAR_W ? 1 : 0);

        if (inside == 0) return;

        if (inside == 3) {
            // celý trojúhelník je viditelný
            rasterizeClipped(a, b, c);
            return;
        }

        // seřadíme tak aby "in" vrcholy byly první
        if (inside == 1) {
            // jeden vrchol před kamerou – najdi ho a dej na pozici a
            if (wB > NEAR_W) { Vertex t = a; a = b; b = t; wA = a.getW(); wB = b.getW(); }
            else if (wC > NEAR_W) { Vertex t = a; a = c; c = t; wA = a.getW(); wC = c.getW(); }
            // a je IN, b a c jsou OUT
            Vertex ab = clipEdge(a, b);
            Vertex ac = clipEdge(a, c);
            rasterizeClipped(a, ab, ac);
        } else {
            // dva vrcholy před kamerou – najdi ten co je OUT a dej na pozici c
            if (wA <= NEAR_W) { Vertex t = a; a = b; b = c; c = t; wA = a.getW(); wB = b.getW(); wC = c.getW(); }
            else if (wB <= NEAR_W) { Vertex t = b; b = c; c = t; wB = b.getW(); wC = c.getW(); }
            // a,b jsou IN, c je OUT
            Vertex ac = clipEdge(a, c);
            Vertex bc = clipEdge(b, c);
            rasterizeClipped(a, b, ac);
            rasterizeClipped(b, bc, ac);
        }
    }

    private Vertex clipEdge(Vertex in, Vertex out) {
        double t = (in.getW() - NEAR_W) / (in.getW() - out.getW());
        return in.mul(1 - t).add(out.mul(t));
    }

    private void rasterizeClipped(Vertex a, Vertex b, Vertex c) {
        double wA = a.getPosition().getW();
        double wB = b.getPosition().getW();
        double wC = c.getPosition().getW();

        Vec3D vA = a.getPosition().dehomog().orElse(null);
        Vec3D vB = b.getPosition().dehomog().orElse(null);
        Vec3D vC = c.getPosition().dehomog().orElse(null);
        if (vA == null || vB == null || vC == null) return;

        // XY frustum reject
        if (vA.getX() < -1 && vB.getX() < -1 && vC.getX() < -1) return;
        if (vA.getX() > 1 && vB.getX() > 1 && vC.getX() > 1) return;
        if (vA.getY() < -1 && vB.getY() < -1 && vC.getY() < -1) return;
        if (vA.getY() > 1 && vB.getY() > 1 && vC.getY() > 1) return;

        int w = lineRasterizer.getRaster().getWidth();
        int h = lineRasterizer.getRaster().getHeight();

        // perspective-correct: uložíme 1/w do position.W a UV vydělíme w
        Vertex tA = a.withPosition(new Point3D(
                (vA.getX() + 1) / 2 * (w - 1),
                (1 - vA.getY()) / 2 * (h - 1),
                vA.getZ(), 1.0 / wA))
                .withUv(new transforms.Vec2D(a.getUv().getX() / wA, a.getUv().getY() / wA));
        Vertex tB = b.withPosition(new Point3D(
                (vB.getX() + 1) / 2 * (w - 1),
                (1 - vB.getY()) / 2 * (h - 1),
                vB.getZ(), 1.0 / wB))
                .withUv(new transforms.Vec2D(b.getUv().getX() / wB, b.getUv().getY() / wB));
        Vertex tC = c.withPosition(new Point3D(
                (vC.getX() + 1) / 2 * (w - 1),
                (1 - vC.getY()) / 2 * (h - 1),
                vC.getZ(), 1.0 / wC))
                .withUv(new transforms.Vec2D(c.getUv().getX() / wC, c.getUv().getY() / wC));

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
