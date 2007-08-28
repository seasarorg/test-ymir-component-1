package org.seasar.ymir.impl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.ymir.Action;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.annotation.Required;
import org.seasar.ymir.mock.MockRequest;

public class PathMappingImplTest extends TestCase {
    private PathMappingImpl target_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        target_ = new PathMappingImpl("^/([a-zA-Z][a-zA-Z0-9]*)\\.(html|do)$",
                "${1}Page", "_${method}", "", "/${1}.html",
                "^_([a-zA-Z][a-zA-Z0-9]*)$");
        target_.setTypeConversionManager(new TypeConversionManagerImpl());
    }

    public void testGetAction_親のボタン用アクション・子のボタン用のアクション・親の通常アクション・子の通常アクションの順で探索すること()
            throws Exception {
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        parameterMap.put("search", new String[] { "" });
        Request request = new MockRequest().setParameterMap(parameterMap);
        VariableResolver resolver = target_.match("/index.html",
                Request.METHOD_POST);
        Parent4Page parent4Page = new Parent4Page();
        Child4Page child4Page = new Child4Page();
        PageComponent pageComponent = new PageComponentImpl(parent4Page,
                Parent4Page.class, new PageComponent[] { new PageComponentImpl(
                        child4Page, Child4Page.class) });

        Action action = target_.getAction(pageComponent, request, resolver);

        assertNotNull(action);
        assertSame(child4Page, action.getTarget());
        assertEquals("_post_search", action.getName());
    }

    public void testParseParameters() throws Exception {
        Object[] actual = target_.parseParameters("[1][test][hoe]",
                new Class[] { Integer.TYPE, String.class, Integer.TYPE,
                    Integer.TYPE, String.class });

        assertNotNull(actual);
        assertEquals(5, actual.length);
        int idx = 0;
        assertEquals(Integer.valueOf(1), actual[idx++]);
        assertEquals("test", actual[idx++]);
        assertEquals(Integer.valueOf(0), actual[idx++]);
        assertEquals(Integer.valueOf(0), actual[idx++]);
        assertNull(actual[idx++]);
    }

    public void test_複数パラメータつきアクションのためのActionオブジェクトを生成できること() throws Exception {
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        parameterMap.put("search[1][test][hoe]", new String[] { "" });
        Request request = new MockRequest().setParameterMap(parameterMap);
        VariableResolver resolver = target_.match("/index.html",
                Request.METHOD_POST);
        PageComponent pageComponent = new PageComponentImpl(
                new PathMappingImplTest1Page(), PathMappingImplTest1Page.class,
                new PageComponent[0]);

        Action action = target_.getAction(pageComponent, request, resolver);

        assertNotNull(action);
        assertEquals("_post_search", action.getName());
        MethodInvoker invoker = action.getMethodInvoker();
        Object[] params = invoker.getParameters();
        assertEquals(5, params.length);
        int idx = 0;
        assertEquals(Integer.valueOf(1), params[idx++]);
        assertEquals("test", params[idx++]);
        assertEquals(Integer.valueOf(0), params[idx++]);
        assertEquals(Integer.valueOf(0), params[idx++]);
        assertNull(params[idx++]);
    }

    @Required
    public void testname() throws Exception {
    }
}
