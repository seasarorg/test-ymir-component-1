package org.seasar.ymir.annotation;

import org.seasar.ymir.testing.PageTestCase;

import com.example.web.RequestParameterITestPage;

public class RequestParameterITest extends
        PageTestCase<RequestParameterITestPage> {
    public void test() throws Exception {
        process(RequestParameterITestPage.class, "value", "VALUE", "dto.aaa",
                "AAA");

        RequestParameterITestPage page = getPage();

        assertEquals("VALUE", page.getValue());
        assertEquals("AAA", page.getDto().getAaa());
    }
}
