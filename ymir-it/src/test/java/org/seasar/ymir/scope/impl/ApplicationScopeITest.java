package org.seasar.ymir.scope.impl;

import static org.seasar.ymir.Globals.APPKEY_CORE_REQUESTPARAMETER_STRICTINJECTION;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.test.PageTestCase;

import com.example.web.ApplicationScopeTestPage;

public class ApplicationScopeITest extends
        PageTestCase<ApplicationScopeTestPage> {
    @Override
    protected Class<ApplicationScopeTestPage> getPageClass() {
        return ApplicationScopeTestPage.class;
    }

    @Override
    protected void setUpConfiguration(Configuration configuration) {
        configuration.setProperty(APPKEY_CORE_REQUESTPARAMETER_STRICTINJECTION,
                "true");
    }

    public void test() throws Exception {
        getServletContext().setAttribute("injectedValue", "INJECTED_VALUE");
        Request request = prepareForProcessing("/applicationScopeTest.html",
                Request.METHOD_GET);
        processRequest(request);

        ApplicationScopeTestPage page = (ApplicationScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertEquals("INJECTED_VALUE", page.getInjectedValue());
        assertEquals("OUTJECTED_VALUE", getServletContext().getAttribute(
                "outjectedValue"));
    }
}
