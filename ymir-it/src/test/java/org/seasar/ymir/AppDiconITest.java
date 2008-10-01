package org.seasar.ymir;

import org.seasar.ymir.testing.PageTestCase;

import com.example.web.HoePage;

public class AppDiconITest extends PageTestCase<HoePage> {
    @Override
    protected Class<HoePage> getPageClass() {
        return HoePage.class;
    }

    public void test_appDiconで定義したコンポーネントにYmirのRequestがDIされること()
            throws Exception {
        Request request = prepareForProcessing("/hoe.html", HttpMethod.GET);
        processRequest(request);
        HoePage page = getPage();
        AppDiconComponent component = page.getAppDiconComponent();
        assertNotNull(component.getYmirRequest());
        assertNotNull("app.diconからインクルードしたYmirコンポーネントもDIされること", component
                .getMessages());
    }
}
