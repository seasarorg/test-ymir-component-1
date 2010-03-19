package org.seasar.ymir.scaffold.web;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Response;
import org.seasar.ymir.constraint.annotation.PermissionDenied;
import org.seasar.ymir.scaffold.SiteManager;
import org.seasar.ymir.scaffold.util.PageBase;
import org.seasar.ymir.scaffold.util.Redirect;

abstract public class ScaffoldPageBase extends PageBase {
    private SiteManager siteManager;

    @Binding(bindingType = BindingType.MUST)
    public final void setSiteManager(SiteManager siteManager) {
        this.siteManager = siteManager;
    }

    protected final SiteManager getSiteManager() {
        return siteManager;
    }

    @PermissionDenied
    public Response permissionDenied() {
        String loginPagePath = getSiteManager().getLoginPagePath();
        if (loginPagePath != null) {
            addNote(true, "error.auth.permissionDenied");
            return Redirect.to(loginPagePath);
        } else {
            return passthrough();
        }
    }
}
