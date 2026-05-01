package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;

public class Cube extends Solid {
    public Cube() {
        setName("Cube");
        setBaseColor(new Col(0x4ecdc4));

        vertexBuffer.add(new Vertex(110, 310, 0.55)); // 0
        vertexBuffer.add(new Vertex(230, 310, 0.55)); // 1
        vertexBuffer.add(new Vertex(230, 430, 0.55)); // 2
        vertexBuffer.add(new Vertex(110, 430, 0.55)); // 3
        vertexBuffer.add(new Vertex(155, 265, 0.45)); // 4
        vertexBuffer.add(new Vertex(275, 265, 0.45)); // 5
        vertexBuffer.add(new Vertex(275, 385, 0.45)); // 6
        vertexBuffer.add(new Vertex(155, 385, 0.45)); // 7

        addIndices(
                0, 1, 1, 2, 2, 3, 3, 0,
                4, 5, 5, 6, 6, 7, 7, 4,
                0, 4, 1, 5, 2, 6, 3, 7
        );

        partBuffer.add(new Part(TopologyType.LINES, 0, 12));
    }
}
