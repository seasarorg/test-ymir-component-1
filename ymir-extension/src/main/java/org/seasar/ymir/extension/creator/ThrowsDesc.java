package org.seasar.ymir.extension.creator;

public interface ThrowsDesc {
    ThrowsDesc addThrowable(Class<?> throwableClass);

    ThrowsDesc addThrowable(String throwableClassName);

    String[] getThrowableClassNames();

    boolean isEmpty();
}
