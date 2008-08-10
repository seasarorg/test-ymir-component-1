package org.seasar.ymir.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.seasar.ymir.Request;
import org.seasar.ymir.impl.YmirTypeConversionManager;
import org.seasar.ymir.mock.MockDispatch;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.test.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.ymir.test.mock.servlet.MockServletContextImpl;

import net.sf.json.JSONObject;

public class JSONInterceptorTest extends TestCase {
    public void testRequestCreated() throws Exception {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("hoe", "HOE");
        jsonObject.put("fuga", "FUGA");

        JSONInterceptor target = new JSONInterceptor() {
            @Override
            HttpServletRequest getHttpServletRequest() {
                return new MockHttpServletRequestImpl(
                        new MockServletContextImpl("/json"), "/index.html") {
                    @Override
                    public BufferedReader getReader() throws IOException {
                        return new BufferedReader(new StringReader(jsonObject
                                .toString()));
                    }

                    @Override
                    public String getContentType() {
                        return "text/javascript; charset=UTF-8";
                    }
                };
            }
        };
        target.setTypeConversionManager(new YmirTypeConversionManager());

        MockRequest request = new MockRequest();
        MockDispatch dispatch = new MockDispatch();
        request.enterDispatch(dispatch);

        Request actual = target.requestCreated(request);

        assertEquals("HOE", actual.getParameter("hoe"));
        assertEquals("FUGA", actual.getParameter("fuga"));
    }

    public void testIsJSONRequest_requestのcontentTypeがnullの場合はfalseを返すこと()
            throws Exception {
        JSONInterceptor target = new JSONInterceptor();

        assertFalse(target.isJSONRequest(new MockHttpServletRequestImpl(
                new MockServletContextImpl("/context"), "/index.html")));
    }
}
