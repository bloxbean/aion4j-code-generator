package org.aion4j.avm.codegenerator.util;

public class Tuple<K,V> {
    private K k;
    private V v;

    public Tuple(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K _1() {
        return k;
    }

    public V _2() {
        return v;
    }
}
