package org.seasar.ymir.redirection.impl;

import javax.servlet.http.Cookie;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.testing.PageTestCase;

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

        Request request = prepareForProcessing("/redirectionScopeTest.html",
                HttpMethod.GET);
        process(request);

        RedirectionScopeTestPage page = (RedirectionScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertNull(page.getInjectedValue());
        String redirectPath = getHttpServletResponse().getRedirectPath();
        String pathPrefix = getContextPath() + "/redirectionScopeTest.html?"
                + RedirectionManagerImpl.KEY_SCOPEID + "=";
        assertTrue(redirectPath.startsWith(pathPrefix));

        request = prepareForProcessing("/redirectionScopeTest.html",
                HttpMethod.GET, "redirect=&"
                        + RedirectionManagerImpl.KEY_SCOPEID + "="
                        + redirectPath.substring(pathPrefix.length()));
        process(request);

        page = (RedirectionScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertEquals("INJECTED_VALUE", page.getInjectedValue());
    }

    public void test_Cookie版() throws Exception {
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
        assertEquals(RedirectionManagerImpl.KEY_SCOPEID, cookies[0].getName());

        request = prepareForProcessing("/redirectionScopeTest.html",
                HttpMethod.GET, "redirect=");
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
