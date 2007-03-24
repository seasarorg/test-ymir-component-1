package org.seasar.ymir.extension.creator;


public interface BodyDesc extends Cloneable {

    Object clone();

    String getKey();

    void setKey(String key);

    Object getRoot();

    void setRoot(Object root);
}
