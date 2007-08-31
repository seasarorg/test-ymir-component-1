package org.seasar.ymir.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.ymir.Attribute;
import org.seasar.ymir.SessionManager;

public class SessionManagerImpl implements SessionManager {
    private S2Container container_;

    private String[] straddlingAttributeNames_ = new String[0];

    public void setContainer(S2Container container) {
        container_ = container;
    }

    public HttpSession getSession() {
        return getSession(true);
    }

    public HttpSession getSession(boolean create) {
        return getHttpServletRequest().getSession(create);
    }

    public void invalidateSession() {
        HttpSession session = getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public HttpSession invalidateAndCreateSession() {
        HttpSession session = getSession(false);
        if (session == null) {
            return getSession();
        }

        Attribute[] attributes = abandonAttributes(session);
        session.invalidate();
        session = getSession();
        populateAttributes(session, attributes);

        return session;
    }

    void populateAttributes(HttpSession session, Attribute[] attributes) {
        for (int i = 0; i < attributes.length; i++) {
            session.setAttribute(attributes[i].getName(), attributes[i]
                    .getValue());
        }
    }

    Attribute[] abandonAttributes(HttpSession session) {
        String[] names = getStraddlingAttributeNames();
        List<Attribute> attributeList = new ArrayList<Attribute>();
        for (int i = 0; i < names.length; i++) {
            Object value = session.getAttribute(names[i]);
            if (value != null) {
                attributeList.add(new Attribute(names[i], value));
            }
        }
        return attributeList.toArray(new Attribute[0]);
    }

    public String[] getStraddlingAttributeNames() {
        return straddlingAttributeNames_;
    }

    public void addStraddlingAttributeName(String attributeName) {
        straddlingAttributeNames_ = (String[]) ArrayUtil.add(
                straddlingAttributeNames_, attributeName);
    }

    HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) container_.getRoot().getExternalContext()
                .getRequest();
    }
}
