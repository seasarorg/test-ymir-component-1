package org.seasar.ymir;

import org.seasar.ymir.mock.servlet.MockHttpServletRequest;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.URIParameterITest1Page;
import com.example.web.URIParameterITest2Page;

public class URIParameterITest extends YmirTestCase {
    public void test_URIに指定したパラメータがPageにDIされること() throws Exception {
        process("/URIParameterITest1/science&technology/15.html");
        URIParameterITest1Page page = getComponent(URIParameterITest1Page.class);

        MockHttpServletRequest request = getHttpServletRequest();
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
}
