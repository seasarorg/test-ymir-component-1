package org.seasar.ymir.test;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.PageTestCaseTestPage;

public class PageTestCaseTest extends PageTestCase<PageTestCaseTestPage> {
    @Override
    protected Class<PageTestCaseTestPage> getPageClass() {
        return PageTestCaseTestPage.class;
    }

    public void test_prerenderが呼び出されること() throws Exception {
        Request request = prepareForProcessing("/pageTestCaseTest.html",
                HttpMethod.GET);
        processRequest(request);

        assertTrue(getPage().isPrerenderCalled());
    }
}
