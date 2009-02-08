package org.seasar.ymir.zpt;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.ResponseFilter;
import org.seasar.ymir.ZptYmirTestCase;

import com.example.web.ParamSelfITest1Page;
import com.example.web.ParamSelfITest2Page;

public class ParamSelfITest extends ZptYmirTestCase {
    public void test1_リクエストパラメータとselfのプロパティが参照されること() throws Exception {
        Request request = prepareForProcessing(ParamSelfITest1Page.class,
                HttpMethod.GET, "param", "PARAM");
        ParamSelfITest1Page page = getComponent(ParamSelfITest1Page.class);
        page.setParam("SELF");
        page.setParam2("SELF2");
        process(request);

        ResponseFilter filter = renderResponse();
        assertEquals(HttpServletResponse.SC_OK, filter.getStatus());
        assertEquals(getTextResource("test1_expected.html"), filter
                .getResponseBodyAsString());
    }

    public void test2_日付型の値の表示のためのHintが取得されること() throws Exception {
        Request request = prepareForProcessing(ParamSelfITest2Page.class,
                HttpMethod.GET);
        ParamSelfITest2Page page = getComponent(ParamSelfITest2Page.class);
        page.setDate(new Date(0L));
        process(request);

        ResponseFilter filter = renderResponse();
        assertEquals(HttpServletResponse.SC_OK, filter.getStatus());
        assertEquals(getTextResource("test2_expected.html"), filter
                .getResponseBodyAsString());
    }
}
