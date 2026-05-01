package controller;

import raster.ZBuffer;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.TriangleRasterizer;
import renderer.RendererSolid;
import solid.Arrow;
import solid.Cube;
import solid.Cylinder;
import solid.Solid;
import solid.Sphere;
import transforms.*;
import view.Panel;

import java.util.List;


public class Controller3D {
    private final Panel panel;
    private final ZBuffer zBuffer;
    private final LineRasterizer lineRasterizer;
    private final TriangleRasterizer triangleRasterizer;
    private final RendererSolid renderer;

    private final List<Solid> scene;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.zBuffer = new ZBuffer(panel.getRaster());
        this.lineRasterizer = new LineRasterizerGraphics(panel.getRaster());
        this.triangleRasterizer = new TriangleRasterizer(zBuffer);
        this.renderer = new RendererSolid(lineRasterizer, triangleRasterizer);

        Solid arrow = new Arrow().setName("Axis Arrow").setBaseColor(new Col(0xffffff));
        Solid cube = new Cube();
        Solid sphere = new Sphere();
        Solid cylinder = new Cylinder();
        this.scene = List.of(arrow, cube, sphere, cylinder);

        initListeners();

        drawScene();
    }

    private void initListeners() {
        // TODO: Inicializace listenerů např. pohyb kamerou
    }

    private void drawScene() {
        panel.getRaster().clear();
        zBuffer.clear();

//        zBuffer.setPixelWithZTest(50, 50, 0.5, new Col(0xff0000)); // 0.5
//        zBuffer.setPixelWithZTest(50, 50, 0.7, new Col(0x00ff00)); // 0.7


//        triangleRasterizer.rasterize(
//            new Vertex(400, 0, 0.5, new Col(0xff0000)),
//            new Vertex(0, 300, 0.5, new Col(0x00ff00)),
//            new Vertex(799, 599, 0.5, new Col(0x0000ff))
//        );

        for (Solid solid : scene) {
            renderer.render(solid);
        }

        panel.repaint();
    }
}
