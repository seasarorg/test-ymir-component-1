package org.seasar.ymir.aop.annotation;

import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.test.PageTestCase;

import com.example.web.HTTPSPage;

public class HTTPSIITest extends PageTestCase<HTTPSPage> {
    @Override
    protected Class<HTTPSPage> getPageClass() {
        return HTTPSPage.class;
    }

    public void test_スキームが同じ場合はリダイレクトされないこと() throws Exception {
        Request request = prepareForProcessing("/HTTPS.html",
                Request.METHOD_GET);
        getHttpServletRequest().setScheme("https");
        getHttpServletRequest().setServerPort(443);
        Response response = processRequest(request);

        assertEquals("/path", response.getPath());
    }

    public void test_スキームが違う場合はリダイレクトされること() throws Exception {
        Request request = prepareForProcessing("/HTTPS.html",
                Request.METHOD_GET);
        Response response = processRequest(request);

        assertEquals("https://localhost/context/HTTPS.html", response.getPath());
    }
}
