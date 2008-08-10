package org.seasar.ymir.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.LocaleManager;
import org.seasar.ymir.YmirContext;

/**
 * このクラスはYmir1.0.xで削除されます。
 * 
 * @author yokota
 */
//TODO [Ymir1.0.x] 廃止する。
public class LocaleUtils {
    private static S2Container container_;

    protected LocaleUtils() {
    }

    static S2Container getS2Container() {
        if (container_ != null) {
            return container_;
        } else {
            return YmirContext.getYmir().getApplication().getS2Container();
        }
    }

    @Deprecated
    public static Locale findLocale(HttpServletRequest request) {
        return getLocale();
    }

    public static Locale getLocale() {
        return getLocaleManager().getLocale();
    }

    @Deprecated
    public static void setLocale(HttpServletRequest request, Locale locale) {
        setLocale(locale);
    }

    public static void setLocale(Locale locale) {
        getLocaleManager().setLocale(locale);
    }

    @Deprecated
    public static void removeLocale(HttpServletRequest request) {
        removeLocale();
    }

    public static void removeLocale() {
        getLocaleManager().removeLocale();
    }

    static LocaleManager getLocaleManager() {
        return (LocaleManager) getS2Container().getComponent(
                LocaleManager.class);
    }
}
