package org.seasar.ymir.impl;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Globals;
import org.seasar.ymir.LocaleManager;
import org.seasar.ymir.Request;

public class LocaleManagerImpl implements LocaleManager {
    private ApplicationManager applicationManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
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
            Locale locale = null;
            HttpSession session = request.getSession(false);
            if (session != null) {
                locale = (Locale) session.getAttribute(Globals.ATTR_LOCALE);
            }
            if (locale == null) {
                locale = request.getLocale();
            }
            return locale;
        } else {
            return Locale.getDefault();
        }
    }

    HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) getS2Container().getRoot()
                .getExternalContext().getRequest();
    }

    public void setLocale(Locale locale) {
        setLocale(getHttpServletRequest(), locale);
        Request request = getRequest();
        if (request != null) {
            request.setLocale(locale);
        }
    }

    public void setLocale(HttpServletRequest request, Locale locale) {
        request.getSession().setAttribute(Globals.ATTR_LOCALE, locale);
    }

    Request getRequest() {
        S2Container container = getS2Container();
        if (container.hasComponentDef(Request.class)) {
            return (Request) container.getComponent(Request.class);
        } else {
            return null;
        }
    }

    S2Container getS2Container() {
        return applicationManager_.findContextApplication().getS2Container();
    }

    public void removeLocale() {
        removeLocale(getHttpServletRequest());
    }

    public void removeLocale(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(Globals.ATTR_LOCALE);
        }
    }
}
