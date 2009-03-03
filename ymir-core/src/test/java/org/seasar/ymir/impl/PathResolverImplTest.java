package org.seasar.ymir.impl;

import junit.framework.TestCase;

import org.seasar.ymir.mock.MockDispatch;
import org.seasar.ymir.mock.MockRequest;

public class PathResolverImplTest extends TestCase {
    private PathResolverImpl target_ = new PathResolverImpl();

    public void testResolve1() throws Exception {
        assertEquals("/", target_.resolve("", new MockRequest()));
    }

    public void testResolve2() throws Exception {
        assertEquals("/hoe.do", target_.resolve("/hoe.do", new MockRequest()));
    }

    public void testResolve3() throws Exception {
        assertEquals("/?hoe=fuga", target_.resolve("?hoe=fuga",
                new MockRequest()));
    }

    public void testResolve4() throws Exception {
        assertEquals("/;jsessionid=XXXX", target_.resolve(";jsessionid=XXXX",
                new MockRequest()));
    }

    public void testResolve5() throws Exception {
        MockRequest request = new MockRequest();
        request.enterDispatch(new MockDispatch().setPath("/index.html"));
        assertEquals("/index.html", target_.resolve(".", request));
    }

    public void testResolve6() throws Exception {
        MockRequest request = new MockRequest();
        request.enterDispatch(new MockDispatch().setPath("/index.html"));
        assertEquals("/index.html?hoe=fuga", target_.resolve(".?hoe=fuga",
                request));
    }

    public void testResolve7() throws Exception {
        MockRequest request = new MockRequest();
        request.enterDispatch(new MockDispatch().setPath("/index.html"));
        assertEquals("/index.html;jsessionid=XXXX", target_.resolve(
                ".;jsessionid=XXXX", request));
    }

    public void testResolve8() throws Exception {
        assertEquals(".dummy", target_.resolve(".dummy", new MockRequest()));
    }
}
