package org.seasar.ymir.impl;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.LocaleResolver;
import org.seasar.ymir.util.LocaleUtils;

public class LocaleResolverImpl implements LocaleResolver {
    private S2Container container_;

    public void setContainer(S2Container container) {
        container_ = container;
    }

    public Locale resolve() {
        return LocaleUtils.findLocale((HttpServletRequest) container_.getRoot()
                .getExternalContext().getRequest());
    }
}
