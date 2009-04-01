package org.seasar.ymir.handler;

import org.seasar.ymir.testing.RequestInitializer;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.ExceptionHandlerITest2Page;
import com.example.web.ExceptionHandlerITest3IncludedPage;
import com.example.web.ExceptionHandlerITest3Page;
import com.example.web.ExceptionHandlerITestPage;

public class ExceptionHandlerITest extends YmirTestCase {
    public void test_ローカルハンドラについてはactionNameで指定されたハンドラが呼び出されること()
            throws Exception {
        process(ExceptionHandlerITestPage.class, "noActionName");
        assertEquals("3", getPage(ExceptionHandlerITestPage.class).getId());
    }

    public void test_inject前にスローされたExceptionをローカルハンドラで処理した際にインジェクトが行なわれること()
            throws Exception {
        process(ExceptionHandlerITest2Page.class, new RequestInitializer() {
            public void initialize() {
                getHttpSession().setAttribute("message", "MESSAGE");
            }
        });
        assertEquals("MESSAGE", getPage(ExceptionHandlerITest2Page.class)
                .getMessage());
    }

    public void test_inject前にスローされたExceptionをローカルハンドラで処理した際にincludeされたPageにもインジェクトが行なわれること()
            throws Exception {
        process(ExceptionHandlerITest3Page.class, new RequestInitializer() {
            public void initialize() {
                getHttpSession().setAttribute("message", "MESSAGE");
            }
        });
        assertEquals("MESSAGE", getPage(ExceptionHandlerITest3Page.class)
                .getMessage());
        assertEquals("MESSAGE", getPage(
                ExceptionHandlerITest3IncludedPage.class).getMessage());
    }
}
