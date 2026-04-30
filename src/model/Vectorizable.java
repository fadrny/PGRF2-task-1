package model;

public interface Vectorizable<E> {
    E mul(double d);
    E add(E v);
}
