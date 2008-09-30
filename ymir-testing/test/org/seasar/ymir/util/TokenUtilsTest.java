package org.seasar.ymir.util;

import junit.framework.TestCase;

import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.ymir.test.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.ymir.test.mock.servlet.MockServletContextImpl;

public class TokenUtilsTest extends TestCase {
    public void testGenerateToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequestImpl(
                new MockServletContextImpl("/context"), "/index.html");

        String token1 = TokenUtils.generateToken(request);
        Thread.sleep(1000);
        String token2 = TokenUtils.generateToken(request);

        assertNotNull(token1);
        assertNotNull(token2);
        assertFalse(token2.equals(token1));
    }
}
