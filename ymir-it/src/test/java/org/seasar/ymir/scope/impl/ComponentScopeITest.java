package org.seasar.ymir.scope.impl;

import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.test.PageTestCase;

import com.example.web.ComponentScopeTestPage;

public class ComponentScopeITest extends PageTestCase<ComponentScopeTestPage> {
    @Override
    protected Class<ComponentScopeTestPage> getPageClass() {
        return ComponentScopeTestPage.class;
    }

    public void test() throws Exception {
        Request request = prepareForProcessing("/componentScopeTest.html",
                Request.METHOD_GET);
        processRequest(request);

        ComponentScopeTestPage page = (ComponentScopeTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);
        assertSame(request, page.getRequest());
    }
}
