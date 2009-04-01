package org.seasar.ymir.testing;

import com.example.web.YmirTestCaseTestPage;

public class YmirTestCaseTest extends YmirTestCase {
    public void test_toTheEndOf() throws Exception {
        toTheEndOf(process(YmirTestCaseTestPage.class));
        assertEquals("_get_action has been called", getPage(
                YmirTestCaseTestPage.class).getMessage());
    }
}
