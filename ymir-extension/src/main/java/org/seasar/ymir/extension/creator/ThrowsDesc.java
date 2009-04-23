package org.seasar.ymir.extension.creator;

import java.util.Set;

public interface ThrowsDesc {
    ThrowsDesc addThrowable(Class<?> throwableClass);

    ThrowsDesc addThrowable(String throwableClassName);

    String[] getThrowableClassNames();

    String[] getThrowableClassShortNames();

    boolean isEmpty();

    void addDependingClassNamesTo(Set<String> set);

    void setTouchedClassNameSet(Set<String> set);
}
