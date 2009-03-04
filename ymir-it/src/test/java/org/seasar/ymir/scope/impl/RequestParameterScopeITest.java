package org.seasar.ymir.scope.impl;

import org.seasar.ymir.testing.PageTestCase;

import com.example.web.RequestParameterScopeITestPage;

public class RequestParameterScopeITest extends
        PageTestCase<RequestParameterScopeITestPage> {
    public void test() throws Exception {
        process(RequestParameterScopeITestPage.class, "injectedValue1", "1",
                "injectedValue2", "2", "injectedValue2", "2", "injectedValue3",
                "3", "injectedValue4", "4", "injectedValue4", "4",
                "injectedValue5", "5", "injectedValue6", "6", "injectedValue6",
                "6", "i7", "7");

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
        assertEquals("7", page.getInjectedValue7());
    }
}
