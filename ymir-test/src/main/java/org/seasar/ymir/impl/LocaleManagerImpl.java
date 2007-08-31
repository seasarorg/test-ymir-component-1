package org.seasar.ymir.impl;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.LocaleManager;
import org.seasar.ymir.util.LocaleUtils;

public class LocaleManagerImpl implements LocaleManager {
    private S2Container container_;

    public void setContainer(S2Container container) {
        container_ = container;
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
    }

    public void removeLocale() {
        LocaleUtils.removeLocale(getHttpServletRequest());
    }
}
