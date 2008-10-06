package com.example.web;

import java.util.Locale;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.locale.LocaleManager;

public class LocaleManagerImplPage {
    private LocaleManager localeManager_;

    private Locale locale_;

    @Binding(bindingType = BindingType.MUST)
    public void setLocaleManager(LocaleManager localeManager) {
        localeManager_ = localeManager;
    }

    public void _get() {
        locale_ = localeManager_.getLocale();
    }

    public Locale getLocale() {
        return locale_;
    }
}
