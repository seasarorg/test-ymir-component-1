package org.seasar.ymir.handler;

import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.ExceptionHandlerITestPage;

public class ExceptionHandlerITest extends YmirTestCase {
    public void test_Page内ハンドラについてはactionNameで指定されたハンドラが呼び出されること()
            throws Exception {
        process(ExceptionHandlerITestPage.class, "noActionName");
        assertEquals("3", getPage(ExceptionHandlerITestPage.class).getId());
    }
}
