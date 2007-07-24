package com.example.web;

import java.util.Locale;

import org.seasar.ymir.LocaleResolver;

public class LocaleResolverImplPage {
    private LocaleResolver localeResolver_;

    private Locale locale_;

    public void setLocaleResolver(LocaleResolver localeResolver) {
        localeResolver_ = localeResolver;
    }

    public void _get() {
        locale_ = localeResolver_.resolve();
    }

    public Locale getLocale() {
        return locale_;
    }
}
