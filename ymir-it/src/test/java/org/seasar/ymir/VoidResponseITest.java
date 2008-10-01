package org.seasar.ymir;

import org.seasar.ymir.testing.PageTestCase;

import com.example.web.VoidResponseITestPage;

public class VoidResponseITest extends PageTestCase<VoidResponseITestPage> {
    @Override
    protected Class<VoidResponseITestPage> getPageClass() {
        return VoidResponseITestPage.class;
    }

    public void test_返り値の型がStringのアクションでnullを返してもrenderメソッドが呼び出されないこと()
            throws Exception {
        Request request = prepareForProcessing("/voidResponseITest.html",
                HttpMethod.GET);
        Response response = processRequest(request);
        VoidResponseITestPage actual = getPage();

        assertEquals(ResponseType.VOID, response.getType());
        assertFalse(actual.isRendered());
    }
}
