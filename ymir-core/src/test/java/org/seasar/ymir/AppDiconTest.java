package org.seasar.ymir;

import com.example.web.HoePage;

public class AppDiconTest extends PageTestCase<HoePage> {
    @Override
    protected Class<HoePage> getPageClass() {
        return HoePage.class;
    }

    public void test_appDiconで定義したコンポーネントにYmirのRequestがDIされること()
            throws Exception {
        Request request = prepareForPrecessing("/hoe.html", Request.METHOD_GET);
        processRequest(request);
        HoePage page = getPageComponent();
        AppDiconComponent component = page.getAppDiconComponent();
        assertNotNull(component.getYmirRequest());
        assertNotNull("app.diconからインクルードしたYmirコンポーネントもDIされること", component
                .getMessages());
    }
}
