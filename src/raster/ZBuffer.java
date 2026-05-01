package raster;

import transforms.Col;

import java.util.Optional;

public class ZBuffer {
    private final Raster<Col> imageBuffer;
    private final Raster<Double> depthBuffer;

    public ZBuffer(Raster<Col> imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void setPixelWithZTest(int x, int y, double z, Col color) {
        if (!isInside(x, y) || Double.isNaN(z) || Double.isInfinite(z) || z < 0.0 || z > 1.0) {
            return;
        }

        Optional<Double> currentDepth = depthBuffer.getValue(x, y);
        if (currentDepth.isEmpty()) {
            return;
        }

        if (z <= currentDepth.get()) {
            depthBuffer.setValue(x, y, z);
            imageBuffer.setValue(x, y, color);
        }
    }

    public void clear() {
        depthBuffer.clear();
    }

    private boolean isInside(int x, int y) {
        return x >= 0 && x < imageBuffer.getWidth() && y >= 0 && y < imageBuffer.getHeight();
    }

    public int getWidth() {
        return imageBuffer.getWidth();
    }

    public int getHeight() {
        return imageBuffer.getHeight();
    }
}
