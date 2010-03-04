package org.seasar.ymir.util.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 値の型が配列であるようなMapの値をコンポーネント型として取り出すためのビュークラスです。
 * <p>コンストラクタで与えたMapの値がnullまたは空の配列の場合、このクラスの{@link #get(Object)}
 * メソッドはnullを返します。そうでない場合は配列の最初の値を返します。
 * </p>
 * <p>このクラスは読み出し専用です。
 * 内容を変更するようなメソッドを呼び出すと{@link UnsupportedOperationException}がスローされます。
 * </p>
 * 
 * @param <K> キーの型。
 * @param <V> 値の型。
 * @since 1.0.7
 */
public class FirstValueMap<K, V> implements Map<K, V> {
    private Map<K, V[]> map;

    public FirstValueMap(Map<K, V[]> map) {
        this.map = map;
    }

    public void clear() {
        map.clear();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Set<java.util.Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public V get(Object key) {
        V[] v = map.get(key);
        if (v == null || v.length == 0) {
            return null;
        } else {
            return v[0];
        }
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public V put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends K, ? extends V> t) {
        throw new UnsupportedOperationException();
    }

    public V remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return map.size();
    }

    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }
}
