package org.seasar.ymir;

import org.seasar.ymir.response.RedirectResponse;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.URIParameterITest1Page;
import com.example.web.URIParameterITest2Page;
import com.example.web.URIParameterITest3Page;

public class URIParameterITest extends YmirTestCase {
    public void test_URIに指定したパラメータがPageにDIされること() throws Exception {
        process("/URIParameterITest1/science&technology/15.html");
        URIParameterITest1Page page = getComponent(URIParameterITest1Page.class);

        Request request = getRequest();
        assertNull(request.getParameter("category"));
        assertNull(request.getParameter("sequence"));
        assertEquals("science&technology", page.getCategory());
        assertEquals(15, page.getSequence());
    }

    public void test_forwardした後はforward先のURIに指定したパラメータがPageにDIされること()
            throws Exception {
        process("/URIParameterITest1/science&technology/15.html");
        URIParameterITest2Page page = getComponent(URIParameterITest2Page.class);

        assertNull(page.getCategory());
        assertEquals(10, page.getSequence());
    }

    public void test_リクエストパラメータがURIに指定したパラメータより優先されること() throws Exception {

        process("/URIParameterITest1/science&technology/15.html?category=PARAMETER");
        URIParameterITest1Page page = getComponent(URIParameterITest1Page.class);

        Request request = getRequest();
        assertEquals("PARAMETER", request.getParameter("category"));
        assertEquals("PARAMETER", page.getCategory());
    }

    public void test_URIに指定したパラメータがURIParameterアノテーションでPageにDIされること()
            throws Exception {
        process("/URIParameterITest3/science&technology/10.html?category=PARAMETER&sequence=1");
        URIParameterITest3Page page = getComponent(URIParameterITest3Page.class);

        Request request = getRequest();
        assertEquals("PARAMETER", request.getParameter("category"));
        assertEquals("1", request.getParameter("sequence"));
        assertEquals("science&technology", page.getCategory());
        assertEquals(10, page.getSequence());
    }

    public void test_PageクラスからURIパラメータを埋め込んだパス文字列を取得できること() throws Exception {
        process("/URIParameterITest4/science&technology/index.htm");
        assertEquals("/URIParameterITest4/science&technology/index.htm",
                ((RedirectResponse) getResponse()).getPath());
    }
}
