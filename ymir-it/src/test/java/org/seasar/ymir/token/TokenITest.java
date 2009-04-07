package org.seasar.ymir.token;

import org.seasar.ymir.testing.RequestInitializer;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.TokenITestPage;

public class TokenITest extends YmirTestCase {
    public void test_チェックしたらトークンがリセットされること() throws Exception {
        final TokenManager tokenManager = getComponent(TokenManager.class);
        process(TokenITestPage.class, new RequestInitializer() {
            public void initialize() {
                tokenManager.saveToken(tokenManager.getTokenKey());
            }
        });

        assertNull(tokenManager.getToken(tokenManager.getTokenKey()));
    }

    public void test_reset要素がfalseである場合はチェックしてもトークンがリセットされないこと()
            throws Exception {
        final TokenManager tokenManager = getComponent(TokenManager.class);
        final Object[] token = new Object[1];
        process(TokenITestPage.class, new RequestInitializer() {
            public void initialize() {
                tokenManager.saveToken(tokenManager.getTokenKey());
                token[0] = tokenManager.getToken(tokenManager.getTokenKey());
            }
        }, "notReset");

        assertNotNull(tokenManager.getToken(tokenManager.getTokenKey()));
        assertEquals(token[0], tokenManager
                .getToken(tokenManager.getTokenKey()));
    }

    public void test_throwException要素がtrueeである場合は例外がスローされること() throws Exception {
        final TokenManager tokenManager = getComponent(TokenManager.class);
        try {
            process(TokenITestPage.class, new RequestInitializer() {
                public void initialize() {
                    tokenManager.saveToken(tokenManager.getTokenKey());
                }
            }, "throwException");
            fail();
        } catch (InvalidTokenRuntimeException expected) {
        }
    }
}
