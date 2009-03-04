package org.seasar.ymir;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.testing.YmirTestCase;

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
}
