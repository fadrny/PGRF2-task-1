package solid;

import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.Col;

public class Axis extends Solid {
    public Axis(String name, double dx, double dy, double dz, Col color) {
        setName(name);
        setBaseColor(color);
        
        vertexBuffer.add(new Vertex(0, 0, 0, color));
        vertexBuffer.add(new Vertex(dx, dy, dz, color));
        
        addIndices(0, 1);
        
        partBuffer.add(new Part(TopologyType.LINES, 0, 1));
    }
}
