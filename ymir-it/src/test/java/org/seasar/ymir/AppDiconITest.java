package org.seasar.ymir;

import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.HoePage;

public class AppDiconITest extends YmirTestCase {
    public void test_appDiconで定義したコンポーネントにYmirのRequestがDIされること()
            throws Exception {
        process(HoePage.class);

        HoePage page = getComponent(HoePage.class);
        AppDiconComponent component = page.getAppDiconComponent();
        assertNotNull(component.getYmirRequest());
        assertNotNull("app.diconからインクルードしたYmirコンポーネントもDIされること", component
                .getMessages());
    }
}
