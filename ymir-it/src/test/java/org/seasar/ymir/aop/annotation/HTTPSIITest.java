package org.seasar.ymir.aop.annotation;

import org.seasar.ymir.testing.RequestInitializer;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.HTTPSPage;

public class HTTPSIITest extends PageTestCase<HTTPSPage> {
    public void test_スキームが同じ場合はリダイレクトされないこと() throws Exception {
        process(HTTPSPage.class, new RequestInitializer() {
            public void initialize() {
                getHttpServletRequest().setScheme("https");
                getHttpServletRequest().setServerPort(443);
            }
        });

        assertEquals("/context/path", getHttpServletResponse()
                .getRedirectPath());
    }

    public void test_スキームが違う場合はリダイレクトされること() throws Exception {
        process(HTTPSPage.class);

        assertEquals("https://localhost/context/HTTPS.html",
                getHttpServletResponse().getRedirectPath());
    }
}
