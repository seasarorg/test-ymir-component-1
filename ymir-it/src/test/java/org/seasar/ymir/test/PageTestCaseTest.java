package org.seasar.ymir.test;

import org.seasar.ymir.testing.PageTestCase;

import com.example.web.PageTestCaseTestPage;

public class PageTestCaseTest extends PageTestCase<PageTestCaseTestPage> {
    public void test_prerenderが呼び出されること() throws Exception {
        process(PageTestCaseTestPage.class);

        assertTrue(getPage().isPrerenderCalled());
    }
}
