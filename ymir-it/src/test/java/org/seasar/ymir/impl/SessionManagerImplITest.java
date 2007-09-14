package org.seasar.ymir.impl;

import javax.servlet.http.HttpSession;

import org.seasar.ymir.Request;
import org.seasar.ymir.SessionManager;
import org.seasar.ymir.test.PageTestCase;

import com.example.web.SessionManagerImplPage;

public class SessionManagerImplITest extends
        PageTestCase<SessionManagerImplPage> {
    @Override
    protected Class<SessionManagerImplPage> getPageClass() {
        return SessionManagerImplPage.class;
    }

    public void testGetSession() throws Exception {
        SessionManager sessionManager = (SessionManager) getContainer()
                .getComponent(SessionManager.class);
        sessionManager.addStraddlingAttributeName("hoehoe");

        Request request = prepareForProcessing("/sessionManagerImpl.html",
                Request.METHOD_GET, "getSession=");
        assertNull(getHttpSession());
        processRequest(request);

        assertNull("getSession(false)ではセッションが作成されないこと", getHttpSession());

        request = prepareForProcessing("/sessionManagerImpl.html",
                Request.METHOD_GET, "getSession2=");
        processRequest(request);

        assertNotNull("getSession(true)ではセッションが作成されること", getHttpSession());
    }

    public void testInvalidate() throws Exception {
        SessionManager sessionManager = (SessionManager) getContainer()
                .getComponent(SessionManager.class);
        sessionManager.addStraddlingAttributeName("hoehoe");

        Request request = prepareForProcessing("/sessionManagerImpl.html",
                Request.METHOD_GET, "invalidate=");
        getHttpServletRequest().getSession(true);
        processRequest(request);

        HttpSession session = getHttpServletRequest().getSession(false);
        assertNull("セッションが無効化されること", session);
    }

    public void testInvalidateAndCreateSession() throws Exception {
        SessionManager sessionManager = (SessionManager) getContainer()
                .getComponent(SessionManager.class);
        sessionManager.addStraddlingAttributeName("hoehoe");

        Request request = prepareForProcessing("/sessionManagerImpl.html",
                Request.METHOD_GET, "invalidateAndCreate=");
        HttpSession session = getHttpServletRequest().getSession();
        session.setAttribute("hoehoe", "HOEHOE");
        processRequest(request);

        HttpSession session2 = getHttpServletRequest().getSession();
        assertFalse("新たにセッションが作成されること", session2.getId()
                .equals(session.getId()));
        assertEquals("セッションを跨ぐべき値が引き継がれていること", "HOEHOE", session2
                .getAttribute("hoehoe"));
    }
}
