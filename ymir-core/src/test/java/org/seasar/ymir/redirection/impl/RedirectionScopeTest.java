package org.seasar.ymir.redirection.impl;

import org.seasar.ymir.PageTestCase;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.redirection.RedirectionManager;

import com.example.web.RedirectionScopeTestPage;

public class RedirectionScopeTest extends
        PageTestCase<RedirectionScopeTestPage> {
    @Override
    protected Class<RedirectionScopeTestPage> getPageClass() {
        return RedirectionScopeTestPage.class;
    }

    public void test() throws Exception {
        Request request = prepareForPrecessing("/redirectionScopeTest.html",
                Request.METHOD_GET);
        process(request);

        RedirectionScopeTestPage page = (RedirectionScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertNull(page.getInjectedValue());
        String redirectPath = getHttpServletResponse().getRedirectPath();
        String pathPrefix = getContextPath() + "/redirectionScopeTest.html?"
                + RedirectionManager.KEY_SCOPE + "=";
        assertTrue(redirectPath.startsWith(pathPrefix));

        request = prepareForPrecessing("/redirectionScopeTest.html",
                Request.METHOD_GET, RedirectionManager.KEY_SCOPE + "="
                        + redirectPath.substring(pathPrefix.length()));
        process(request);

        page = (RedirectionScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertEquals("INJECTED_VALUE", page.getInjectedValue());
    }
}
