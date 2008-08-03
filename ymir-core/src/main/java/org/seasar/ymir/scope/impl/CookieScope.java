package org.seasar.ymir.scope.impl;

import javax.servlet.http.Cookie;

/**
 * Cookieと等価のスコープを表すクラスです。
 * <p>このスコープを使うことで、Cookieに値を設定したりCookieから値を取り出したりすることができます。
 * </p>
 * <p>Cookieで保持できる値は基本的に文字列だけですので、
 * このスコープに値を設定しようとした時には{@link String#valueOf(Object)}メソッドを使って
 * 文字列に変換された上で設定されます。
 * ただしこのスコープに設定しようとした値の型が{@link Cookie}クラスである場合は、
 * その値がそのままHttpServlerResponseに設定されます。
 * この時pathが無指定の場合はpathにアプリケーションのコンテキストパスが設定されます。
 * </p>
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class CookieScope extends AbstractServletScope {
    public Object getAttribute(String name) {
        Cookie[] cookies = getRequest().getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (name.equals(cookies[i].getName())) {
                return cookies[i].getValue();
            }
        }
        return null;
    }

    public void setAttribute(String name, Object value) {
        Cookie cookie;
        if (value instanceof Cookie) {
            cookie = (Cookie) value;
        } else {
            cookie = new Cookie(name, String.valueOf(value));
            if (value == null) {
                cookie.setMaxAge(0);
            }
        }
        if (cookie.getPath() == null) {
            String path = getRequest().getContextPath();
            if (path.length() == 0) {
                path = "/";
            }
            cookie.setPath(path);
        }

        getResponse().addCookie(cookie);
    }
}
