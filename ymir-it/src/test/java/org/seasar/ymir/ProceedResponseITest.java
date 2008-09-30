package org.seasar.ymir;

import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.ProceedResponseITestPage;

public class ProceedResponseITest extends YmirTestCase {
    public void test_YMIR134_proceedの場合はrenderメソッドが呼び出されないこと() throws Exception {
        Request request = prepareForProcessing("/proceedResponseITest.html",
                Request.METHOD_GET);
        processRequest(request);
        ProceedResponseITestPage actual = getComponent(ProceedResponseITestPage.class);

        assertFalse("proceedの場合は遷移先Pageクラスが存在しない場合でも遷移元のrenderは呼ばれないこと", actual
                .isRenderCalled());

        request = prepareForProcessing("/proceedResponseITest.html",
                Request.METHOD_POST);
        processRequest(request);
        actual = getComponent(ProceedResponseITestPage.class);

        assertFalse("遷移先のPageクラスが存在する場合はもちろん遷移元のrenderは呼ばれないこと", actual
                .isRenderCalled());
    }
}
