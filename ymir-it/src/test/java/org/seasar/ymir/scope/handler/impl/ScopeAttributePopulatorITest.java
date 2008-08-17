package org.seasar.ymir.scope.handler.impl;

import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.scope.impl.RequestParameterScope;
import org.seasar.ymir.test.YmirTestCase;

import com.example.web.ScopeAttributePopulatorTestPage;

public class ScopeAttributePopulatorITest extends YmirTestCase {
    public void testInjectTo_ネストしたBeanに属性値をインジェクションできること() throws Exception {
        final ScopeAttributePopulator target = new ScopeAttributePopulator(
                getComponent(RequestParameterScope.class),
                getComponent(HotdeployManager.class),
                getComponent(TypeConversionManager.class));
        target.addEntry(ScopeAttributePopulatorTestPage.class.getMethod(
                "getAaa", new Class[0]), new String[0]);
        final Request request = prepareForProcessing(
                "/scopeAttributePopulatorTest.html", Request.METHOD_GET,
                "aaa.bbb=AAA");
        processRequest(request, new Test() {
            @Override
            protected void test() throws Throwable {
                Object page = request.getAttribute(RequestProcessor.ATTR_SELF);
                target.injectTo(page, null);
            }
        });

        ScopeAttributePopulatorTestPage actual = (ScopeAttributePopulatorTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);

        assertEquals("AAA", actual.getAaa().getBbb());
    }
}
