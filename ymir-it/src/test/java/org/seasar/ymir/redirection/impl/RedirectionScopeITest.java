package org.seasar.ymir.redirection.impl;

import javax.servlet.http.Cookie;

import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.testing.RequestInitializer;
import org.seasar.ymir.testing.PageTestCase;
import org.seasar.ymir.window.WindowManager;

import com.example.web.RedirectionScopeTestPage;

public class RedirectionScopeITest extends
        PageTestCase<RedirectionScopeTestPage> {
    public void test_リクエストパラメータ版() throws Exception {
        RedirectionManagerImpl redirectionManager = getComponent(RedirectionManagerImpl.class);
        RequestParameterScopeIdManager requestParameterScopeIdManager = new RequestParameterScopeIdManager();
        requestParameterScopeIdManager
                .setApplicationManager(getComponent(ApplicationManager.class));
        redirectionManager.setScopeIdManager(requestParameterScopeIdManager);

        process(RedirectionScopeTestPage.class);

        RedirectionScopeTestPage page = getPage();
        assertNull(page.getInjectedValue());
        String redirectPath = getHttpServletResponse().getRedirectPath();
        String pathPrefix = getContextPath() + "/redirectionScopeTest.html?"
                + AbstractScopeIdManager.KEY_SCOPEID + "=";
        assertTrue(redirectPath.startsWith(pathPrefix));

        process(RedirectionScopeTestPage.class,
                AbstractScopeIdManager.KEY_SCOPEID, redirectPath
                        .substring(pathPrefix.length()), "redirect");

        page = getPage();
        assertEquals("INJECTED_VALUE", page.getInjectedValue());
    }

    public void test_Cookie版() throws Exception {
        RedirectionManagerImpl redirectionManager = getComponent(RedirectionManagerImpl.class);
        CookieScopeIdManager cookieScopeIdManager = new CookieScopeIdManager();
        cookieScopeIdManager
                .setApplicationManager(getComponent(ApplicationManager.class));
        cookieScopeIdManager
                .setWindowManager(getComponent(WindowManager.class));
        redirectionManager.setScopeIdManager(cookieScopeIdManager);

        process(RedirectionScopeTestPage.class);

        RedirectionScopeTestPage page = getPage();
        assertNull(page.getInjectedValue());
        String redirectPath = getHttpServletResponse().getRedirectPath();
        assertEquals(getContextPath() + "/redirectionScopeTest.html",
                redirectPath);
        Cookie[] cookies = getHttpServletResponse().getCookies();
        assertEquals(1, cookies.length);
        assertEquals(AbstractScopeIdManager.KEY_SCOPEID, cookies[0].getName());

        final Cookie cookie = cookies[0];
        process(RedirectionScopeTestPage.class, new RequestInitializer() {
            public void initialize() {
                getHttpServletRequest().addCookie(cookie);
            }
        }, "redirect");

        page = getPage();
        assertEquals("INJECTED_VALUE", page.getInjectedValue());
        cookies = getHttpServletResponse().getCookies();
        assertEquals(1, cookies.length);
        assertEquals(AbstractScopeIdManager.KEY_SCOPEID, cookies[0].getName());
        assertEquals("", cookies[0].getValue());
        assertEquals(0, cookies[0].getMaxAge());
    }

    public void test_パス版() throws Exception {
        process(RedirectionScopeTestPage.class);

        RedirectionScopeTestPage page = getPage();
        assertNull(page.getInjectedValue());
        String redirectPath = getHttpServletResponse().getRedirectPath();
        String pathPrefix = getContextPath() + "/redirectionScopeTest.html";
        assertTrue(redirectPath.startsWith(pathPrefix));

        process(RedirectionScopeTestPage.class);

        page = getPage();
        assertEquals("INJECTED_VALUE", page.getInjectedValue());
    }
}
