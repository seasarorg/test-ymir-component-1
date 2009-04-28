package org.seasar.ymir.extension.creator;

import java.util.Map;

import org.seasar.ymir.constraint.ConstraintInterceptor;

public interface BodyDesc {
    String KEY_ASIS = "asIs";

    String KEY_PERMISSIONDENIED = ConstraintInterceptor.ACTION_PERMISSIONDENIED;

    String PROP_BODY = "body";

    String getKey();

    void setKey(String key);

    Map<String, Object> getRoot();

    void setRoot(Map<String, Object> root);

    String[] getDependingClassNames();

    void setDependingClassNames(String[] classNames);
}
