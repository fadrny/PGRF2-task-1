package solid;

import model.Part;
import model.Vertex;
import transforms.Col;
import transforms.Mat4;
import transforms.Mat4Identity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Solid {
    protected final List<Vertex> vertexBuffer = new ArrayList<>();
    protected final List<Integer> indexBuffer = new ArrayList<>();
    protected final List<Part> partBuffer = new ArrayList<>();

    private String name = getClass().getSimpleName();
    private Mat4 modelMatrix = new Mat4Identity();
    private Col baseColor = new Col(0xffffff);
    private boolean textureEnabled = true;

    public List<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public List<Part> getPartBuffer() {
        return partBuffer;
    }

    public String getName() {
        return name;
    }

    public Solid setName(String name) {
        this.name = name;
        return this;
    }

    public Mat4 getModelMatrix() {
        return modelMatrix;
    }

    public Solid setModelMatrix(Mat4 modelMatrix) {
        this.modelMatrix = modelMatrix;
        return this;
    }

    public Col getBaseColor() {
        return baseColor;
    }

    public Solid setBaseColor(Col baseColor) {
        this.baseColor = baseColor;
        return this;
    }

    public boolean isTextureEnabled() {
        return textureEnabled;
    }

    public Solid setTextureEnabled(boolean textureEnabled) {
        this.textureEnabled = textureEnabled;
        return this;
    }

    public void addIndices(Integer... indices) {
        indexBuffer.addAll(Arrays.asList(indices));
    }
}
