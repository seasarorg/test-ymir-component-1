package org.seasar.ymir.scaffold.auth;

import java.io.Serializable;

public interface Role extends Serializable {
    Long getId();

    String getName();
}
