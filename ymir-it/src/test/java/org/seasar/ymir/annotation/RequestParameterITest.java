package org.seasar.ymir.annotation;

import org.seasar.ymir.Request;
import org.seasar.ymir.test.PageTestCase;

import com.example.web.RequestParameterITestPage;

public class RequestParameterITest extends
        PageTestCase<RequestParameterITestPage> {
    @Override
    protected Class<RequestParameterITestPage> getPageClass() {
        return RequestParameterITestPage.class;
    }

    public void test() throws Exception {
        Request request = prepareForProcessing("/requestParameterITest.html",
                Request.METHOD_GET, "value=VALUE&dto.aaa=AAA");
        processRequest(request);

        RequestParameterITestPage page = getPage();

        assertEquals("VALUE", page.getValue());
        assertEquals("AAA", page.getDto().getAaa());
    }
}
