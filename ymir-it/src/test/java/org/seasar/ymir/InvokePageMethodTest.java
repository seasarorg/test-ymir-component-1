package org.seasar.ymir;

import java.util.List;

import org.seasar.ymir.test.PageTestCase;

import com.example.web.InvokePageMethodTestPage;

public class InvokePageMethodTest extends
        PageTestCase<InvokePageMethodTestPage> {

    @Override
    protected Class<InvokePageMethodTestPage> getPageClass() {
        return InvokePageMethodTestPage.class;
    }

    public void test() throws Exception {
        Request request = prepareForProcessing("/invokePageMethodTest.html",
                Request.METHOD_GET);
        processRequest(request);
        InvokePageMethodTestPage actual = getPage();

        List<Phase> list = actual.getList();
        assertEquals(6, list.size());
        int idx = 0;
        assertEquals(Phase.PAGECOMPONENT_CREATED, list.get(idx++));
        assertEquals(Phase.SCOPEOBJECT_INJECTING, list.get(idx++));
        assertEquals(Phase.ACTION_INVOKING, list.get(idx++));
        assertEquals(Phase.ACTION_INVOKED, list.get(idx++));
        assertEquals(Phase.SCOPEOBJECT_OUTJECTING, list.get(idx++));
        assertEquals(Phase.SCOPEOBJECT_OUTJECTED, list.get(idx++));
    }
}
