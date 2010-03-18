package org.seasar.ymir.scaffold.auth;

import java.io.Serializable;

import org.seasar.ymir.scaffold.exentity.YsUser;

public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private YsUser user;

    public LoginUser(YsUser user) {
        this.user = user;
    }

    public YsUser getUser() {
        return user;
    }
}
