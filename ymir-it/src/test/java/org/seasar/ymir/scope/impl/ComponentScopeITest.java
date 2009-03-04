package org.seasar.ymir.scope.impl;

import org.seasar.ymir.testing.PageTestCase;

import com.example.web.ComponentScopeTestPage;

public class ComponentScopeITest extends PageTestCase<ComponentScopeTestPage> {
    @Override
    protected Class<ComponentScopeTestPage> getPageClass() {
        return ComponentScopeTestPage.class;
    }

    public void test() throws Exception {
        process(ComponentScopeTestPage.class);

        ComponentScopeTestPage page = getPage();
        assertSame(getRequest(), page.getRequest());
    }
}
