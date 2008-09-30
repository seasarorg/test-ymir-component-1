package org.seasar.ymir.redirection.impl;

import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.seasar.ymir.session.impl.SessionManagerImpl;
import org.seasar.ymir.test.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.ymir.test.mock.servlet.MockServletContextImpl;
import org.seasar.ymir.window.impl.WindowManagerImpl;

public class RedirectionManagerImplTest extends TestCase {
    private RedirectionManagerImpl target_;

    private boolean[] sessionCreated = new boolean[] { false };

    @Override
    protected void setUp() throws Exception {
        target_ = new RedirectionManagerImpl();
        WindowManagerImpl windowManager = new WindowManagerImpl() {
            @Override
            public String getWindowIdFromRequest() {
                return null;
            }
        };
        windowManager.setSessionManager(new SessionManagerImpl() {
            @Override
            public HttpSession getSession(boolean create) {
                if (create) {
                    sessionCreated[0] = true;
                }
                return new MockHttpServletRequestImpl(
                        new MockServletContextImpl("/context"), "/request")
                        .getSession(create);
            }
        });
        target_.setWindowManager(windowManager);
    }

    public void testGetScopeMapしただけではSessionが生成されないこと() throws Exception {
        target_.getScopeMap("aaa", false);

        assertFalse(sessionCreated[0]);
    }
}