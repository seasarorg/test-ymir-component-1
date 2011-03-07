package org.seasar.ymir.scaffold.auth;

import java.io.Serializable;

public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private User user;

    public LoginUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean isInRole(Role... roles) {
        return user.isInRole(roles);
    }

    public boolean isInRole(String... roleNames) {
        return user.isInRole(roleNames);
    }

    public boolean isInRole(Long... roleIds) {
        return user.isInRole(roleIds);
    }
}
