package org.seasar.ymir;

import java.util.List;

import org.seasar.ymir.testing.PageTestCase;

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
}
