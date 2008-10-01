package org.seasar.ymir.scope.handler.impl;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.impl.RequestParameterScope;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.ScopeAttributePopulatorTest2Page;
import com.example.web.ScopeAttributePopulatorTestPage;

public class ScopeAttributePopulatorITest extends YmirTestCase {
    public void testPopulateTo_ネストしたBeanに属性値をインジェクションできること() throws Exception {
        final ScopeAttributePopulatorImpl target = new ScopeAttributePopulatorImpl(
                getComponent(RequestParameterScope.class),
                getComponent(AnnotationHandler.class),
                getComponent(ScopeManager.class),
                getComponent(TypeConversionManager.class));
        target.addEntry(ScopeAttributePopulatorTestPage.class.getMethod(
                "getAaa", new Class[0]), new String[0]);
        final Request request = prepareForProcessing(
                "/scopeAttributePopulatorTest.html", HttpMethod.GET,
                "aaa.bbb=AAA");
        processRequest(request, new Test() {
            @Override
            protected void test() throws Throwable {
                Object page = request.getAttribute(RequestProcessor.ATTR_SELF);
                target.populateTo(page, null);
            }
        });

        ScopeAttributePopulatorTestPage actual = (ScopeAttributePopulatorTestPage) request
                .getAttribute(RequestProcessor.ATTR_SELF);

        assertEquals("AAA", actual.getAaa().getBbb());
    }

    public void test_属性名を指定したメソッドに属性値をインジェクションできること() throws Exception {
        final Request request = prepareForProcessing(
                "/scopeAttributePopulatorTest2.html", HttpMethod.GET,
                "a=AAA&bbb=BBB");
        processRequest(request);
        ScopeAttributePopulatorTest2Page actual = (ScopeAttributePopulatorTest2Page) request
                .getAttribute(RequestProcessor.ATTR_SELF);

        assertNull("aはsetAaaにバインドしているのでここには値が入っていないこと", actual.getA());
        assertEquals("aはsetAaaにバインドしているのでここに値が入っていること", "AAA", actual.getAaa());
        assertNull("setBbbにはbをバインドしているのでbbbの値は入っていないこと", actual.getBbb());
    }
}
