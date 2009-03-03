package org.seasar.ymir.redirection.impl;

import javax.servlet.http.Cookie;

import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.testing.PageTestCase;
import org.seasar.ymir.window.WindowManager;

import com.example.web.RedirectionScopeTestPage;

public class RedirectionScopeITest extends
        PageTestCase<RedirectionScopeTestPage> {
    @Override
    protected Class<RedirectionScopeTestPage> getPageClass() {
        return RedirectionScopeTestPage.class;
    }

    public void test_リクエストパラメータ版() throws Exception {
        RedirectionManagerImpl redirectionManager = getComponent(RedirectionManagerImpl.class);
        RequestParameterScopeIdManager requestParameterScopeIdManager = new RequestParameterScopeIdManager();
        requestParameterScopeIdManager
                .setApplicationManager(getComponent(ApplicationManager.class));
        redirectionManager.setScopeIdManager(requestParameterScopeIdManager);

        Request request = prepareForProcessing("/redirectionScopeTest.html",
                HttpMethod.GET);
        process(request);

        RedirectionScopeTestPage page = (RedirectionScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertNull(page.getInjectedValue());
        String redirectPath = getHttpServletResponse().getRedirectPath();
        String pathPrefix = getContextPath() + "/redirectionScopeTest.html?"
                + AbstractScopeIdManager.KEY_SCOPEID + "=";
        assertTrue(redirectPath.startsWith(pathPrefix));

        request = prepareForProcessing("/redirectionScopeTest.html",
                HttpMethod.GET, "redirect=&"
                        + AbstractScopeIdManager.KEY_SCOPEID + "="
                        + redirectPath.substring(pathPrefix.length()));
        process(request);

        page = (RedirectionScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
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

        Request request = prepareForProcessing("/redirectionScopeTest.html",
                HttpMethod.GET);
        process(request);

        RedirectionScopeTestPage page = (RedirectionScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertNull(page.getInjectedValue());
        String redirectPath = getHttpServletResponse().getRedirectPath();
        assertEquals(getContextPath() + "/redirectionScopeTest.html",
                redirectPath);
        Cookie[] cookies = getHttpServletResponse().getCookies();
        assertEquals(1, cookies.length);
        assertEquals(AbstractScopeIdManager.KEY_SCOPEID, cookies[0].getName());

        request = prepareForProcessing("/redirectionScopeTest.html",
                HttpMethod.GET, "redirect=");
        getHttpServletRequest().addCookie(cookies[0]);
        process(request);

        page = (RedirectionScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertEquals("INJECTED_VALUE", page.getInjectedValue());
        cookies = getHttpServletResponse().getCookies();
        assertEquals(1, cookies.length);
        assertEquals(AbstractScopeIdManager.KEY_SCOPEID, cookies[0].getName());
        assertEquals("", cookies[0].getValue());
        assertEquals(0, cookies[0].getMaxAge());
    }

    public void test_パス版() throws Exception {
        Request request = prepareForProcessing("/redirectionScopeTest.html",
                HttpMethod.GET);
        process(request);

        RedirectionScopeTestPage page = (RedirectionScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertNull(page.getInjectedValue());
        String redirectPath = getHttpServletResponse().getRedirectPath();
        String pathPrefix = getContextPath() + "/redirectionScopeTest.html";
        assertTrue(redirectPath.startsWith(pathPrefix));

        request = prepareForProcessing("/redirectionScopeTest.html",
                HttpMethod.GET);
        process(request);

        page = (RedirectionScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertEquals("INJECTED_VALUE", page.getInjectedValue());
    }

}
