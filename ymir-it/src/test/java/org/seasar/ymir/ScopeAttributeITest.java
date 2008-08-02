package org.seasar.ymir;

import com.example.web.ScopePage;
import org.seasar.ymir.test.PageTestCase;

public class ScopeAttributeITest extends PageTestCase<ScopePage> {
    @Override
    protected Class<ScopePage> getPageClass() {
        return ScopePage.class;
    }

    public void test_Inアノテーションが指定されたアクションでだけ有効であること() throws Exception {
        getServletContext().setAttribute("param1", "value1");
        getServletContext().setAttribute("param2", "value2");

        Request request = prepareForProcessing("/scope.html",
                Request.METHOD_GET);
        processRequest(request);
        ScopePage page = getPage();
        assertNull(page.getParam1());
        assertEquals("value2", page.getParam2());

        request = prepareForProcessing("/scope.html", Request.METHOD_POST);
        processRequest(request);
        page = getPage();
        assertEquals("value1", page.getParam1());
        assertEquals("value2", page.getParam2());
    }

    public void test_Outアノテーションが指定されたアクションでだけ有効であること() throws Exception {
        Request request = prepareForProcessing("/scope.html",
                Request.METHOD_GET);
        ScopePage page = getPage();
        page.setParam1("value1");
        page.setParam2("value2");
        processRequest(request);
        assertNull(getServletContext().getAttribute("param1"));
        assertEquals("value2", getServletContext().getAttribute("param2"));

        request = prepareForProcessing("/scope.html", Request.METHOD_POST);
        page = getPage();
        page.setParam1("value1");
        page.setParam2("value2");
        processRequest(request);
        assertEquals("value1", getServletContext().getAttribute("param1"));
        assertEquals("value2", getServletContext().getAttribute("param2"));
    }

    public void test_Insアノテーションが正しく解釈されること() throws Exception {
        getServletContext().setAttribute("param3", "value3");

        Request request = prepareForProcessing("/scope.html",
                Request.METHOD_GET);
        processRequest(request);
        ScopePage page = getPage();
        assertEquals("value3", page.getParam3());
    }

    public void test_Outsアノテーションが正しく解釈されること() throws Exception {
        Request request = prepareForProcessing("/scope.html",
                Request.METHOD_GET);
        ScopePage page = getPage();
        page.setParam3("value3");
        processRequest(request);
        assertEquals("value3", getServletContext().getAttribute("param3"));
    }
}
