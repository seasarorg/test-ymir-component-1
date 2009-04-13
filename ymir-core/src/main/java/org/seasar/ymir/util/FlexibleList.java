package org.seasar.ymir.util;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * 指定されたインデックスに従って自動的に大きさを拡張するList実装です。
 * 
 * @since 1.0.2
 */
public class FlexibleList<E> extends AbstractList<E> implements List<E>,
        RandomAccess {
    private static final int DEFAULT_MAXINDEX = 999;

    private static final int MAXINDEX_UNLIMITED = -1;

    private SortedMap<Integer, E> map = new TreeMap<Integer, E>();

    private int maxIndex = DEFAULT_MAXINDEX;

    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }

    public void add(int index, E element) {
        if (!validateIndex(index)) {
            return;
        }

        if (map.isEmpty()) {
            map.put(index, element);
        } else {
            SortedMap<Integer, E> m = new TreeMap<Integer, E>();
            for (Iterator<Map.Entry<Integer, E>> itr = map.tailMap(index)
                    .entrySet().iterator(); itr.hasNext();) {
                Entry<Integer, E> entry = itr.next();
                m.put(entry.getKey().intValue() + 1, entry.getValue());
                itr.remove();
            }

            map.put(index, element);

            for (Map.Entry<Integer, E> entry : m.entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private boolean validateIndex(int index) {
        if (maxIndex == MAXINDEX_UNLIMITED) {
            return index >= 0;
        } else {
            return index >= 0 && index <= maxIndex;
        }
    }

    public E get(int index) {
        return map.get(index);
    }

    public E remove(int index) {
        return map.remove(index);
    }

    public E set(int index, E element) {
        if (!validateIndex(index)) {
            return null;
        }

        return map.put(index, element);
    }

    public int size() {
        if (map.isEmpty()) {
            return 0;
        } else {
            return map.lastKey().intValue() + 1;
        }
    }
}
