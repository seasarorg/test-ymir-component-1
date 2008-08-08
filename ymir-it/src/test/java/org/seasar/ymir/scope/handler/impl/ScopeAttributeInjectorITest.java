package org.seasar.ymir.scope.handler.impl;

import org.seasar.ymir.Request;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.scope.impl.RequestParameterScope;
import org.seasar.ymir.test.YmirTestCase;

public class ScopeAttributeInjectorITest extends YmirTestCase {
    public void testInjectTo_ネストしたBeanに属性値をインジェクションできること() throws Exception {
        final ScopeAttributeInjector target = new ScopeAttributeInjector(
                "aaa.bbb", getComponent(RequestParameterScope.class),
                Page.class.getMethod("getAaa", new Class[0]), false,
                new String[0], getComponent(HotdeployManager.class),
                getComponent(TypeConversionManager.class));

        final Page page = new Page();

        Request request = prepareForProcessing("/conversation.html",
                Request.METHOD_GET, "aaa.bbb=AAA");
        processRequest(request, new Test() {
            @Override
            protected void test() throws Throwable {
                target.injectTo(page);
            }
        });

        assertEquals("AAA", page.getAaa().getBbb());
    }

    public static class Page {
        private Aaa aaa_;

        public Aaa getAaa() {
            return aaa_;
        }

        public void setAaa(Aaa aaa) {
            aaa_ = aaa;
        }
    }

    public static class Aaa {
        private String bbb_;

        public String getBbb() {
            return bbb_;
        }

        public void setBbb(String bbb) {
            bbb_ = bbb;
        }
    }
}
