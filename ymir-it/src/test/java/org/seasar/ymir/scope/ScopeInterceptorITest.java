package org.seasar.ymir.scope;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.handler.ExceptionHandler;
import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.scope.impl.ApplicationScope;
import org.seasar.ymir.scope.impl.RequestParameterScope;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.ScopeInterceptorITest2Page;
import com.example.web.ScopeInterceptorITestPage;

@SuppressWarnings("deprecation")
public class ScopeInterceptorITest extends YmirTestCase {
    public void test_Pageへのインジェクションとアウトジェクションが行なわれること() throws Exception {
        process(ScopeInterceptorITestPage.class, "value", "aaa");

        assertEquals("aaa", getServletContext().getAttribute("value"));
    }

    public void test_ExceptionHandlerへのインジェクションとアウトジェクションが行なわれること()
            throws Exception {
        S2Container container = getContainer("ymir.dicon");
        ExceptionHandlerImpl handler = new ExceptionHandlerImpl();
        container.register(handler, "runtimeExceptionHandler");

        process(ScopeInterceptorITest2Page.class, "value", "aaa");

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
