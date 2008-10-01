package org.seasar.ymir.impl;

import org.seasar.ymir.ComponentClientTestCase;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.annotation.Resolve;
import org.seasar.ymir.scope.impl.MapScope;

public class ActionManagerImplTest extends ComponentClientTestCase {
    private ActionManagerImpl target_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        MapScope mapScope = new MapScope();
        register(mapScope);

        target_ = getComponent(ActionManagerImpl.class);

        mapScope.setAttribute("value", "VALUE");
        mapScope.setAttribute("number", "20");
    }

    public void testNewMethodInvoker() throws Exception {
        MethodInvoker actual = target_.newMethodInvoker(Page.class, Page.class
                .getMethod("act", new Class<?>[] { String.class, Integer.TYPE,
                    Integer.TYPE, String.class, Integer.TYPE }), new Object[] {
            Integer.valueOf(10), "string" });

        Object[] params = actual.getParameters();
        assertEquals(5, params.length);
        int idx = 0;
        assertEquals("VALUE", params[idx++]);
        assertEquals(Integer.valueOf(20), params[idx++]);
        assertEquals(Integer.valueOf(10), params[idx++]);
        assertEquals("string", params[idx++]);
        assertEquals(Integer.valueOf(0), params[idx++]);
    }

    public static class Page {
        public void act(@Resolve(scopeClass = MapScope.class, value = "value")
        String value, @Resolve(scopeClass = MapScope.class, value = "number")
        int number, int number2, String string, int number3) {
        }
    }
}
