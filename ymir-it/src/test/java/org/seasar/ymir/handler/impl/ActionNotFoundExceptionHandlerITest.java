package org.seasar.ymir.handler.impl;

import org.seasar.ymir.Request;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.ActionNotFoundExceptionHandlerPage;

public class ActionNotFoundExceptionHandlerITest extends
        PageTestCase<ActionNotFoundExceptionHandlerPage> {
    @Override
    protected Class<ActionNotFoundExceptionHandlerPage> getPageClass() {
        return ActionNotFoundExceptionHandlerPage.class;
    }

    public void test_アクションが見つからなかった時に405を返すこと() throws Exception {
        Request request = prepareForProcessing(
                "/actionNotFoundExceptionHandler.html", Request.METHOD_TRACE);
        process(request);

        assertEquals(405, getHttpServletResponse().getStatus());
        assertEquals("GET,POST", getHttpServletResponse().getHeader("Allow"));
    }
}
