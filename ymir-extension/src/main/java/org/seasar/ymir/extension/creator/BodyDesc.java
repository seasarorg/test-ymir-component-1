package org.seasar.ymir.extension.creator;

import java.util.Map;

public interface BodyDesc extends Cloneable {

    Object clone();

    String getKey();

    void setKey(String key);

    Map<String, Object> getRoot();

    void setRoot(Map<String, Object> root);
}
