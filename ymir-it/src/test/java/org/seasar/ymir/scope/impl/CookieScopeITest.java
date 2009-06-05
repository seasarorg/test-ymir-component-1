package org.seasar.ymir.scope.impl;

import java.util.Arrays;
import java.util.Comparator;

import javax.servlet.http.Cookie;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.testing.RequestInitializer;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.CookieScopeITestPage;

public class CookieScopeITest extends YmirTestCase {
    public void test_GetterでレスポンスにCookieを設定できること() throws Exception {
        process(CookieScopeITestPage.class, HttpMethod.POST, "login");

        Cookie[] actual = getHttpServletResponse().getCookies();
        Arrays.sort(actual, new Comparator<Cookie>() {
            public int compare(Cookie o1, Cookie o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        assertEquals(2, actual.length);
        int idx = 0;
        assertEquals("hoge", actual[idx].getName());
        assertEquals("fuga", actual[idx].getValue());
        idx++;
        assertEquals("value", actual[idx].getName());
        assertEquals("VALUE", actual[idx].getValue());
    }

    public void test_String型のSetterでリクエストからCookieを取得できること() throws Exception {
        process(CookieScopeITestPage.class, new RequestInitializer() {
            public void initialize() {
                getHttpServletRequest().addCookie(new Cookie("value", "Value"));
            }
        });

        String actual = getPage(CookieScopeITestPage.class).getValue();
        assertEquals("Value", actual);
    }

    public void test_YMIR_341_Cookie型のSetterでリクエストからCookieを取得できること()
            throws Exception {
        process(CookieScopeITestPage.class, new RequestInitializer() {
            public void initialize() {
                getHttpServletRequest().addCookie(new Cookie("hoge", "fuga"));
            }
        });

        Cookie actual = getPage(CookieScopeITestPage.class).getHoge();
        assertNotNull(actual);
        assertEquals("hoge", actual.getName());
        assertEquals("fuga", actual.getValue());
    }
}
