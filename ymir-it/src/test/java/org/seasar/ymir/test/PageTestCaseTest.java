package org.seasar.ymir.test;

import org.seasar.ymir.Request;

import com.example.web.PageTestCaseTestPage;

public class PageTestCaseTest extends PageTestCase<PageTestCaseTestPage> {
    @Override
    protected Class<PageTestCaseTestPage> getPageClass() {
        return PageTestCaseTestPage.class;
    }

    public void test_renderが呼び出されること() throws Exception {
        Request request = prepareForProcessing("/pageTestCaseTest.html",
                Request.METHOD_GET);
        processRequest(request);

        assertTrue(getPage().isRenderCalled());
    }
}