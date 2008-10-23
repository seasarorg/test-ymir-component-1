package org.seasar.ymir;

import java.util.List;

import org.seasar.ymir.testing.PageTestCase;

import com.example.web.InvokePageMethodTest2Page;
import com.example.web.InvokePageMethodTestPage;

public class InvokePageMethodTest extends
        PageTestCase<InvokePageMethodTestPage> {

    @Override
    protected Class<InvokePageMethodTestPage> getPageClass() {
        return InvokePageMethodTestPage.class;
    }

    public void test() throws Exception {
        Request request = prepareForProcessing("/invokePageMethodTest.html",
                HttpMethod.GET);
        processRequest(request);
        InvokePageMethodTestPage actual = getPage();

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
        InvokePageMethodTest2Page actual = (InvokePageMethodTest2Page) request
                .getAttribute("self");

        assertEquals("value", actual.getValue());
    }
}
