package raster;

import transforms.Col;

public class ZBuffer {
    private final Raster<Col> imageBuffer;
    private final Raster<Double> depthBuffer;

    public ZBuffer(Raster<Col> imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void setPixelWithZTest(int x, int y, double z, Col color) {
        // TODO: načtu hodnotu Z z depthbufferu
        // TODO: porovnám staré a nové z
        // TODO: rozhodnu, jestli
        // TODO:    a) končím, nic se nestane
        // TODO:    b) obarvím pixel a updatuju hodnotu v depth bufferu

        // tohle jen pro debug
        imageBuffer.setValue(x, y, color);
    }

    // TODO: metoda clear
}
