package org.seasar.ymir.handler.impl;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.ActionNotFoundRuntimeExceptionHandlerPage;

public class ActionNotFoundRuntimeExceptionHandlerITest extends
        PageTestCase<ActionNotFoundRuntimeExceptionHandlerPage> {
    @Override
    protected Class<ActionNotFoundRuntimeExceptionHandlerPage> getPageClass() {
        return ActionNotFoundRuntimeExceptionHandlerPage.class;
    }

    public void test_アクションが見つからなかった時に405を返すこと() throws Exception {
        process(ActionNotFoundRuntimeExceptionHandlerPage.class,
                HttpMethod.TRACE);

        assertEquals(405, getHttpServletResponse().getStatus());
        assertEquals("GET,POST", getHttpServletResponse().getHeader("Allow"));
    }
}
