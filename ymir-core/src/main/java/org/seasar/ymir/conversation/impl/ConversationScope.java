package org.seasar.ymir.conversation.impl;

import javax.servlet.http.HttpSession;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.Globals;
import org.seasar.ymir.Request;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.scope.impl.AbstractServletScope;

public class ConversationScope extends AbstractServletScope {
    public static final String ATTRPREFIX_CONVERSATION = Globals.IDPREFIX
            + "conversation.";

    public static final String ATTR_CONVERSATIONS = ATTRPREFIX_CONVERSATION
            + "conversations";

    private Configuration configuration_;

    @Binding(bindingType = BindingType.MUST)
    public void setConfiguration(Configuration configuration) {
        configuration_ = configuration;
    }

    public Object getAttribute(String name) {
        if (isUseSessionScopeAsConversationScope()) {
            HttpSession session = getSession(false);
            if (session == null) {
                return null;
            } else {
                return session.getAttribute(name);
            }
        } else {
            Conversations conversations = getConversations();
            if (conversations != null
                    && conversationNameEquals(getPageConversationName(),
                            conversations.getCurrentConversationName())) {
                return conversations.getAttribute(name);
            } else {
                return null;
            }
        }
    }

    boolean conversationNameEquals(String conversationName1,
            String conversationName2) {
        return conversationName1 != null
                && conversationName1.equals(conversationName2);
    }

    String getPageConversationName() {
        Conversation conversation = ((Request) container_
                .getComponent(Request.class)).getCurrentDispatch()
                .getPageComponent().getPageClass().getAnnotation(
                        Conversation.class);
        if (conversation != null) {
            return conversation.name();
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
        if (isUseSessionScopeAsConversationScope()) {
            getSession().setAttribute(name, value);
        } else {
            Conversations conversations = getConversations();
            if (conversations != null
                    && conversationNameEquals(getPageConversationName(),
                            conversations.getCurrentConversationName())) {
                conversations.setAttribute(name, value);
            }
        }
    }

    boolean isUseSessionScopeAsConversationScope() {
        return PropertyUtils
                .valueOf(
                        configuration_
                                .getProperty(org.seasar.ymir.conversation.Globals.APPKEY_USESESSIONSCOPEASCONVERSATIONSCOPE),
                        false);
    }
}
