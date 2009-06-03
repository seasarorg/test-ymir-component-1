package org.seasar.ymir.scope;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.handler.ExceptionHandler;
import org.seasar.ymir.impl.FormFileImpl;
import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.scope.impl.ApplicationScope;
import org.seasar.ymir.scope.impl.RequestParameterScope;
import org.seasar.ymir.testing.RequestInitializer;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.ScopeInterceptorITest2Page;
import com.example.web.ScopeInterceptorITest4Page;
import com.example.web.ScopeInterceptorITest5Page;
import com.example.web.ScopeInterceptorITest6Page;
import com.example.web.ScopeInterceptorITest7Page;
import com.example.web.ScopeInterceptorITest8Page;
import com.example.web.ScopeInterceptorITest9Page;
import com.example.web.ScopeInterceptorITestPage;
import com.example.web.ScopeInterceptorITest3Page;

@SuppressWarnings("deprecation")
public class ScopeInterceptorITest extends YmirTestCase {
    public void test_Pageへのインジェクションとアウトジェクションが行なわれること() throws Exception {
        process(ScopeInterceptorITestPage.class, "value", "aaa");

        assertEquals("aaa", getServletContext().getAttribute("value"));
    }

    public void test_RequestParameterアノテーションで指定された名前のリクエストパラメータがインジェクトされること()
            throws Exception {
        process(ScopeInterceptorITestPage.class, "Value2", "bbb", "setValue2");

        assertEquals("bbb", getServletContext().getAttribute("value2"));
    }

    public void test_Inアノテーションを使ってPageのアクションメソッドの引数へのインジェクションが行なわれること()
            throws Exception {
        process(ScopeInterceptorITestPage.class, "value", "aaa", "resolve");

        assertEquals("aaa", getServletContext().getAttribute("value"));
    }

    public void test_Resolveアノテーションを使ってPageのアクションメソッドの引数へのインジェクションが行なわれること()
            throws Exception {
        process(ScopeInterceptorITestPage.class, "value", "aaa", "resolve2");

        assertEquals("aaa", getServletContext().getAttribute("value"));
    }

    public void test_ExceptionHandlerへのインジェクションとアウトジェクションが行なわれること()
            throws Exception {
        S2Container container = getContainer("ymir.dicon");
        ExceptionHandlerImpl handler = new ExceptionHandlerImpl();
        container.register(handler, "runtimeExceptionHandler");

        process(ScopeInterceptorITest2Page.class, "value", "aaa");

        assertEquals("aaa", getServletContext().getAttribute("value"));
    }

    public static class ExceptionHandlerImpl implements
            ExceptionHandler<RuntimeException> {
        private String value_;

        @In(RequestParameterScope.class)
        public void setValue(String value) {
            value_ = value;
        }

        public String handle(RuntimeException t) {
            return null;
        }

        @Out(ApplicationScope.class)
        public String getValue() {
            return value_;
        }
    }

    public void test_include元とinclude先が共通の親クラスを持つ場合にインジェクションとアウトジェクションが正しく行なわれること()
            throws Exception {
        process(ScopeInterceptorITest3Page.class, new RequestInitializer() {
            public void initialize() {
                getHttpSession().setAttribute("test", "TESTING");
            }
        });

        assertEquals(
                "TESTEDになりそうだが、実際はインクルードされたPageからのアウトジェクションが後に行なわれるのでTESTINGになること",
                "TESTING", getHttpSession().getAttribute("test"));
    }

    public void test_pathInfoScopeからのインジェクションが行なわれること() throws Exception {
        process("/scopeInterceptorITest4.html/pathInfo");

        assertEquals("/pathInfo", getPage(ScopeInterceptorITest4Page.class)
                .getPathInfo());
    }

    public void test_RequestParameterアノテーションでアクションメソッドへのインジェクションが行なわれること()
            throws Exception {
        process(ScopeInterceptorITest5Page.class, "param", "value");

        assertEquals("value", getPage(ScopeInterceptorITest5Page.class)
                .getParam());
    }

    public void test_Injectアノテーションでアクションメソッドへのインジェクションが行なわれること()
            throws Exception {
        process(ScopeInterceptorITest6Page.class);

        assertNotNull(getPage(ScopeInterceptorITest6Page.class)
                .getScopeManager());
    }

    public void test_Insアノテーションでアクションメソッドへのインジェクションが行なわれること() throws Exception {
        process(ScopeInterceptorITest7Page.class, "param", "value");

        assertEquals("value", getPage(ScopeInterceptorITest7Page.class)
                .getObject());
    }

    public void test_複数アノテーション指定でアクションメソッドへのインジェクションが行なわれること() throws Exception {
        process(ScopeInterceptorITest8Page.class, "param", "value");

        assertEquals("value", getPage(ScopeInterceptorITest8Page.class)
                .getObject());
    }

    public void test_配列型のプロパティにリクエストパラメータがポピュレートされること() throws Exception {
        process(ScopeInterceptorITest9Page.class, "string", "value1", "string",
                "value2", "file", new FormFileImpl(null), "file",
                new FormFileImpl(null), "object", "value1", "object", "value2");

        ScopeInterceptorITest9Page page = getPage(ScopeInterceptorITest9Page.class);

        String[] string = page.getString();
        assertNotNull(string);
        assertEquals(2, string.length);

        FormFile[] file = page.getFile();
        assertNotNull(file);
        assertEquals(2, file.length);

        Object[] object = page.getObject();
        assertNotNull(object);
        assertEquals(2, object.length);
    }
}
