package org.seasar.ymir;

import org.seasar.ymir.test.YmirTestCase;

import com.example.web.ForwardResponseITest2Page;
import com.example.web.ForwardResponseITest4Page;
import com.example.web.ForwardResponseITest5Page;
import com.example.web.ForwardResponseITest8Page;

public class ForwardResponseITest extends YmirTestCase {
    public void test_ForwardResponseについてはリクエストパラメータが引き継がれること() throws Exception {
        Request request = prepareForProcessing("/forwardResponseITest.html",
                Request.METHOD_GET, "param1=value1");
        process(request);

        prepareForProcessing("/forwardResponseITest2.html", Dispatcher.FORWARD);
        ForwardResponseITest2Page page = getComponent(ForwardResponseITest2Page.class);
        processRequest(request);

        assertEquals("value1", request.getParameter("param1"));
        assertEquals("value2", request.getParameter("param2"));
        assertEquals("value1", page.getParam1());
        assertEquals("value2", page.getParam2());
    }

    public void test_ProceedResponseについてはリクエストパラメータを引き継がないこと() throws Exception {
        Request request = prepareForProcessing("/forwardResponseITest.html",
                Request.METHOD_POST, "param1=value1");
        process(request);

        prepareForProcessing("/forwardResponseITest2.html", Dispatcher.FORWARD);
        ForwardResponseITest2Page page = getComponent(ForwardResponseITest2Page.class);
        processRequest(request);

        assertNull(request.getParameter("param1"));
        assertEquals("value2", request.getParameter("param2"));
        assertNull(page.getParam1());
        assertEquals("value2", page.getParam2());
    }

    public void test_ForwardResponseについてはHTTPメソッドが引き継がれること() throws Exception {
        Request request = prepareForProcessing("/forwardResponseITest3.html",
                Request.METHOD_PUT);
        process(request);

        prepareForProcessing("/forwardResponseITest4.html", Dispatcher.FORWARD);
        ForwardResponseITest4Page page = getComponent(ForwardResponseITest4Page.class);
        processRequest(request);

        assertTrue(page.isPutCalled());
    }

    public void test_ProceedResponseについてはHTTPメソッドが引き継がれないこと() throws Exception {
        Request request = prepareForProcessing("/forwardResponseITest3.html",
                Request.METHOD_POST);
        process(request);

        prepareForProcessing("/forwardResponseITest4.html", Dispatcher.FORWARD);
        ForwardResponseITest4Page page = getComponent(ForwardResponseITest4Page.class);
        processRequest(request);

        assertTrue(page.isGetCalled());
    }

    public void test_YMIR134_forwardの場合は対応するPageコンポーネントが存在しない時だけrenderメソッドが呼び出されること()
            throws Exception {
        Request request = prepareForProcessing("/forwardResponseITest5.html",
                Request.METHOD_GET);
        processRequest(request);
        ForwardResponseITest5Page actual = getComponent(ForwardResponseITest5Page.class);

        assertTrue("forward先のページ対応するコンポーネントが存在しない場合はrenderが呼び出されること", actual
                .isRenderCalled());

        request = prepareForProcessing("/forwardResponseITest5.html",
                Request.METHOD_POST);
        processRequest(request);
        actual = getComponent(ForwardResponseITest5Page.class);

        assertFalse("forward先のページ対応するコンポーネントが存在する場合はrenderが呼び出されないこと（互換性のため）",
                actual.isRenderCalled());
    }

    public void test_DeniedPathMappingにマッチするパスにforwardする時は遷移元のrenderメソッドが呼び出されること()
            throws Exception {
        Request request = prepareForProcessing("/forwardResponseITest8.html",
                Request.METHOD_GET);
        processRequest(request);
        ForwardResponseITest8Page actual = getComponent(ForwardResponseITest8Page.class);

        assertTrue(actual.isRenderCalled());
    }
}