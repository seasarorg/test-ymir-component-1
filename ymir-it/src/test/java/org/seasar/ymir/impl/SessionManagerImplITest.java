package org.seasar.ymir.impl;

import javax.servlet.http.HttpSession;

import org.seasar.ymir.session.SessionManager;
import org.seasar.ymir.testing.RequestInitializer;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.SessionManagerImplPage;

public class SessionManagerImplITest extends
        PageTestCase<SessionManagerImplPage> {
    @Override
    protected Class<SessionManagerImplPage> getPageClass() {
        return SessionManagerImplPage.class;
    }

    public void testGetSession() throws Exception {
        SessionManager sessionManager = getComponent(SessionManager.class);
        sessionManager.addStraddlingAttributeNamePattern("hoehoe");

        process(SessionManagerImplPage.class, "getSession");

        assertNull("getSession(false)ではセッションが作成されないこと", getHttpSession(false));

        process(SessionManagerImplPage.class, "getSession2");

        assertNotNull("getSession(true)ではセッションが作成されること", getHttpSession(false));
    }

    public void testInvalidate() throws Exception {
        SessionManager sessionManager = getComponent(SessionManager.class);
        sessionManager.addStraddlingAttributeNamePattern("hoehoe");

        process(SessionManagerImplPage.class, new RequestInitializer() {
            public void initialize() {
                getHttpServletRequest().getSession(true);
            }
        }, "invalidate");

        HttpSession session = getHttpServletRequest().getSession(false);
        assertNull("セッションが無効化されること", session);
    }

    public void testInvalidateAndCreateSession() throws Exception {
        SessionManager sessionManager = getComponent(SessionManager.class);
        sessionManager.addStraddlingAttributeNamePattern("hoehoe");

        final HttpSession[] oldSession = new HttpSession[1];
        process(SessionManagerImplPage.class, new RequestInitializer() {
            public void initialize() {
                oldSession[0] = getHttpServletRequest().getSession();
                oldSession[0].setAttribute("hoehoe", "HOEHOE");
            }
        }, "invalidateAndCreate");

        HttpSession session = getHttpServletRequest().getSession();
        assertFalse("新たにセッションが作成されること", session.getId().equals(
                oldSession[0].getId()));
        assertEquals("セッションを跨ぐべき値が引き継がれていること", "HOEHOE", session
                .getAttribute("hoehoe"));
    }
}
