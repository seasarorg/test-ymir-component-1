package org.seasar.ymir.extension.creator;

import java.util.Map;

import org.seasar.ymir.constraint.ConstraintInterceptor;

public interface BodyDesc extends Cloneable {
    String KEY_ASIS = "asIs";

    String KEY_PERMISSIONDENIED = ConstraintInterceptor.ACTION_PERMISSIONDENIED;

    String PROP_BODY = "body";

    Object clone();

    String getKey();

    void setKey(String key);

    Map<String, Object> getRoot();

    void setRoot(Map<String, Object> root);
}
