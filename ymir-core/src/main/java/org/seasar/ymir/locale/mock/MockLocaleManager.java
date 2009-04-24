package org.seasar.ymir.locale.mock;

import java.util.Locale;
import java.util.TimeZone;

import org.seasar.ymir.locale.LocaleManager;

public class MockLocaleManager implements LocaleManager {
    private Locale locale_;

    private TimeZone timeZone_;

    public Locale getLocale() {
        if (locale_ != null) {
            return locale_;
        } else {
            return Locale.getDefault();
        }
    }

    public TimeZone getTimeZone() {
        if (timeZone_ != null) {
            return timeZone_;
        } else {
            return TimeZone.getDefault();
        }
    }

    public void removeLocale() {
        locale_ = null;
    }

    public void removeTimeZone() {
        timeZone_ = null;
    }

    public void setLocale(Locale locale) {
        locale_ = locale;
    }

    public void setTimeZone(TimeZone timeZone) {
        timeZone_ = timeZone;
    }
}
