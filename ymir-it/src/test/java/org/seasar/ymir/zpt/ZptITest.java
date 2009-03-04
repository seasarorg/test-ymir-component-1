package org.seasar.ymir.zpt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.ResponseFilter;
import org.seasar.ymir.ZptYmirTestCase;
import org.seasar.ymir.mock.servlet.MockFilterChainImpl;

import com.example.web.ZptITest1Page;

public class ZptITest extends ZptYmirTestCase {
    public void test1_page式で自分自身を表す記号が正しく解釈されること() throws Exception {
        process(ZptITest1Page.class, new MockFilterChainImpl() {
            @Override
            public void doFilter(ServletRequest request,
                    ServletResponse response) throws IOException,
                    ServletException {
                ResponseFilter filter = renderResponse();
                assertEquals(HttpServletResponse.SC_OK, filter.getStatus());
                assertEquals(getTextResource("test1_expected.html"), filter
                        .getResponseBodyAsString());
            }
        });
    }
}
