package org.seasar.ymir.scope.impl;

import org.seasar.ymir.testing.PageTestCase;

import com.example.web.URIParameterScopeITestPage;

public class URIParameterScopeITest extends
        PageTestCase<URIParameterScopeITestPage> {
    public void test() throws Exception {
        process("/URIParameterScopeITest/science&technology/index.html?category=PARAMETER&title=TITLE");

        URIParameterScopeITestPage page = getPage();
        assertEquals("PARAMETER", getRequest().getParameter("category"));
        assertEquals("science&technology", page.getCategory());

        assertEquals("TITLE", getRequest().getParameter("title"));
        assertNull(page.getTitle());
    }
}
