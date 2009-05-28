package org.seasar.ymir.extension.creator;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.seasar.ymir.extension.creator.util.DescUtils;

public class Born<T extends Comparable<T>> implements Comparable<Born<T>> {
    private Set<String> bornOf_ = new TreeSet<String>();

    private T element_;

    public Born() {
    }

    public Born(T element) {
        setElement(element);
    }

    public Born(T element, String[] bornOf) {
        setElement(element);
        setBornOf(bornOf);
    }

    @Override
    public String toString() {
        return "element=" + element_ + ", bornOf=" + bornOf_;
    }

    public int compareTo(Born<T> o) {
        return element_.compareTo(o.getElement());
    }

    public String[] getBornOf() {
        return bornOf_.toArray(new String[0]);
    }

    public void setBornOf(String... bornOf) {
        bornOf_.clear();
        addBornOf(bornOf);
    }

    public void addBornOf(String... bornOf) {
        if (bornOf != null) {
            bornOf_.addAll(Arrays.asList(bornOf));
        }
    }

    public T getElement() {
        return element_;
    }

    public void setElement(T element) {
        element_ = element;
    }

    public boolean removeBornOf(String bornOf) {
        if (bornOf == null || bornOf_.isEmpty()) {
            return false;
        }

        bornOf_.remove(bornOf);

        return bornOf_.isEmpty();
    }

    public AnnotationDesc getBornOfAnnotationDesc() {
        return DescUtils.newBornOfMetaAnnotationDesc(getBornOf());
    }
}
