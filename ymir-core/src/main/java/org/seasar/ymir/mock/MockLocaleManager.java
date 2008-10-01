package org.seasar.ymir.mock;

import java.util.Locale;

import org.seasar.ymir.LocaleManager;

public class MockLocaleManager implements LocaleManager {
    private Locale locale_;

    public Locale getLocale() {
        return locale_;
    }

    public void removeLocale() {
        locale_ = null;
    }

    public void setLocale(final Locale locale) {
        locale_ = locale;
    }
}
