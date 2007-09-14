package org.seasar.ymir;

import org.seasar.ymir.test.YmirTestCase;

import com.example.web.ForwardResponseITest2Page;

public class ForwardResponseITest extends YmirTestCase {
    public void test_isParameterTakenOverがtrueであるForwardResponseについてはリクエストパラメータを引き継がれること()
            throws Exception {
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

    public void test_isParameterTakenOverがfalseであるForwardResponseについてはリクエストパラメータを引き継がないこと()
            throws Exception {
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
}
