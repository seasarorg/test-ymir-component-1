package com.example.web;

import java.util.Locale;

import org.seasar.ymir.LocaleManager;

public class LocaleManagerImplPage {
    private LocaleManager localeManager_;

    private Locale locale_;

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
