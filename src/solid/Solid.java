package solid;

import model.Part;
import model.Vertex;
import transforms.Col;
import transforms.Mat4;
import transforms.Mat4Identity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public abstract class Solid {
    protected final List<Vertex> vertexBuffer = new ArrayList<>();
    protected final List<Integer> indexBuffer = new ArrayList<>();
    protected final List<Part> partBuffer = new ArrayList<>();

    private String name = getClass().getSimpleName();
    private Mat4 modelMatrix = new Mat4Identity();
    private Col baseColor = new Col(0xffffff);
    private boolean textureEnabled = true;
    private BufferedImage texture;
    private BufferedImage texture2;

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

    public BufferedImage getTexture() {
        return texture;
    }

    public Solid setTexture(BufferedImage texture) {
        this.texture = texture;
        return this;
    }

    public Solid loadTexture(String path) {
        try {
            this.texture = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Textura nenalezena: " + path);
        }
        return this;
    }

    public BufferedImage getTexture2() {
        return texture2;
    }

    public Solid loadTexture2(String path) {
        try {
            this.texture2 = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Textura2 nenalezena: " + path);
        }
        return this;
    }

    public void addIndices(Integer... indices) {
        indexBuffer.addAll(Arrays.asList(indices));
    }
}

