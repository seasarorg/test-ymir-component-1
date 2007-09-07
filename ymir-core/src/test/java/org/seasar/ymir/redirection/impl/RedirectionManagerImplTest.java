package org.seasar.ymir.redirection.impl;

import javax.servlet.http.HttpSession;

import org.seasar.framework.mock.servlet.MockHttpSessionImpl;

import junit.framework.TestCase;

public class RedirectionManagerImplTest extends TestCase {
    public void testGetScopeMap_createしない場合はセッションを生成しないこと() throws Exception {
        final boolean[] sessionCreated = new boolean[] { false };
        RedirectionManagerImpl target_ = new RedirectionManagerImpl() {
            @Override
            public String getScopeId() {
                return "scopeId";
            }

            @Override
            HttpSession getSession(boolean create) {
                if (create) {
                    sessionCreated[0] = true;
                    return new MockHttpSessionImpl(null);
                } else {
                    return null;
                }
            }
        };

        target_.getScopeMap(false);

        assertFalse(sessionCreated[0]);
    }
}
