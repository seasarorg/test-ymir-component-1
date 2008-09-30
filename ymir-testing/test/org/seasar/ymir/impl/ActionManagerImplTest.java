package org.seasar.ymir.impl;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.annotation.Resolve;
import org.seasar.ymir.scope.impl.MapScope;

public class ActionManagerImplTest extends S2TestCase {
    private ActionManagerImpl target_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(getClass().getName().replace('.', '/').concat(".dicon"));
        ApplicationManager applicationManager = (ApplicationManager) getComponent(ApplicationManager.class);
        applicationManager.setBaseApplication(new SingleApplication(null, null,
                null, getContainer(), null, null));
        YmirContext.setYmir(new YmirImpl() {
            @Override
            public boolean isUnderDevelopment() {
                return false;
            }
        });
        target_ = (ActionManagerImpl) getComponent(ActionManagerImpl.class);

        MapScope mapScope = (MapScope) getComponent(MapScope.class);
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
