package org.seasar.ymir;

public interface PageComponent extends Acceptor<PageComponentVisitor> {
    Object getPage();

    Class<?> getPageClass();

    <T> T getRelatedObject(Class<T> clazz);

    <T> void setRelatedObject(Class<T> clazz, T object);

    PageComponent[] getChildren();

    PageComponent[] getDescendants();
}
