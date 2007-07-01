package org.seasar.ymir.conversation.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.ymir.Globals;
import org.seasar.ymir.Request;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.scope.Scope;
import org.seasar.framework.container.S2Container;

public class ConversationScope implements Scope {
    public static final String ATTRPREFIX_CONVERSATION = Globals.IDPREFIX
            + "conversation.";

    public static final String ATTR_CONVERSATIONS = ATTRPREFIX_CONVERSATION
            + "conversations";

    private S2Container container_;

    public void setS2Container(S2Container container) {
        container_ = container.getRoot();
    }

    public Object getAttribute(String name) {
        Conversations conversations = getConversations(false);
        if (conversations == null) {
            return null;
        } else {
            return conversations.getAttribute(getConversationAnnotation()
                    .name(), name);
        }
    }

    Conversation getConversationAnnotation() {
        Class<? extends Object> pageClass = getPage().getClass();
        Conversation conversation = pageClass.getAnnotation(Conversation.class);
        if (conversation == null) {
            throw new RuntimeException("Page class'" + pageClass.getName()
                    + "' must be annotated by @Conversation");
        }
        return conversation;
    }

    Conversations getConversations() {
        return getConversations(true);
    }

    Conversations getConversations(boolean create) {
        HttpSession session = getSession(create);
        if (session == null) {
            return null;
        } else {
            synchronized (session.getId().intern()) {
                Conversations conversations = (Conversations) session
                        .getAttribute(ATTR_CONVERSATIONS);
                if (conversations == null && create) {
                    conversations = new ConversationsImpl();
                    session.setAttribute(ATTR_CONVERSATIONS, conversations);
                }
                return conversations;
            }
        }
    }

    Object getPage() {
        return container_.getComponent(((Request) container_
                .getComponent(Request.class)).getComponentName());
    }

    public void setAttribute(String name, Object value) {
        getConversations().setAttribute(getConversationAnnotation().name(),
                name, value);
    }

    HttpSession getSession() {
        return getSession(true);
    }

    HttpSession getSession(boolean create) {
        return ((HttpServletRequest) container_.getRoot().getExternalContext()
                .getRequest()).getSession(create);
    }
}
