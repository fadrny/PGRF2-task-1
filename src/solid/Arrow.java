package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;

public class Arrow extends Solid {
    public Arrow() {
        vertexBuffer.add(new Vertex(200, 300, 0.5)); // v0
        vertexBuffer.add(new Vertex(400, 300, 0.5)); // v1
        vertexBuffer.add(new Vertex(400, 340, 0.5, new Col(0xff0000))); // v2
        vertexBuffer.add(new Vertex(360, 300, 0.5)); // v3
        vertexBuffer.add(new Vertex(400, 260, 0.5)); // v4

        addIndices(0, 1); // lines
        addIndices(4, 3, 2); // triangles

        partBuffer.add(new Part(TopologyType.LINES, 0, 1));
        partBuffer.add(new Part(TopologyType.TRIANGLES, 2, 1));
    }
}
