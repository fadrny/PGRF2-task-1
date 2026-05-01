package controller;

import raster.ZBuffer;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.TriangleRasterizer;
import renderer.RendererSolid;
import solid.Cube;
import solid.Cylinder;
import solid.Solid;
import solid.Sphere;
import solid.Axis;
import transforms.*;
import view.Panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Controller3D {
    private final Panel panel;
    private final ZBuffer zBuffer;
    private final RendererSolid renderer;

    private final List<Solid> scene = new java.util.ArrayList<>();
    private final List<Solid> axes;
    private Camera camera;
    private final Mat4 projectionPersp;
    private final Mat4 projectionOrtho;
    private boolean usePerspective = true;
    private boolean wireframeMode = false;
    
    private int activeSolidIndex = 0;
    private int lastX, lastY;
    private boolean useLighting = true;
    private transforms.Vec3D lightPosition = new transforms.Vec3D(0, 0, 0);

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.zBuffer = new ZBuffer(panel.getRaster());
        LineRasterizer lineRasterizer = new LineRasterizerGraphics(panel.getRaster());
        TriangleRasterizer triangleRasterizer = new TriangleRasterizer(zBuffer);
        this.renderer = new RendererSolid(lineRasterizer, triangleRasterizer);

        // Osy musí být inicializovány před voláním drawScene()
        Solid axisX = new Axis("Osa X", 1, 0, 0, new transforms.Col(255, 0, 0));
        Solid axisY = new Axis("Osa Y", 0, 1, 0, new transforms.Col(0, 255, 0));
        Solid axisZ = new Axis("Osa Z", 0, 0, 1, new transforms.Col(0, 0, 255));
        this.axes = List.of(axisX, axisY, axisZ);

        camera = new Camera(new Vec3D(3, -8, 5), Math.toRadians(90), Math.toRadians(-30), 1.0, true);
        projectionPersp = new Mat4PerspRH(Math.PI / 4, 600.0 / 800.0, 0.1, 100.0);
        projectionOrtho = new Mat4OrthoRH(10, 10 * 600.0 / 800.0, 0.1, 100.0);

        resetScene();
        initListeners();
        drawScene();
    }

    private void resetScene() {
        Solid cube = new Cube();
        Solid sphere = new Sphere();
        Solid cylinder = new Cylinder();

        cube.setModelMatrix(new Mat4Transl(2, 0, 0));
        sphere.setModelMatrix(new Mat4Transl(4, 0, 0));
        cylinder.setModelMatrix(new Mat4Transl(6, 0, 0));

        cube.loadTexture("textures/t1.jpg");
        sphere.loadTexture("textures/t2.jpg");
        cylinder.loadTexture("textures/c1.jpg");
        cylinder.loadTexture2("textures/c2.jpg");

        Solid light = new solid.LightSource();
        light.setModelMatrix(new Mat4Transl(6, -2, 3));

        scene.clear();
        scene.addAll(List.of(cube, sphere, cylinder, light));
        activeSolidIndex = 0;

        // Reset kamery a nastavení
        camera = new Camera(new Vec3D(3, -8, 5), Math.toRadians(90), Math.toRadians(-30), 1.0, true);
        usePerspective = true;
        wireframeMode = false;
        useLighting = true;
    }

    private void initListeners() {
        panel.setFocusTraversalKeysEnabled(false);
        
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - lastX;
                int dy = e.getY() - lastY;
                lastX = e.getX();
                lastY = e.getY();

                double sensitivity = 0.01;
                camera = camera.addAzimuth(-dx * sensitivity);
                camera = camera.addZenith(-dy * sensitivity);
                drawScene();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                double speed = 0.2;
                Solid active = scene.get(activeSolidIndex);

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        camera = camera.forward(speed);
                        break;
                    case KeyEvent.VK_S:
                        camera = camera.backward(speed);
                        break;
                    case KeyEvent.VK_A:
                        camera = camera.left(speed);
                        break;
                    case KeyEvent.VK_D:
                        camera = camera.right(speed);
                        break;
                    case KeyEvent.VK_SPACE:
                        camera = camera.up(speed);
                        break;
                    case KeyEvent.VK_SHIFT:
                    case KeyEvent.VK_C:
                        camera = camera.down(speed);
                        break;

                    case KeyEvent.VK_R:
                        resetScene();
                        break;
                        
                    case KeyEvent.VK_P:
                        usePerspective = !usePerspective;
                        break;
                        
                    case KeyEvent.VK_Q:
                        wireframeMode = !wireframeMode;
                        break;
                        
                    case KeyEvent.VK_T:
                        active.setTextureEnabled(!active.isTextureEnabled());
                        break;
                        
                    case KeyEvent.VK_TAB:
                        activeSolidIndex = (activeSolidIndex + 1) % scene.size();
                        break;
                        
                    case KeyEvent.VK_Z:
                    case KeyEvent.VK_Y:
                        active.setModelMatrix(new Mat4Transl(0.1, 0, 0).mul(active.getModelMatrix()));
                        break;
                    case KeyEvent.VK_U:
                        active.setModelMatrix(new Mat4Transl(-0.1, 0, 0).mul(active.getModelMatrix()));
                        break;
                        
                    case KeyEvent.VK_H:
                        active.setModelMatrix(new Mat4Transl(0, -0.1, 0).mul(active.getModelMatrix()));
                        break;
                    case KeyEvent.VK_J:
                        active.setModelMatrix(new Mat4Transl(0, 0.1, 0).mul(active.getModelMatrix()));
                        break;
                        
                    case KeyEvent.VK_V:
                        active.setModelMatrix(new Mat4Transl(0, 0, -0.1).mul(active.getModelMatrix()));
                        break;
                    case KeyEvent.VK_B:
                        active.setModelMatrix(new Mat4Transl(0, 0, 0.1).mul(active.getModelMatrix()));
                        break;
                        
                    case KeyEvent.VK_I:
                        active.setModelMatrix(new Mat4RotX(0.1).mul(active.getModelMatrix()));
                        break;
                    case KeyEvent.VK_O:
                        active.setModelMatrix(new Mat4RotX(-0.1).mul(active.getModelMatrix()));
                        break;
                        
                    case KeyEvent.VK_K:
                        active.setModelMatrix(new Mat4RotY(0.1).mul(active.getModelMatrix()));
                        break;

                    case KeyEvent.VK_L:
                        active.setModelMatrix(new Mat4RotY(-0.1).mul(active.getModelMatrix()));
                        break;
                        
                    case KeyEvent.VK_F:
                        useLighting = !useLighting;
                        break;
                        
                    case KeyEvent.VK_N:
                        active.setModelMatrix(new Mat4RotZ(0.1).mul(active.getModelMatrix()));
                        break;
                    case KeyEvent.VK_M:
                        active.setModelMatrix(new Mat4RotZ(-0.1).mul(active.getModelMatrix()));
                        break;
                        
                    case KeyEvent.VK_ADD:
                    case KeyEvent.VK_PLUS:
                    case KeyEvent.VK_EQUALS:
                        active.setModelMatrix(new Mat4Scale(1.1).mul(active.getModelMatrix()));
                        break;
                    case KeyEvent.VK_SUBTRACT:
                    case KeyEvent.VK_MINUS:
                        active.setModelMatrix(new Mat4Scale(0.9).mul(active.getModelMatrix()));
                        break;
                }
                drawScene();
            }
        });
    }

    private void drawScene() {
        panel.getRaster().clear();
        zBuffer.clear();

        // Odvod pozice světla z modelMatrix světelného tělesa
        for (Solid s : scene) {
            if (s.getName().equals("Light")) {
                transforms.Point3D origin = new transforms.Point3D(0, 0, 0);
                transforms.Point3D worldPos = origin.mul(s.getModelMatrix());
                lightPosition = new transforms.Vec3D(worldPos.getX(), worldPos.getY(), worldPos.getZ());
                break;
            }
        }

        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(usePerspective ? projectionPersp : projectionOrtho);
        renderer.setWireframeMode(wireframeMode);
        renderer.setLighting(lightPosition, camera.getPosition(), useLighting);

        for (Solid solid : scene) {
            renderer.render(solid);
        }

        for (Solid axis : axes) {
            renderer.render(axis);
        }

        Solid activeSolid = scene.get(activeSolidIndex);
        java.awt.Graphics g = panel.getRaster().getImage().getGraphics();
        g.setColor(java.awt.Color.WHITE);
        String activeName = activeSolid.getName().equals("Light")
                ? "Light (posun: Z/U/H/J/V/B)"
                : activeSolid.getName();
        g.drawString("Aktivni teleso (Tab): " + activeName, 10, 20);
        g.drawString("Projekce (P): " + (usePerspective ? "Perspektivni" : "Ortogonalni"), 10, 40);
        g.drawString("Mod (Q): " + (wireframeMode ? "Dratovy" : "Vyplneny"), 10, 60);
        g.drawString("Textura (T): " + (activeSolid.isTextureEnabled() && activeSolid.getTexture() != null ? "Zapnuta" : "Vypnuta"), 10, 80);
        g.drawString("Osvetleni (F): " + (useLighting ? "Zapnuto" : "Vypnuto"), 10, 100);
        g.drawString(String.format("Svetlo @ (%.1f, %.1f, %.1f)", lightPosition.getX(), lightPosition.getY(), lightPosition.getZ()), 10, 120);

        panel.repaint();
    }
}
