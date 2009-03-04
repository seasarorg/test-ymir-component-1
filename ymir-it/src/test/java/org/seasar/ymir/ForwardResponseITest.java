package org.seasar.ymir;

import org.seasar.ymir.mock.servlet.MockHttpServletRequest;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.ForwardResponseITest2Page;
import com.example.web.ForwardResponseITest3Page;
import com.example.web.ForwardResponseITest4Page;
import com.example.web.ForwardResponseITest5Page;
import com.example.web.ForwardResponseITest8Page;
import com.example.web.ForwardResponseITestPage;

public class ForwardResponseITest extends YmirTestCase {
    public void test_ForwardResponseについてはリクエストパラメータが引き継がれること() throws Exception {
        process(ForwardResponseITestPage.class, "param1", "value1");
        ForwardResponseITest2Page page = getComponent(ForwardResponseITest2Page.class);

        MockHttpServletRequest request = getHttpServletRequest();
        assertEquals("value1", request.getParameter("param1"));
        assertEquals("value2", request.getParameter("param2"));
        assertEquals("value1", page.getParam1());
        assertEquals("value2", page.getParam2());
    }

    public void test_ProceedResponseについてはリクエストパラメータを引き継がないこと() throws Exception {
        process(ForwardResponseITestPage.class, HttpMethod.POST, "param1",
                "value1");
        ForwardResponseITest2Page page = getComponent(ForwardResponseITest2Page.class);

        Request request = getRequest();
        assertNull(request.getParameter("param1"));
        assertEquals("value2", request.getParameter("param2"));
        assertNull(page.getParam1());
        assertEquals("value2", page.getParam2());
    }

    public void test_ForwardResponseについてはHTTPメソッドが引き継がれること() throws Exception {
        process(ForwardResponseITest3Page.class, HttpMethod.PUT);
        ForwardResponseITest4Page page = getComponent(ForwardResponseITest4Page.class);

        assertTrue(page.isPutCalled());
    }

    public void test_ProceedResponseについてはHTTPメソッドが引き継がれないこと() throws Exception {
        process(ForwardResponseITest3Page.class, HttpMethod.POST);
        ForwardResponseITest4Page page = getComponent(ForwardResponseITest4Page.class);

        assertTrue(page.isGetCalled());
    }

    public void test_YMIR265_forwardの場合はprerenderメソッドが呼び出されないこと()
            throws Exception {
        process(ForwardResponseITest5Page.class);
        ForwardResponseITest5Page actual = getComponent(ForwardResponseITest5Page.class);

        assertFalse("forward先のページ対応するコンポーネントが存在しない場合はprerenderが呼び出されないこと",
                actual.isPrerenderCalled());

        process(ForwardResponseITest5Page.class, HttpMethod.POST);
        actual = getComponent(ForwardResponseITest5Page.class);

        assertFalse("forward先のページ対応するコンポーネントが存在する場合はprerenderが呼び出されないこと",
                actual.isPrerenderCalled());
    }

    public void test_YMIR265_DeniedPathMappingにマッチするパスにforwardする時でも遷移元のprerenderメソッドが呼び出されないこと()
            throws Exception {
        process(ForwardResponseITest8Page.class);
        ForwardResponseITest8Page actual = getComponent(ForwardResponseITest8Page.class);

        assertFalse(actual.isPrerenderCalled());
    }
}
