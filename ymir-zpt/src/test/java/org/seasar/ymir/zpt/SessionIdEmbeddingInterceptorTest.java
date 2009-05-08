package org.seasar.ymir.zpt;

import junit.framework.TestCase;

public class SessionIdEmbeddingInterceptorTest extends TestCase {
    private SessionIdEmbeddingInterceptor target_ = new SessionIdEmbeddingInterceptor();

    public void test_omitSessionId() throws Exception {
        assertEquals("/hoe", target_.omitSessionId("/hoe"));

        assertEquals("/hoe", target_.omitSessionId("/hoe;jsessionid=XXX"));

        assertEquals("/hoe?a=1", target_
                .omitSessionId("/hoe;jsessionid=XXX?a=1"));

        assertEquals("/hoe#ref", target_
                .omitSessionId("/hoe;jsessionid=XXX#ref"));
    }
}
