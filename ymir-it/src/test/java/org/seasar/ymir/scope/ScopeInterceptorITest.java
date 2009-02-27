package org.seasar.ymir.scope;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.Request;
import org.seasar.ymir.handler.ExceptionHandler;
import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.scope.impl.ApplicationScope;
import org.seasar.ymir.scope.impl.RequestParameterScope;
import org.seasar.ymir.testing.YmirTestCase;

@SuppressWarnings("deprecation")
public class ScopeInterceptorITest extends YmirTestCase {
    public void test_Pageへのインジェクションとアウトジェクションが行なわれること() throws Exception {
        Request request = prepareForProcessing("/scopeInterceptorITest.html?value=aaa");
        process(request);

        assertEquals("aaa", getServletContext().getAttribute("value"));
    }

    public void test_ExceptionHandlerへのインジェクションとアウトジェクションが行なわれること()
            throws Exception {
        S2Container container = getContainer("ymir.dicon");
        ExceptionHandlerImpl handler = new ExceptionHandlerImpl();
        container.register(handler, "runtimeExceptionHandler");
        Request request = prepareForProcessing("/scopeInterceptorITest2.html?value=aaa");
        process(request);

        assertEquals("aaa", getServletContext().getAttribute("value"));
    }

    public static class ExceptionHandlerImpl implements
            ExceptionHandler<RuntimeException> {
        private String value_;

        @In(RequestParameterScope.class)
        public void setValue(String value) {
            value_ = value;
        }

        public String handle(RuntimeException t) {
            return null;
        }

        @Out(ApplicationScope.class)
        public String getValue() {
            return value_;
        }
    }
}
