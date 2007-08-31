package org.seasar.ymir.hotdeploy.fitter;

public interface HotdeployFitter<T> {
    Class<T> getTargetClass();

    T copy(T value);
}
