package org.seasar.ymir.conversation.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.Globals;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.scope.Scope;

public class ConversationScope implements Scope {
    public static final String ATTRPREFIX_CONVERSATION = Globals.IDPREFIX
            + "conversation.";

    public static final String ATTR_CONVERSATIONS = ATTRPREFIX_CONVERSATION
            + "conversations";

    S2Container getS2Container() {
        return YmirContext.getYmir().getApplication().getS2Container();
    }

    public Object getAttribute(String name) {
        Conversations conversations = getConversations();
        if (conversations != null) {
            return conversations.getAttribute(name);
        } else {
            return null;
        }
    }

    Conversations getConversations() {
        HttpSession session = getSession(false);
        if (session == null) {
            return null;
        } else {
            synchronized (session.getId().intern()) {
                return (Conversations) session.getAttribute(ATTR_CONVERSATIONS);
            }
        }
    }

    public void setAttribute(String name, Object value) {
        Conversations conversations = getConversations();
        if (conversations != null) {
            conversations.setAttribute(name, value);
        }
    }

    HttpSession getSession() {
        return getSession(true);
    }

    HttpSession getSession(boolean create) {
        return ((HttpServletRequest) getS2Container().getExternalContext()
                .getRequest()).getSession(create);
    }
}
