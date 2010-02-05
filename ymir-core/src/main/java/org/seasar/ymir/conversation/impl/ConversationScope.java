package org.seasar.ymir.conversation.impl;

import static org.seasar.ymir.conversation.Globals.APPKEY_CORE_CONVERSATION_USESESSIONSCOPEASCONVERSATIONSCOPE;

import java.util.ArrayList;
import java.util.Iterator;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Globals;
import org.seasar.ymir.Request;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.conversation.Conversation;
import org.seasar.ymir.conversation.ConversationManager;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.session.SessionManager;

/**
 * conversationの範囲で有効なオブジェクトを管理するスコープを表すクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class ConversationScope implements Scope {
    public static final String ATTRPREFIX_CONVERSATION = Globals.IDPREFIX
            + "conversation.";

    public static final String ATTR_CONVERSATIONS = ATTRPREFIX_CONVERSATION
            + "conversations";

    private ApplicationManager applicationManager_;

    private AnnotationHandler annotationHandler_;

    private SessionManager sessionManager_;

    private ConversationManager conversationManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setAnnotationHandler(AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setSessionManager(SessionManager sessionManager) {
        sessionManager_ = sessionManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setConversationManager(ConversationManager conversationManager) {
        conversationManager_ = conversationManager;
    }

    public Object getAttribute(String name, Class<?> type) {
        if (name == null) {
            return null;
        }

        if (isUseSessionScopeAsConversationScope()) {
            return sessionManager_.getRawAttribute(name);
        } else {
            Conversation conversation = findCurrentConversation();
            if (conversation != null) {
                return conversation.getAttribute(name);
            } else {
                return null;
            }
        }
    }

    Conversation findCurrentConversation() {
        Conversations conversations = conversationManager_.getConversations();
        if (conversations == null) {
            return null;
        }

        String pageConversationName = getPageConversationName();
        if (pageConversationName == null) {
            return null;
        }

        Conversation conversation = conversations.getCurrentConversation();
        if (conversation == null) {
            conversation = conversations.getSuperConversation();
        }
        if (conversation == null) {
            return null;
        }

        if (!pageConversationName.equals(conversation.getName())) {
            return null;
        }

        return conversation;
    }

    String getPageConversationName() {
        org.seasar.ymir.conversation.annotation.Conversation conversation = annotationHandler_
                .getAnnotation(
                        ((Request) getS2Container().getComponent(Request.class))
                                .getCurrentDispatch().getPageComponent()
                                .getPageClass(),
                        org.seasar.ymir.conversation.annotation.Conversation.class);
        if (conversation != null) {
            return conversation.name();
        } else {
            return null;
        }
    }

    S2Container getS2Container() {
        return applicationManager_.findContextApplication().getS2Container();
    }

    public void setAttribute(String name, Object value) {
        if (name == null) {
            return;
        }

        if (isUseSessionScopeAsConversationScope()) {
            sessionManager_.setAttribute(name, value);
        } else {
            Conversation conversation = findCurrentConversation();
            if (conversation != null) {
                conversation.setAttribute(name, value);
            }
        }
    }

    boolean isUseSessionScopeAsConversationScope() {
        return PropertyUtils
                .valueOf(
                        applicationManager_
                                .findContextApplication()
                                .getProperty(
                                        APPKEY_CORE_CONVERSATION_USESESSIONSCOPEASCONVERSATIONSCOPE),
                        false);
    }

    public Iterator<String> getAttributeNames() {
        if (isUseSessionScopeAsConversationScope()) {
            // XXX 問題があれば、SessionにConversationScopeに対応するMapを格納する
            // ようにするなどして対処する。
            return new ArrayList<String>().iterator();
        } else {
            Conversation conversation = findCurrentConversation();
            if (conversation != null) {
                return conversation.getAttributeNames();
            } else {
                return new ArrayList<String>().iterator();
            }
        }
    }
}
