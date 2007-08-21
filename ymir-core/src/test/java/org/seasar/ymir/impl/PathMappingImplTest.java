package org.seasar.ymir.impl;

import java.util.HashMap;
import java.util.Map;

import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.ymir.Action;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.mock.MockRequest;

import junit.framework.TestCase;

public class PathMappingImplTest extends TestCase {
    public void testGetAction_親のボタン用アクション・子のボタン用のアクション・親の通常アクション・子の通常アクションの順で探索すること()
            throws Exception {
        PathMappingImpl target = new PathMappingImpl(
                "^/([a-zA-Z][a-zA-Z0-9]*)\\.(html|do)$", "${1}Page",
                "_${method}", "", "/${1}.html", "^_([a-zA-Z][a-zA-Z0-9]*)$");
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        parameterMap.put("search", new String[] { "" });
        Request request = new MockRequest().setParameterMap(parameterMap);
        VariableResolver resolver = target.match("/index.html",
                Request.METHOD_POST);
        Parent4Page parent4Page = new Parent4Page();
        Child4Page child4Page = new Child4Page();
        PageComponent pageComponent = new PageComponentImpl(parent4Page,
                Parent4Page.class, new PageComponent[] { new PageComponentImpl(
                        child4Page, Child4Page.class) });

        Action action = target.getAction(pageComponent, request, resolver);

        assertNotNull(action);
        assertSame(child4Page, action.getTarget());
        assertEquals("_post_search", action.getName());
    }
}
