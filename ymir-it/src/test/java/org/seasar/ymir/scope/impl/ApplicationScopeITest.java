package org.seasar.ymir.scope.impl;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.ApplicationScopeTestPage;

public class ApplicationScopeITest extends
        PageTestCase<ApplicationScopeTestPage> {
    @Override
    protected Class<ApplicationScopeTestPage> getPageClass() {
        return ApplicationScopeTestPage.class;
    }

    public void test() throws Exception {
        getServletContext().setAttribute("injectedValue", "INJECTED_VALUE");
        Request request = prepareForProcessing("/applicationScopeTest.html",
                HttpMethod.GET);
        processRequest(request);

        ApplicationScopeTestPage page = (ApplicationScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertEquals("INJECTED_VALUE", page.getInjectedValue());
        assertEquals("OUTJECTED_VALUE", getServletContext().getAttribute(
                "outjectedValue"));
    }
}
