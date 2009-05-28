package org.seasar.ymir;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.testing.RequestInitializer;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.PageITest2Page;
import com.example.web.PageITest3Page;
import com.example.web.PageITestPage;

public class PageITest extends YmirTestCase {
    public void test_何のアノテーションもついていないSetterにコンポーネントがインジェクトされないこと()
            throws Exception {
        S2Container container = getContainer("ymir.dicon");
        container.register(new PageITestComponent1Impl());
        container.register(new PageITestComponent2Impl());
        container.register(new PageITestComponent3Impl());

        process(PageITestPage.class);

        PageITestPage actual = getComponent(PageITestPage.class);
        assertNull(actual.getComponent1());
        assertNotNull(actual.getComponent2());
        assertNotNull(actual.getComponent3());
    }

    public void test2_actionNameにパターンを指定できること() throws Exception {
        process(PageITest2Page.class, new RequestInitializer() {
            public void initialize() {
                getHttpServletRequest().setAttribute("message", "Hello");
            }
        });
        assertEquals("Hello", getPage(PageITest2Page.class).getMessage());

        process(PageITest2Page.class, new RequestInitializer() {
            public void initialize() {
                getHttpServletRequest().setAttribute("message", "Hello");
            }
        }, "ok");
        assertEquals("Hello", getPage(PageITest2Page.class).getMessage());

        process(PageITest2Page.class, HttpMethod.POST,
                new RequestInitializer() {
                    public void initialize() {
                        getHttpServletRequest()
                                .setAttribute("message", "Hello");
                    }
                }, null);
        assertNull(getPage(PageITest2Page.class).getMessage());
    }

    public void test3_YMIR_340_IncludeされているPageについてPageComponentCreatedのInvokeメソッドが呼び出されること()
            throws Exception {
        process(PageITest3Page.class);

        assertNotNull(getHttpServletRequest().getAttribute("initialized"));
    }
}
