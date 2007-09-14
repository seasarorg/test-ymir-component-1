package org.seasar.ymir;

import com.example.web.HoePage;
import org.seasar.ymir.test.PageTestCase;

public class AppDiconITest extends PageTestCase<HoePage> {
    @Override
    protected Class<HoePage> getPageClass() {
        return HoePage.class;
    }

    public void test_appDiconで定義したコンポーネントにYmirのRequestがDIされること()
            throws Exception {
        Request request = prepareForProcessing("/hoe.html", Request.METHOD_GET);
        processRequest(request);
        HoePage page = getPage();
        AppDiconComponent component = page.getAppDiconComponent();
        assertNotNull(component.getYmirRequest());
        assertNotNull("app.diconからインクルードしたYmirコンポーネントもDIされること", component
                .getMessages());
    }
}
