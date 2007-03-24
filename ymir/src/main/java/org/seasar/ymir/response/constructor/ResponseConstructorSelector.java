package org.seasar.cms.ymir.response.constructor;

public interface ResponseConstructorSelector {

    boolean hasResponseConstructor(Class<?> type);

    <T> ResponseConstructor<T> getResponseConstructor(Class<T> type);
}
