package org.seasar.ymir.impl;

import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.ymir.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.ymir.mock.servlet.MockServletContextImpl;
import org.seasar.ymir.session.impl.SessionManagerImpl;

public class TokenManagerImplTest extends TestCase {
    private TokenManagerImpl target_ = new TokenManagerImpl();

    @Override
    protected void setUp() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequestImpl(
                new MockServletContextImpl("/context"), "/index.html");
        target_.setSessionManager(new SessionManagerImpl() {
            @Override
            public HttpSession getSession(boolean create) {
                return request.getSession(create);
            }
        });
    }

    public void testGenerateToken() throws Exception {
        String token1 = target_.generateToken();
        Thread.sleep(1000);
        String token2 = target_.generateToken();

        assertNotNull(token1);
        assertNotNull(token2);
        assertFalse(token2.equals(token1));
    }
}
