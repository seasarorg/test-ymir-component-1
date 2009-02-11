package org.seasar.ymir.zpt;

import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.ResponseFilter;
import org.seasar.ymir.ZptYmirTestCase;

import com.example.web.ZptITest1Page;

public class ZptITest extends ZptYmirTestCase {
    public void test1_page式で自分自身を表す記号が正しく解釈されること() throws Exception {
        Request request = prepareForProcessing(ZptITest1Page.class,
                HttpMethod.GET);
        process(request, new Test() {
            @Override
            protected void test() throws Throwable {
                ResponseFilter filter = renderResponse();
                assertEquals(HttpServletResponse.SC_OK, filter.getStatus());
                assertEquals(getTextResource("test1_expected.html"), filter
                        .getResponseBodyAsString());
            }
        });
    }
}
