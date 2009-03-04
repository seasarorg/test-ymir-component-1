package org.seasar.ymir;

import org.seasar.ymir.testing.PageTestCase;

import com.example.web.VoidResponseITestPage;

public class VoidResponseITest extends PageTestCase<VoidResponseITestPage> {
    public void test_返り値の型がStringのアクションでnullを返してもrenderメソッドが呼び出されないこと()
            throws Exception {
        process(VoidResponseITestPage.class);

        VoidResponseITestPage actual = getPage();

        assertEquals(ResponseType.VOID, getResponse().getType());
        assertFalse(actual.isRendered());
    }
}
