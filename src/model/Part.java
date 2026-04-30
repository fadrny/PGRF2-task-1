package model;

public class Part {
    private final TopologyType type;
    private final int startIndex;
    private final int count;

    public Part(TopologyType type, int startIndex, int count) {
        this.type = type;
        this.startIndex = startIndex;
        this.count = count;
    }

    public TopologyType getType() {
        return type;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getCount() {
        return count;
    }
}
