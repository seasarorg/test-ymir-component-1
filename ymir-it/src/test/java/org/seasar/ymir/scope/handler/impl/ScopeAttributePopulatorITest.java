package org.seasar.ymir.scope.handler.impl;

import org.seasar.ymir.testing.Initializer;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.ScopeAttributePopulatorTest2Page;
import com.example.web.ScopeAttributePopulatorTestPage;

public class ScopeAttributePopulatorITest extends YmirTestCase {
    public void testPopulateTo_ネストしたBeanに属性値をインジェクションできること() throws Exception {
        process(ScopeAttributePopulatorTestPage.class, new Initializer() {
            public void initialize() {
                getComponent(ScopeAttributePopulatorTestPage.class)
                        .setTestCase(ScopeAttributePopulatorITest.this);
            }
        }, "aaa.bbb", "AAA");

        ScopeAttributePopulatorTestPage actual = getComponent(ScopeAttributePopulatorTestPage.class);

        assertEquals("AAA", actual.getAaa().getBbb());
    }

    public void test_属性名を指定したメソッドに属性値をインジェクションできること() throws Exception {
        process(ScopeAttributePopulatorTest2Page.class, "a", "AAA", "bbb",
                "BBB");

        ScopeAttributePopulatorTest2Page actual = getComponent(ScopeAttributePopulatorTest2Page.class);

        assertNull("aはsetAaaにバインドしているのでここには値が入っていないこと", actual.getA());
        assertEquals("aはsetAaaにバインドしているのでここに値が入っていること", "AAA", actual.getAaa());
        assertNull("setBbbにはbをバインドしているのでbbbの値は入っていないこと", actual.getBbb());
    }
}
