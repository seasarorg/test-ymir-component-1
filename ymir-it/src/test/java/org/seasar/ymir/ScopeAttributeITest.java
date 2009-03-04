package org.seasar.ymir;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.seasar.ymir.mock.servlet.MockHttpServletRequest;
import org.seasar.ymir.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.ymir.mock.servlet.MockHttpSession;
import org.seasar.ymir.mock.servlet.MockHttpSessionImpl;
import org.seasar.ymir.mock.servlet.MockServletContext;
import org.seasar.ymir.testing.Initializer;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.Scope2Page;
import com.example.web.ScopePage;

public class ScopeAttributeITest extends PageTestCase<ScopePage> {
    private List<String> setNameSet_ = new ArrayList<String>();

    public void test_Inアノテーションが指定されたアクションでだけ有効であること() throws Exception {
        getServletContext().setAttribute("param1", "value1");
        getServletContext().setAttribute("param2", "value2");

        process(ScopePage.class);
        ScopePage page = getPage();
        assertNull(page.getParam1());
        assertEquals("value2", page.getParam2());

        process(ScopePage.class, HttpMethod.POST);
        page = getPage();
        assertEquals("value1", page.getParam1());
        assertEquals("value2", page.getParam2());
    }

    public void test_Outアノテーションが指定されたアクションでだけ有効であること() throws Exception {
        process(new Initializer() {
            public void initialize() {
                ScopePage page = getPage();
                page.setParam1("value1");
                page.setParam2("value2");
            }
        });

        assertNull(getServletContext().getAttribute("param1"));
        assertEquals("value2", getServletContext().getAttribute("param2"));

        process(HttpMethod.POST, new Initializer() {
            public void initialize() {
                ScopePage page = getPage();
                page.setParam1("value1");
                page.setParam2("value2");
            }
        });

        assertEquals("value1", getServletContext().getAttribute("param1"));
        assertEquals("value2", getServletContext().getAttribute("param2"));
    }

    public void test_Insアノテーションが正しく解釈されること() throws Exception {
        getServletContext().setAttribute("param3", "value3");

        process();

        ScopePage page = getPage();
        assertEquals("value3", page.getParam3());
    }

    public void test_Outsアノテーションが正しく解釈されること() throws Exception {
        process(new Initializer() {
            public void initialize() {
                ScopePage page = getPage();
                page.setParam3("value3");
            }
        });

        assertEquals("value3", getServletContext().getAttribute("param3"));
    }

    public void test_セッションからInされたものはOutしなくてもHttpSession_setAttributeされること()
            throws Exception {
        process(Scope2Page.class, new Initializer() {
            public void initialize() {
                getHttpSession(true).setAttribute("string", "STRING");
                getHttpSession().setAttribute("date", new Date());
                setNameSet_.clear();
            }
        });

        assertEquals(1, setNameSet_.size());
        assertEquals("MutableオブジェクトだけがsetAttributeされること", "date", setNameSet_
                .get(0));
    }

    @Override
    protected MockHttpServletRequest newHttpServletRequest(
            final MockServletContext application, String path,
            MockHttpSession session) {
        return new MockHttpServletRequestImpl(application, path, session) {
            @Override
            public HttpSession getSession(boolean create) {
                if (create) {
                    MockHttpSession session = new MockHttpSessionImpl(
                            application, this) {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void setAttribute(String name, Object value) {
                            setNameSet_.add(name);
                            super.setAttribute(name, value);
                        }
                    };
                    setSession(session);
                    return session;
                } else {
                    return super.getSession(create);
                }
            }
        };
    }
}
