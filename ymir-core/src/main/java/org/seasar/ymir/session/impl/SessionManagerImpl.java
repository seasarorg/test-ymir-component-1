package org.seasar.ymir.session.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.S2Container;
import org.seasar.kvasir.util.collection.EnumerationIterator;
import org.seasar.ymir.Attribute;
import org.seasar.ymir.session.SessionManager;
import org.seasar.ymir.util.ContainerUtils;

public class SessionManagerImpl implements SessionManager {
    private S2Container container_;

    private String straddlingAttributeNamePatternString_;

    private Pattern straddlingAttributeNamePattern_;

    private AttributeListener attributeListener_ = new VoidAttributeListener();

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
            // setした以降値を変更しないのでセッションレプリケーションsafe。
            session.setAttribute(attributes[i].getName(), attributes[i]
                    .getValue());
        }
    }

    Attribute[] abandonAttributes(HttpSession session) {
        List<Attribute> attributeList = new ArrayList<Attribute>();
        for (Enumeration<?> enm = session.getAttributeNames(); enm
                .hasMoreElements();) {
            String name = (String) enm.nextElement();
            if (isStraddlingAttributeName(name)) {
                attributeList.add(new Attribute(name, session
                        .getAttribute(name)));
            }
        }
        return attributeList.toArray(new Attribute[0]);
    }

    /**
     * セッションを無効化して再作成する場合にセッションを跨いで値を保持したい属性の名前かどうかを返します。
     * 
     * @return セッションを跨いで値を保持したい属性の名前かどうか。
     * @see #addStraddlingAttributeNamePattern(String)
     */
    boolean isStraddlingAttributeName(String name) {
        if (straddlingAttributeNamePattern_ == null) {
            return false;
        } else {
            return straddlingAttributeNamePattern_.matcher(name).matches();
        }
    }

    public void addStraddlingAttributeNamePattern(String namePattern) {
        if (straddlingAttributeNamePatternString_ == null) {
            straddlingAttributeNamePatternString_ = namePattern;
        } else {
            straddlingAttributeNamePatternString_ = straddlingAttributeNamePatternString_
                    + "|" + namePattern;
        }
        straddlingAttributeNamePattern_ = Pattern
                .compile(straddlingAttributeNamePatternString_);
    }

    HttpServletRequest getHttpServletRequest() {
        return ContainerUtils.getHttpServletRequest(container_.getRoot());
    }

    public Object getAttribute(String name) {
        HttpSession session = getSession(false);
        if (session == null) {
            return null;
        } else {
            attributeListener_.notifyGetAttribute(name);
            return session.getAttribute(name);
        }
    }

    @SuppressWarnings("unchecked")
    public Iterator<String> getAttributeNames() {
        HttpSession session = getSession(false);
        if (session == null) {
            return new ArrayList<String>().iterator();
        } else {
            return new EnumerationIterator(session.getAttributeNames());
        }
    }

    public boolean isSessionPresent() {
        return (getSession(false) != null);
    }

    public void removeAttribute(String name) {
        HttpSession session = getSession(false);
        if (session != null) {
            synchronized (session) {
                session.removeAttribute(name);
            }
        }
    }

    public void setAttribute(String name, Object value) {
        HttpSession session = getSession();
        synchronized (session) {
            attributeListener_.notifySetAttribute(name);
            session.setAttribute(name, value);
        }
    }

    public void refreshAttribute(String name) {
        HttpSession session = getSession(false);
        if (session != null) {
            synchronized (session) {
                Object value = session.getAttribute(name);
                if (value != null && !isImmutable(value)) {
                    session.setAttribute(name, value);
                }
            }
        }
    }

    protected boolean isImmutable(Object value) {
        return (value instanceof String || value instanceof Boolean
                || value instanceof Character || value instanceof Byte
                || value instanceof Short || value instanceof Integer
                || value instanceof Long || value instanceof Float || value instanceof Double);
    }

    public void setAttributeListener(AttributeListener attributeListener) {
        attributeListener_ = attributeListener;
    }
}
