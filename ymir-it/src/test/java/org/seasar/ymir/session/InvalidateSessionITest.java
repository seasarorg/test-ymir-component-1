package org.seasar.ymir.session;

import org.seasar.ymir.testing.RequestInitializer;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.InvalidateSessionITestPage;

public class InvalidateSessionITest extends YmirTestCase {
    public void test_invalidateされること() throws Exception {
        process(InvalidateSessionITestPage.class, new RequestInitializer() {
            public void initialize() {
                getHttpSession();
            }
        });

        assertNull(getHttpSession(false));
    }

    public void test_Exceptionがスローされた場合はinvalidateされないこと() throws Exception {
        try {
            process(InvalidateSessionITestPage.class, new RequestInitializer() {
                public void initialize() {
                    getHttpSession();
                }
            }, "exception");
        } catch (RuntimeException ignore) {
        }

        assertNotNull(getHttpSession(false));
    }
}
