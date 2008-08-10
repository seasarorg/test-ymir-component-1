package org.seasar.ymir.scope.impl;

import org.seasar.ymir.Request;
import org.seasar.ymir.test.PageTestCase;

import com.example.web.RequestParameterScopeITestPage;

public class RequestParameterScopeITest extends
        PageTestCase<RequestParameterScopeITestPage> {
    @Override
    protected Class<RequestParameterScopeITestPage> getPageClass() {
        return RequestParameterScopeITestPage.class;
    }

    public void test() throws Exception {
        Request request = prepareForProcessing(
                "/requestParameterScopeITest.html",
                Request.METHOD_GET,
                "injectedValue1=1&injectedValue2=2&injectedValue2=2"
                        + "&injectedValue3=3&injectedValue4=4&injectedValue4=4&injectedValue5=5&injectedValue6=6&injectedValue6=6");
        processRequest(request);

        RequestParameterScopeITestPage page = getPage();
        assertEquals("1", page.getInjectedValue1());
        assertEquals("2", page.getInjectedValue2());
        assertEquals(1, page.getInjectedValue3().length);
        assertEquals("3", page.getInjectedValue3()[0]);
        assertEquals(2, page.getInjectedValue4().length);
        assertEquals("4", page.getInjectedValue4()[0]);
        assertEquals("4", page.getInjectedValue4()[1]);
        assertEquals(Integer.valueOf(5), page.getInjectedValue5());
        assertEquals(2, page.getInjectedValue6().length);
        assertEquals(Integer.valueOf(6), page.getInjectedValue6()[0]);
        assertEquals(Integer.valueOf(6), page.getInjectedValue6()[1]);
    }
}