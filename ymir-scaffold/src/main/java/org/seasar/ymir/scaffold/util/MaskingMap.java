package org.seasar.ymir.scaffold.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MaskingMap<K, V> implements Map<K, V> {
    private Map<K, V> map;

    private Set<K> visibleKeys;

    public MaskingMap(Map<K, V> map, Collection<K> visibleKeys) {
        this.map = map;
        this.visibleKeys = new HashSet<K>(visibleKeys);
    }

    public void clear() {
        map.clear();
    }

    public boolean containsKey(Object key) {
        if (!visibleKeys.contains(key)) {
            return false;
        }
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = new LinkedHashSet<Entry<K, V>>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (visibleKeys.contains(entry.getKey())) {
                set.add(entry);
            }
        }
        return set;
    }

    public V get(Object key) {
        if (!visibleKeys.contains(key)) {
            return null;
        }
        return map.get(key);
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public Set<K> keySet() {
        Set<K> set = new LinkedHashSet<K>();
        for (K key : map.keySet()) {
            if (visibleKeys.contains(key)) {
                set.add(key);
            }
        }
        return set;
    }

    public V put(K key, V value) {
        if (!visibleKeys.contains(key)) {
            return null;
        }
        return map.put(key, value);
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            if (visibleKeys.contains(entry.getKey())) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public V remove(Object key) {
        if (!visibleKeys.contains(key)) {
            return null;
        }
        return map.remove(key);
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }
}
