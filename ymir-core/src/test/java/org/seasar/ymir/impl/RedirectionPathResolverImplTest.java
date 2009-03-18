package org.seasar.ymir.impl;

import junit.framework.TestCase;

import org.seasar.ymir.mock.MockDispatch;
import org.seasar.ymir.mock.MockRequest;

public class RedirectionPathResolverImplTest extends TestCase {
    private RedirectionPathResolverImpl target_ = new RedirectionPathResolverImpl();

    public void testResolve1() throws Exception {
        assertEquals("/", target_.resolve("", null, null, new MockRequest()
                .setContextPath(""), null));
        assertEquals("/", target_.resolveToProceed("", null, null,
                new MockRequest().setContextPath(""), null));
    }

    public void testResolve2() throws Exception {
        assertEquals("/hoe.do", target_.resolve("/hoe.do", null, null,
                new MockRequest().setContextPath(""), null));
        assertEquals("/hoe.do", target_.resolveToProceed("/hoe.do", null, null,
                new MockRequest().setContextPath(""), null));
    }

    public void testResolve3() throws Exception {
        assertEquals("/?hoe=fuga", target_.resolve("?hoe=fuga", null, null,
                new MockRequest().setContextPath(""), null));
        assertEquals("/?hoe=fuga", target_.resolveToProceed("?hoe=fuga", null,
                null, new MockRequest().setContextPath(""), null));
    }

    public void testResolve4() throws Exception {
        assertEquals("/;jsessionid=XXXX", target_.resolve(";jsessionid=XXXX",
                null, null, new MockRequest().setContextPath(""), null));
        assertEquals("/;jsessionid=XXXX", target_.resolveToProceed(
                ";jsessionid=XXXX", null, null, new MockRequest()
                        .setContextPath(""), null));
    }

    public void testResolve5() throws Exception {
        assertEquals("/context/", target_.resolve("", null, null,
                new MockRequest().setContextPath("/context"), null));
        assertEquals("/", target_.resolveToProceed("", null, null,
                new MockRequest().setContextPath("/context"), null));
    }

    public void testResolve6() throws Exception {
        assertEquals("/context/hoe.do", target_.resolve("/hoe.do", null, null,
                new MockRequest().setContextPath("/context"), null));
        assertEquals("/hoe.do", target_.resolveToProceed("/hoe.do", null, null,
                new MockRequest().setContextPath("/context"), null));
    }

    public void testResolve7() throws Exception {
        assertEquals("/context/?hoe=fuga", target_.resolve("?hoe=fuga", null,
                null, new MockRequest().setContextPath("/context"), null));
        assertEquals("/?hoe=fuga", target_.resolveToProceed("?hoe=fuga", null,
                null, new MockRequest().setContextPath("/context"), null));
    }

    public void testResolve8() throws Exception {
        assertEquals("/context/;jsessionid=XXXX", target_.resolve(
                ";jsessionid=XXXX", null, null, new MockRequest()
                        .setContextPath("/context"), null));
        assertEquals("/;jsessionid=XXXX", target_.resolveToProceed(
                ";jsessionid=XXXX", null, null, new MockRequest()
                        .setContextPath("/context"), null));
    }

    public void testResolve9() throws Exception {
        assertEquals("index.html", target_.resolve("index.html", null, null,
                new MockRequest().setContextPath("/context"), null));
        assertEquals("index.html", target_.resolveToProceed("index.html", null,
                null, new MockRequest().setContextPath("/context"), null));
    }
}
