package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;

public class Cube extends Solid {
    public Cube() {
        setName("Cube");
        setBaseColor(new Col(0x4ecdc4));

        vertexBuffer.add(new Vertex(-0.5, -0.5, -0.5, new Col(0, 0, 0))); // 0
        vertexBuffer.add(new Vertex(0.5, -0.5, -0.5, new Col(1, 0, 0)));  // 1
        vertexBuffer.add(new Vertex(0.5, 0.5, -0.5, new Col(1, 1, 0)));   // 2
        vertexBuffer.add(new Vertex(-0.5, 0.5, -0.5, new Col(0, 1, 0)));  // 3
        
        vertexBuffer.add(new Vertex(-0.5, -0.5, 0.5, new Col(0, 0, 1)));  // 4
        vertexBuffer.add(new Vertex(0.5, -0.5, 0.5, new Col(1, 0, 1)));   // 5
        vertexBuffer.add(new Vertex(0.5, 0.5, 0.5, new Col(1, 1, 1)));    // 6
        vertexBuffer.add(new Vertex(-0.5, 0.5, 0.5, new Col(0, 1, 1)));   // 7

        addIndices(
                0, 1, 1, 2, 2, 3, 3, 0,
                4, 5, 5, 6, 6, 7, 7, 4,
                0, 4, 1, 5, 2, 6, 3, 7
        );
        partBuffer.add(new Part(TopologyType.LINES, 0, 12));

        int triStart = getIndexBuffer().size();
        addIndices(
            0, 1, 2, 0, 2, 3, // Bottom
            4, 5, 6, 4, 6, 7, // Top
            0, 1, 5, 0, 5, 4, // Front
            1, 2, 6, 1, 6, 5, // Right
            2, 3, 7, 2, 7, 6, // Back
            3, 0, 4, 3, 4, 7  // Left
        );
        partBuffer.add(new Part(TopologyType.TRIANGLES, triStart, 12));
    }
}
