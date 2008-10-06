package org.seasar.ymir.locale.impl;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Globals;
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
     * 指定されたrequestを元にロケールを決定します。
     * <p>ロケールの決定方法は次の通りです：</p>
     * <ol>
     * <li>session中に
     * {@link Globals#LOCALE_KEY}というキーでにバインドされているLocale</li>
     * <li><code>request.getLocale()の返り値</code></li>
     * </ol>
     * <p><code>request</code>としてnullを指定した場合、
     * システムのデフォルトロケールを返します。
     * </p>
     *
     * @param request リクエストオブジェクト。nullを指定することもできます。
     * @return Localeオブジェクト。
     */
    protected Locale findLocale(HttpServletRequest request) {
        if (request != null) {
            Locale locale = (Locale) sessionManager_
                    .getAttribute(Globals.ATTR_LOCALE);
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
        sessionManager_.setAttribute(Globals.ATTR_LOCALE, locale);
    }

    S2Container getS2Container() {
        return applicationManager_.findContextApplication().getS2Container();
    }

    public void removeLocale() {
        sessionManager_.removeAttribute(Globals.ATTR_LOCALE);
    }
}
