package org.seasar.ymir.impl;

import org.seasar.framework.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.framework.mock.servlet.MockHttpServletResponse;
import org.seasar.framework.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.ymir.Response;
import org.seasar.ymir.mock.MockResponse;

import junit.framework.TestCase;

public class ResponseProcessorImplTest extends TestCase {
    private ResponseProcessorImpl target_ = new ResponseProcessorImpl();

    public void testPopulateHaders() throws Exception {
        Response response = new MockResponse();
        response.addDateHeader("date", 1L);
        response.addHeader("string", "2");
        response.addIntHeader("int", 3);
        response.setDateHeader("date", 4L);
        response.setHeader("string", "5");
        response.setIntHeader("int", 6);
        MockHttpServletResponse httpResponse = new MockHttpServletResponseImpl(
                new MockHttpServletRequestImpl(new MockServletContextImpl(
                        "/context"), "/index.html")) {
            @Override
            public void addDateHeader(String name, long value) {
                assertEquals("date", name);
                assertEquals(1L, value);
            }

            @Override
            public void addHeader(String name, String value) {
                assertEquals("string", name);
                assertEquals("2", value);
            }

            @Override
            public void addIntHeader(String name, int value) {
                assertEquals("int", name);
                assertEquals(3, value);
            }

            @Override
            public void setDateHeader(String name, long value) {
                assertEquals("date", name);
                assertEquals(4L, value);
            }

            @Override
            public void setHeader(String name, String value) {
                assertEquals("string", name);
                assertEquals("5", value);
            }

            @Override
            public void setIntHeader(String name, int value) {
                assertEquals("int", name);
                assertEquals(6, value);
            }
        };

        target_.populateHeaders(response, httpResponse);
    }
}
