package org.seasar.ymir;

import java.util.Locale;

public interface LocaleManager {
    Locale getLocale();

    void setLocale(Locale locale);

    void removeLocale();
}
