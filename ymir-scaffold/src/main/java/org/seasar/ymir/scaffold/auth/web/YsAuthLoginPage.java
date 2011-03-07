package org.seasar.ymir.scaffold.auth.web;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Response;
import org.seasar.ymir.annotation.DefaultReturn;
import org.seasar.ymir.annotation.SuppressUpdating;
import org.seasar.ymir.constraint.annotation.Required;
import org.seasar.ymir.constraint.annotation.ValidationFailed;
import org.seasar.ymir.scaffold.auth.LoginUser;
import org.seasar.ymir.scaffold.auth.User;
import org.seasar.ymir.scaffold.auth.UserManager;
import org.seasar.ymir.scaffold.auth.annotation.SuppressLoginCheck;
import org.seasar.ymir.scaffold.util.Redirect;
import org.seasar.ymir.scaffold.web.ScaffoldPageBase;
import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.scope.annotation.RequestParameter;
import org.seasar.ymir.scope.impl.SessionScope;
import org.seasar.ymir.session.annotation.InvalidateSession;
import org.seasar.ymir.util.StringUtils;

@SuppressUpdating
@SuppressLoginCheck
@DefaultReturn("/WEB-INF/zpt/scaffold/auth/login.html")
public class YsAuthLoginPage extends ScaffoldPageBase {
    @Binding(bindingType = BindingType.MUST)
    protected UserManager userManager;

    private LoginUser loginUser;

    private String redirectionURL;

    private String name;

    private String password;

    @RequestParameter
    public void setName(String name) {
        this.name = name;
    }

    @RequestParameter
    public void setPassword(String password) {
        this.password = password;
    }

    @In(SessionScope.class)
    public void setRedirectionURL(String redirectionURL) {
        this.redirectionURL = redirectionURL;
    }

    @ValidationFailed
    public void validationFailed() {
        getNotes().clear();
        addNote("error.auth.login.failed");
    }

    public void _get() {
    }

    @Required(value = "name", namePrefixOnNote = "ys-auth-")
    @InvalidateSession
    public Response _post() {
        User user = login(name, password);
        if (user != null) {
            loginUser = new LoginUser(user);
            if (StringUtils.isEmpty(redirectionURL)) {
                return whereAfterLogin();
            } else {
                return Redirect.to(redirectionURL);
            }
        } else {
            addNote("error.auth.login.failed");
            return passthrough();
        }
    }

    protected Response whereAfterLogin() {
        return Redirect.to("/");
    }

    @InvalidateSession
    public void _get_logout() {
    }

    protected User login(String name, String password) {
        User user = userManager.getUserByName(name);
        if (user != null && user.passwordEquals(password)) {
            return user;
        } else {
            return null;
        }
    }

    @Out(SessionScope.class)
    public LoginUser getLoginUser() {
        return loginUser;
    }
}
