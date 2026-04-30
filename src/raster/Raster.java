package raster;

import java.util.Optional;

public interface Raster<E> {
    void setValue(int x, int y, E value);
    Optional<E> getValue(int x, int y);
    int getWidth();
    int getHeight();
    void clear();
}
