package org.seasar.ymir;

import java.util.List;

import org.seasar.ymir.testing.YmirTestCase;
import org.seasar.ymir.util.ServletUtils;

import com.example.web.InvokePageMethodTest2Page;
import com.example.web.InvokePageMethodTest3Page;
import com.example.web.InvokePageMethodTest4Page;
import com.example.web.InvokePageMethodTestPage;

public class InvokePageMethodTest extends YmirTestCase {
    public void test() throws Exception {
        process(InvokePageMethodTestPage.class);

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

        process(InvokePageMethodTest2Page.class);

        InvokePageMethodTest2Page actual = getComponent(InvokePageMethodTest2Page.class);

        assertEquals("value", actual.getValue());
    }

    public void test_YMIR323_invokeするメソッドの返り値がpassthroughの場合は処理が継続すること()
            throws Exception {
        process(InvokePageMethodTest3Page.class);

        assertNotNull(getHttpServletResponse().getRedirectPath());
    }

    public void test_YMIR323_invokeするメソッドの返り値がpassthroughでない場合は遷移先が変更されること()
            throws Exception {
        process(InvokePageMethodTest4Page.class);

        assertEquals(Dispatcher.FORWARD, ServletUtils
                .getDispatcher(getHttpServletRequest()));
        assertFalse("アクションは呼ばれないこと", getComponent(
                InvokePageMethodTest4Page.class).isInvoked());
    }
}
