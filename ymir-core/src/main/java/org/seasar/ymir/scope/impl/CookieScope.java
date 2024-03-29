package org.seasar.ymir.scope.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
 * <p>このスコープに設定しようとした値が配列型の場合は、
 * 配列のそれぞれの要素について上記のルールに基づいてCookieが設定されます。
 * </p>
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class CookieScope extends AbstractServletScope {
    public Object getAttribute(String name, Class<?> type) {
        if (name == null) {
            return null;
        }

        Cookie[] cookies = getRequest().getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (name.equals(cookies[i].getName())) {
                    if (type.isAssignableFrom(Cookie.class)) {
                        return cookies[i];
                    } else {
                        return cookies[i].getValue();
                    }
                }
            }
        }
        return null;
    }

    public void setAttribute(String name, Object value) {
        if (name == null) {
            return;
        }

        if (value != null && value.getClass().isArray()) {
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                addCookie(name, Array.get(value, i));
            }
        } else {
            addCookie(name, value);
        }
    }

    protected void addCookie(String name, Object value) {
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

    public Iterator<String> getAttributeNames() {
        Cookie[] cookies = getRequest().getCookies();
        List<String> list = new ArrayList<String>();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                list.add(cookies[i].getName());
            }
        }
        return list.iterator();
    }
}
