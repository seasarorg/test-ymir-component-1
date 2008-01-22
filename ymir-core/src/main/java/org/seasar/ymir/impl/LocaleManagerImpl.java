package org.seasar.ymir.impl;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.LocaleManager;
import org.seasar.ymir.Request;
import org.seasar.ymir.util.LocaleUtils;

public class LocaleManagerImpl implements LocaleManager {
    private S2Container container_;

    private ApplicationManager applicationManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setContainer(S2Container container) {
        container_ = container;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    public Locale getLocale() {
        return LocaleUtils.findLocale(getHttpServletRequest());
    }

    HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) container_.getRoot().getExternalContext()
                .getRequest();
    }

    public void setLocale(Locale locale) {
        LocaleUtils.setLocale(getHttpServletRequest(), locale);
        Request request = getRequest();
        if (request != null) {
            request.setLocale(locale);
        }
    }

    Request getRequest() {
        S2Container container = applicationManager_.findContextApplication()
                .getS2Container();
        if (container.hasComponentDef(Request.class)) {
            return (Request) container.getComponent(Request.class);
        } else {
            return null;
        }
    }

    public void removeLocale() {
        LocaleUtils.removeLocale(getHttpServletRequest());
    }
}
