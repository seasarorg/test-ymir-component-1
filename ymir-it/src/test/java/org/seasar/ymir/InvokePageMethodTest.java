package org.seasar.ymir;

import java.util.List;

import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.InvokePageMethodTest2Page;
import com.example.web.InvokePageMethodTest4Page;
import com.example.web.InvokePageMethodTestPage;

public class InvokePageMethodTest extends YmirTestCase {
    public void test() throws Exception {
        Request request = prepareForProcessing("/invokePageMethodTest.html",
                HttpMethod.GET);
        processRequest(request);
        InvokePageMethodTestPage actual = getComponent(InvokePageMethodTestPage.class);

        List<Phase> list = actual.getList();
        assertEquals(3, list.size());
        int idx = 0;
        assertEquals(Phase.PAGECOMPONENT_CREATED, list.get(idx++));
        assertEquals(Phase.ACTION_INVOKING, list.get(idx++));
        assertEquals(Phase.ACTION_INVOKED, list.get(idx++));
    }

    public void test_YMIR266_invokeするメソッドに引数を指定できること() throws Exception {
        getServletContext().setAttribute("value", "value");
        Request request = prepareForProcessing("/invokePageMethodTest2.html",
                HttpMethod.GET);
        processRequest(request);
        InvokePageMethodTest2Page actual = getComponent(InvokePageMethodTest2Page.class);

        assertEquals("value", actual.getValue());
    }

    public void test_YMIR323_invokeするメソッドの返り値がpassthroughの場合は処理が継続すること()
            throws Exception {
        Request request = prepareForProcessing("/invokePageMethodTest3.html",
                HttpMethod.GET);
        Response response = processRequest(request);

        assertEquals(ResponseType.REDIRECT, response.getType());
    }

    public void test_YMIR323_invokeするメソッドの返り値がpassthroughでない場合は遷移先が変更されること()
            throws Exception {
        Request request = prepareForProcessing("/invokePageMethodTest4.html",
                HttpMethod.GET);
        Response response = processRequest(request);

        assertEquals(ResponseType.FORWARD, response.getType());
        assertFalse("アクションは呼ばれないこと", getComponent(
                InvokePageMethodTest4Page.class).isInvoked());
    }
}
