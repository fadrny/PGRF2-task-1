package util;

import model.Vectorizable;

public class Lerp<E extends Vectorizable<E>> {
    public E lerp(E v1, E v2, double t) {
        return v1.mul(1 - t).add(v2.mul(t));
    }
}
