package raster;

import java.util.Optional;

public class DepthBuffer implements Raster<Double>{
    private final double[][] buffer;
    private final int width, height;

    public DepthBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        buffer = new double[width][height];
    }

    @Override
    public void setValue(int x, int y, Double value) {
        // TODO: implementovat
    }

    @Override
    public Optional<Double> getValue(int x, int y) {
        // TODO: implementovat
        return Optional.empty();
    }

    @Override
    public int getWidth() {
        // TODO: implementovat
        return 0;
    }

    @Override
    public int getHeight() {
        // TODO: implementovat
        return 0;
    }

    @Override
    public void clear() {

    }
}
