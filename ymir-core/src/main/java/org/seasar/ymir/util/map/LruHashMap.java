package org.seasar.ymir.util.map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Seasar2.4のLruHashMapです。
 */
public class LruHashMap extends LinkedHashMap {

    private static final long serialVersionUID = 1L;

    protected static final int DEFAULT_INITIAL_CAPACITY = 16;

    protected static final float DEFAULT_LOAD_FACTOR = 0.75f;

    protected int limitSize;

    public LruHashMap(final int limitSize) {
        this(limitSize, DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public LruHashMap(final int limitSize, final int initialCapacity,
            final float loadFactor) {
        super(initialCapacity, loadFactor, true);
        this.limitSize = limitSize;
    }

    public int getLimitSize() {
        return limitSize;
    }

    protected boolean removeEldestEntry(final Map.Entry entry) {
        return size() > limitSize;
    }
}