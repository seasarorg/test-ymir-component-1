package org.seasar.ymir.aop.annotation;

import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.HTTPPage;

public class HTTPIITest extends PageTestCase<HTTPPage> {
    @Override
    protected Class<HTTPPage> getPageClass() {
        return HTTPPage.class;
    }

    public void test_スキームが同じ場合はリダイレクトされないこと() throws Exception {
        Request request = prepareForProcessing("/HTTP.html", Request.METHOD_GET);
        Response response = processRequest(request);

        assertEquals("/path", response.getPath());
    }

    public void test_スキームが違う場合はリダイレクトされること() throws Exception {
        Request request = prepareForProcessing("/HTTP.html", Request.METHOD_GET);
        getHttpServletRequest().setScheme("https");
        getHttpServletRequest().setServerPort(443);
        Response response = processRequest(request);

        assertEquals("http://localhost/context/HTTP.html", response.getPath());
    }
}
