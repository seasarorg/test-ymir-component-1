package org.seasar.ymir.checkbox;

import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.ResponseFilter;
import org.seasar.ymir.ZptYmirTestCase;

import com.example.web.CheckboxITest1Page;

public class CheckboxITest extends ZptYmirTestCase {
    public void test1_checkboxについてhiddenタグが生成されること() throws Exception {
        process(CheckboxITest1Page.class);

        ResponseFilter filter = renderResponse();
        assertEquals(HttpServletResponse.SC_OK, filter.getStatus());
        assertEquals(getTextResource("test1_expected.html"), filter
                .getResponseBodyAsString());
    }

    public void test2_checkboxがチェックされなかった場合でもリクエストパラメータが設定されること()
            throws Exception {
        process(CheckboxITest1Page.class, HttpMethod.POST, "check1", "true",
                "org.seasar.ymir.checkbox", new String[] { "check1", "check2" });

        CheckboxITest1Page actual = getPage(CheckboxITest1Page.class);
        assertTrue(actual.isCheck1());
        assertFalse(actual.isCheck2());
    }
}
