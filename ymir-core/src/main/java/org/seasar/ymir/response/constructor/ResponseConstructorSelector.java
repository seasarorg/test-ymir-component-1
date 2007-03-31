package org.seasar.ymir.response.constructor;

public interface ResponseConstructorSelector {

    boolean hasResponseConstructor(Class<?> type);

    <T> ResponseConstructor<T> getResponseConstructor(Class<T> type);
}
