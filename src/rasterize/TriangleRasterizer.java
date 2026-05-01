package rasterize;

import model.Vertex;
import raster.ZBuffer;
import transforms.Col;
import util.Lerp;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    public void rasterize(Vertex a, Vertex b, Vertex c) {
        if (a.getY() > b.getY()) { Vertex temp = a; a = b; b = temp; }
        if (b.getY() > c.getY()) { Vertex temp = b; b = c; c = temp; }
        if (a.getY() > b.getY()) { Vertex temp = a; a = b; b = temp; }

        if (a.getY() == c.getY()) return;

        int startY1 = Math.max(0, (int) Math.round(a.getY()));
        int endY1 = Math.min(zBuffer.getHeight(), (int) Math.round(b.getY()));

        for (int y = startY1; y < endY1; y++) {
            double dy1 = b.getY() - a.getY();
            double t1 = (dy1 == 0) ? 0 : (y - a.getY()) / dy1;
            t1 = Math.max(0.0, Math.min(1.0, t1));
            Vertex ab = a.mul(1 - t1).add(b.mul(t1));

            double dy2 = c.getY() - a.getY();
            double t2 = (dy2 == 0) ? 0 : (y - a.getY()) / dy2;
            t2 = Math.max(0.0, Math.min(1.0, t2));
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));

            fillLine(y, ab, ac);
        }

        int startY2 = Math.max(0, (int) Math.round(b.getY()));
        int endY2 = Math.min(zBuffer.getHeight(), (int) Math.round(c.getY()));

        for (int y = startY2; y < endY2; y++) {
            double dy1 = c.getY() - b.getY();
            double t1 = (dy1 == 0) ? 0 : (y - b.getY()) / dy1;
            t1 = Math.max(0.0, Math.min(1.0, t1));
            Vertex bc = b.mul(1 - t1).add(c.mul(t1));

            double dy2 = c.getY() - a.getY();
            double t2 = (dy2 == 0) ? 0 : (y - a.getY()) / dy2;
            t2 = Math.max(0.0, Math.min(1.0, t2));
            Vertex ac = a.mul(1 - t2).add(c.mul(t2));

            fillLine(y, bc, ac);
        }
    }

    private void fillLine(int y, Vertex v1, Vertex v2) {
        if (v1.getX() > v2.getX()) {
            Vertex temp = v1;
            v1 = v2;
            v2 = temp;
        }

        int xStart = Math.max(0, (int) Math.round(v1.getX()));
        int xEnd = Math.min(zBuffer.getWidth(), (int) Math.round(v2.getX()));

        double dx = v2.getX() - v1.getX();

        for (int x = xStart; x < xEnd; x++) {
            double t = (dx == 0) ? 0 : (x - v1.getX()) / dx;
            t = Math.max(0.0, Math.min(1.0, t));
            Vertex pixel = v1.mul(1 - t).add(v2.mul(t));
            zBuffer.setPixelWithZTest(x, y, pixel.getPosition().getZ(), pixel.getColor());
        }
    }
}
