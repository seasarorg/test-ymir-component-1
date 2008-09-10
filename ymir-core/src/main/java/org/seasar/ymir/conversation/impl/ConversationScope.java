package org.seasar.ymir.conversation.impl;

import static org.seasar.ymir.conversation.Globals.APPKEY_USESESSIONSCOPEASCONVERSATIONSCOPE;

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
import org.seasar.ymir.conversation.ConversationManager;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.conversation.annotation.Conversation;
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
            return sessionManager_.getAttribute(name);
        } else {
            Conversations conversations = conversationManager_
                    .getConversations();
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
        Conversation conversation = annotationHandler_
                .getAnnotation(((Request) getS2Container().getComponent(
                        Request.class)).getCurrentDispatch().getPageComponent()
                        .getPageClass(), Conversation.class);
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
            Conversations conversations = conversationManager_
                    .getConversations();
            if (conversations != null
                    && conversationNameEquals(getPageConversationName(),
                            conversations.getCurrentConversationName())) {
                conversations.setAttribute(name, value);
            }
        }
    }

    boolean isUseSessionScopeAsConversationScope() {
        return PropertyUtils.valueOf(applicationManager_
                .findContextApplication().getProperty(
                        APPKEY_USESESSIONSCOPEASCONVERSATIONSCOPE), false);
    }

    public Iterator<String> getAttributeNames() {
        if (isUseSessionScopeAsConversationScope()) {
            // TODO 問題があれば、SessionにConversationScopeに対応するMapを格納する
            // ようにするなどして対処する。
            return new ArrayList<String>().iterator();
        } else {
            Conversations conversations = conversationManager_
                    .getConversations();
            if (conversations != null
                    && conversationNameEquals(getPageConversationName(),
                            conversations.getCurrentConversationName())) {
                return conversations.getAttributeNames();
            } else {
                return new ArrayList<String>().iterator();
            }
        }
    }
}
