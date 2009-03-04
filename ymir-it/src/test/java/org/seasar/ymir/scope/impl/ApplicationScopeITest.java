package org.seasar.ymir.scope.impl;

import org.seasar.ymir.testing.PageTestCase;

import com.example.web.ApplicationScopeTestPage;

public class ApplicationScopeITest extends
        PageTestCase<ApplicationScopeTestPage> {
    public void test() throws Exception {
        getServletContext().setAttribute("injectedValue", "INJECTED_VALUE");
        process(ApplicationScopeTestPage.class);

        ApplicationScopeTestPage page = getPage();
        assertEquals("INJECTED_VALUE", page.getInjectedValue());
        assertEquals("OUTJECTED_VALUE", getServletContext().getAttribute(
                "outjectedValue"));
    }
}
