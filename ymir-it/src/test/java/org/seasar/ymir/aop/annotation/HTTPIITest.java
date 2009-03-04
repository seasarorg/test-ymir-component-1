package org.seasar.ymir.aop.annotation;

import org.seasar.ymir.testing.RequestInitializer;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.HTTPPage;

public class HTTPIITest extends PageTestCase<HTTPPage> {
    public void test_スキームが同じ場合はリダイレクトされないこと() throws Exception {
        process(HTTPPage.class);

        assertEquals("/context/path", getHttpServletResponse()
                .getRedirectPath());
    }

    public void test_スキームが違う場合はリダイレクトされること() throws Exception {
        process(HTTPPage.class, new RequestInitializer() {
            public void initialize() {
                getHttpServletRequest().setScheme("https");
                getHttpServletRequest().setServerPort(443);
            }
        });

        assertEquals("http://localhost/context/HTTP.html",
                getHttpServletResponse().getRedirectPath());
    }
}
