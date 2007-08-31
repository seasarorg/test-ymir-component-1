package org.seasar.ymir.redirection.impl;

import javax.servlet.http.Cookie;

import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.test.PageTestCase;

import com.example.web.RedirectionScopeTestPage;

public class RedirectionScopeITest extends
        PageTestCase<RedirectionScopeTestPage> {
    @Override
    protected Class<RedirectionScopeTestPage> getPageClass() {
        return RedirectionScopeTestPage.class;
    }

    public void test_リクエストパラメータ版() throws Exception {
        RedirectionManagerImpl redirectionManager = (RedirectionManagerImpl) getContainer()
                .getComponent(RedirectionManagerImpl.class);
        redirectionManager.setAddScopeIdAsRequestParameter(true);

        Request request = prepareForPrecessing("/redirectionScopeTest.html",
                Request.METHOD_GET);
        process(request);

        RedirectionScopeTestPage page = (RedirectionScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertNull(page.getInjectedValue());
        String redirectPath = getHttpServletResponse().getRedirectPath();
        String pathPrefix = getContextPath() + "/redirectionScopeTest.html?"
                + RedirectionManagerImpl.KEY_SCOPEID + "=";
        assertTrue(redirectPath.startsWith(pathPrefix));

        request = prepareForPrecessing("/redirectionScopeTest.html",
                Request.METHOD_GET, "redirect=&"
                        + RedirectionManagerImpl.KEY_SCOPEID + "="
                        + redirectPath.substring(pathPrefix.length()));
        process(request);

        page = (RedirectionScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertEquals("INJECTED_VALUE", page.getInjectedValue());
    }

    public void test_Cookie版() throws Exception {
        Request request = prepareForPrecessing("/redirectionScopeTest.html",
                Request.METHOD_GET);
        process(request);

        RedirectionScopeTestPage page = (RedirectionScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertNull(page.getInjectedValue());
        String redirectPath = getHttpServletResponse().getRedirectPath();
        assertEquals(getContextPath() + "/redirectionScopeTest.html",
                redirectPath);
        Cookie[] cookies = getHttpServletResponse().getCookies();
        assertEquals(1, cookies.length);
        assertEquals(RedirectionManagerImpl.KEY_SCOPEID, cookies[0].getName());

        request = prepareForPrecessing("/redirectionScopeTest.html",
                Request.METHOD_GET, "redirect=");
        getHttpServletRequest().addCookie(cookies[0]);
        process(request);

        page = (RedirectionScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertEquals("INJECTED_VALUE", page.getInjectedValue());
        cookies = getHttpServletResponse().getCookies();
        assertEquals(1, cookies.length);
        assertEquals(RedirectionManagerImpl.KEY_SCOPEID, cookies[0].getName());
        assertEquals("", cookies[0].getValue());
        assertEquals(0, cookies[0].getMaxAge());
    }
}
