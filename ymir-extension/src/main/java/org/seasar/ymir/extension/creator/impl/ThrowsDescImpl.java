package org.seasar.ymir.extension.creator.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import org.seasar.ymir.extension.creator.ThrowsDesc;

public class ThrowsDescImpl implements ThrowsDesc {

    private Set<String> throwableClassNameSet_ = new LinkedHashSet<String>();

    public ThrowsDescImpl() {
    }

    public ThrowsDescImpl(Class<?> throwableClass) {
        addThrowable(throwableClass);
    }

    public ThrowsDescImpl(String throwableClassName) {
        addThrowable(throwableClassName);
    }

    public ThrowsDesc addThrowable(Class<?> throwableClass) {
        return addThrowable(throwableClass.getName());
    }

    public ThrowsDesc addThrowable(String throwableClassName) {
        throwableClassNameSet_.add(throwableClassName);
        return this;
    }

    public String[] getThrowableClassNames() {
        return throwableClassNameSet_.toArray(new String[0]);
    }

    public boolean isEmpty() {
        return throwableClassNameSet_.isEmpty();
    }
}
