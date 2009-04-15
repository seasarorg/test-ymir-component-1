package org.seasar.ymir.locale.impl;

import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.locale.LocaleManager;
import org.seasar.ymir.session.SessionManager;
import org.seasar.ymir.util.ContainerUtils;

public class LocaleManagerImpl implements LocaleManager {
    private ApplicationManager applicationManager_;

    private SessionManager sessionManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setSessionManager(SessionManager sessionManager) {
        sessionManager_ = sessionManager;
    }

    public Locale getLocale() {
        return findLocale(getHttpServletRequest());
    }

    /**
     * 指定されたrequestを元にロケールを決定して返します。
     * <p>ロケールの決定方法は次の通りです：</p>
     * <ol>
     * <li>session中に
     * {@link LocaleManager#ATTR_LOCALE}というキーでにバインドされているLocale</li>
     * <li><code>request.getLocale()の返り値</code></li>
     * </ol>
     * <p><code>request</code>としてnullを指定した場合、
     * システムのデフォルトロケールを返します。
     * </p>
     *
     * @param request リクエストオブジェクト。nullを指定することもできます。
     * @return Localeオブジェクト。nullが返されることはありません。
     */
    protected Locale findLocale(HttpServletRequest request) {
        if (request != null) {
            Locale locale = (Locale) sessionManager_.getAttribute(ATTR_LOCALE);
            if (locale == null) {
                locale = request.getLocale();
            }
            return locale;
        } else {
            return Locale.getDefault();
        }
    }

    HttpServletRequest getHttpServletRequest() {
        return ContainerUtils.getHttpServletRequest(getS2Container().getRoot());
    }

    public void setLocale(Locale locale) {
        sessionManager_.setAttribute(ATTR_LOCALE, locale);
    }

    S2Container getS2Container() {
        return applicationManager_.findContextApplication().getS2Container();
    }

    public void removeLocale() {
        sessionManager_.removeAttribute(ATTR_LOCALE);
    }

    /**
     * タイムゾーンを決定して返します。
     * <p>タイムゾーンの決定方法は次の通りです：</p>
     * <ol>
     * <li>session中に
     * {@link LocaleManager#ATTR_LOCALE}というキーでにバインドされているTimeZone</li>
     * <li>システムのデフォルトタイムゾーン</li>
     * </ol>
     * </p>
     *
     * @return TimeZoneオブジェクト。nullが返されることはありません。
     * @since 1.0.3
     */
    public TimeZone getTimeZone() {
        TimeZone timeZone = (TimeZone) sessionManager_
                .getAttribute(ATTR_TIMEZONE);
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        sessionManager_.setAttribute(ATTR_TIMEZONE, timeZone);
    }

    public void removeTimeZone() {
        sessionManager_.removeAttribute(ATTR_TIMEZONE);
    }
}
