package org.seasar.ymir.extension.creator.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.seasar.ymir.extension.creator.ThrowsDesc;
import org.seasar.ymir.util.ClassUtils;

public class ThrowsDescImpl implements ThrowsDesc {
    private Set<String> throwableClassNameSet_ = new LinkedHashSet<String>();

    private Set<String> touchedClassNameSet_ = new HashSet<String>();

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

    public String[] getThrowableClassShortNames() {
        List<String> list = new ArrayList<String>();
        for (String className : throwableClassNameSet_) {
            touchedClassNameSet_.add(className);
            list.add(ClassUtils.getShortName(className));
        }
        return list.toArray(new String[0]);
    }

    public boolean isEmpty() {
        return throwableClassNameSet_.isEmpty();
    }

    public void addDependingClassNamesTo(Set<String> set) {
        for (String className : throwableClassNameSet_) {
            set.add(className);
        }
    }

    public void setTouchedClassNameSet(Set<String> set) {
        if (set == touchedClassNameSet_) {
            return;
        }

        touchedClassNameSet_ = set;
    }
}
