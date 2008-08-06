package org.seasar.ymir.session.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.Attribute;
import org.seasar.ymir.session.SessionManager;
import org.seasar.ymir.util.ContainerUtils;

public class SessionManagerImpl implements SessionManager {
    private S2Container container_;

    private String straddlingAttributeNamePatternString_;

    private Pattern straddlingAttributeNamePattern_;

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

    @SuppressWarnings("unchecked")
    Attribute[] abandonAttributes(HttpSession session) {
        List<Attribute> attributeList = new ArrayList<Attribute>();
        for (Enumeration enm = session.getAttributeNames(); enm
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
}
