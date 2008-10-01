package org.seasar.ymir;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.PageITestPage;

public class PageITest extends PageTestCase<PageITestPage> {
    @Override
    protected Class<PageITestPage> getPageClass() {
        return PageITestPage.class;
    }

    public void test_何のアノテーションもついていないSetterにコンポーネントがインジェクトされないこと()
            throws Exception {
        S2Container container = getContainer("ymir.dicon");
        container.register(new PageITestComponent1Impl());
        container.register(new PageITestComponent2Impl());
        container.register(new PageITestComponent3Impl());

        Request request = prepareForProcessing("/pageITest.html",
                HttpMethod.GET);
        process(request);
        PageITestPage actual = getPage();

        assertNull(actual.getComponent1());
        assertNotNull(actual.getComponent2());
        assertNotNull(actual.getComponent3());
    }
}
