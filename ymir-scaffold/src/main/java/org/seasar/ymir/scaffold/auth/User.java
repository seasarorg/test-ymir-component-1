package org.seasar.ymir.scaffold.auth;

import java.io.Serializable;

public interface User extends Serializable {
    Long getId();

    String getPassword();

    boolean passwordEquals(String password);

    boolean isInRole(Role... roles);

    boolean isInRole(String... roleNames);

    boolean isInRole(Long... roleIds);
}
