package model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;
import transforms.Vec3D;

public class Vertex implements Vectorizable<Vertex> {
    private final Point3D position;
    private final Col color;
    private final Vec3D normal;
    private final Vec2D uv;
    private final Point3D worldPosition;

    public Vertex(Point3D position, Col color, Vec3D normal, Vec2D uv, Point3D worldPosition) {
        this.position = position;
        this.color = color;
        this.normal = normal;
        this.uv = uv;
        this.worldPosition = worldPosition;
    }

    public Vertex(Point3D position, Col color, Vec3D normal, Vec2D uv) {
        this(position, color, normal, uv, position);
    }

    public Vertex(Point3D position, Col color) {
        this(position, color, new Vec3D(), new Vec2D(), position);
    }

    public Vertex(Point3D position) {
        this(position, new Col(0xffffff));
    }

    public Vertex(double x, double y, double z) {
        this(new Point3D(x, y, z));
    }

    public Vertex(double x, double y, double z, Col color) {
        this(new Point3D(x, y, z), color);
    }

    public Vertex(double x, double y, double z, Col color, Vec3D normal, Vec2D uv) {
        this(new Point3D(x, y, z), color, normal, uv);
    }

    public Point3D getPosition() {
        return position;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public double getZ() {
        return position.getZ();
    }

    public double getW() {
        return position.getW();
    }

    public Col getColor() {
        return color;
    }

    public Vec3D getNormal() {
        return normal;
    }

    public Vec2D getUv() {
        return uv;
    }

    public Point3D getWorldPosition() {
        return worldPosition;
    }

    public Vertex withPosition(Point3D newPosition) {
        return new Vertex(newPosition, color, normal, uv, worldPosition);
    }

    public Vertex withColor(Col newColor) {
        return new Vertex(position, newColor, normal, uv, worldPosition);
    }

    public Vertex withNormal(Vec3D newNormal) {
        return new Vertex(position, color, newNormal, uv, worldPosition);
    }

    public Vertex withUv(Vec2D newUv) {
        return new Vertex(position, color, normal, newUv, worldPosition);
    }

    public Vertex withWorldPosition(Point3D newWorldPosition) {
        return new Vertex(position, color, normal, uv, newWorldPosition);
    }

    @Override
    public Vertex mul(double d) {
        return new Vertex(
                position.mul(d),
                color.mul(d),
                normal.mul(d),
                uv.mul(d),
                worldPosition.mul(d)
        );
    }

    @Override
    public Vertex add(Vertex v) {
        return new Vertex(
                position.add(v.getPosition()),
                color.add(v.getColor()),
                normal.add(v.getNormal()),
                uv.add(v.getUv()),
                worldPosition.add(v.getWorldPosition())
        );
    }
}
