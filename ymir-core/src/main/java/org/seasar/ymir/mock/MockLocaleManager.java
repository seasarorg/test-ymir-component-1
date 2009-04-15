package org.seasar.ymir.mock;

import java.util.Locale;
import java.util.TimeZone;

import org.seasar.ymir.locale.LocaleManager;

public class MockLocaleManager implements LocaleManager {
    private Locale locale_;

    private TimeZone timeZone_;

    public Locale getLocale() {
        return locale_;
    }

    public void removeLocale() {
        locale_ = null;
    }

    public void setLocale(final Locale locale) {
        locale_ = locale;
    }

    public TimeZone getTimeZone() {
        return timeZone_;
    }

    public void removeTimeZone() {
        timeZone_ = null;
    }

    public void setTimeZone(TimeZone timeZone) {
        timeZone_ = timeZone;
    }
}
