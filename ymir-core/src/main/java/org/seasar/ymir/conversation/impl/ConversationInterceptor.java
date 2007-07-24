package org.seasar.ymir.conversation.impl;

import java.lang.reflect.Method;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.Request;
import org.seasar.ymir.SessionManager;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.conversation.ConversationUtils;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.BeginSubConversation;
import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.conversation.annotation.End;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;

/**
 * Conversationスコープを実現するためのクラスです。
 */
public class ConversationInterceptor extends AbstractYmirProcessInterceptor {
    private SessionManager sessionManager_;

    public void setSessionManager(SessionManager sessionManager) {
        sessionManager_ = sessionManager;
    }

    @Override
    public void init() {
        super.init();

        sessionManager_
                .addStraddlingAttributeName(ConversationUtils.ATTR_CONVERSATIONS);
    }

    @Override
    public MethodInvoker actionInvoking(Object component, MethodInvoker action,
            Request request, MethodInvoker methodInvoker)
            throws PermissionDeniedException {
        Conversation annotation = request.getComponentClass().getAnnotation(
                Conversation.class);
        if (annotation != null) {
            Conversations conversations = ConversationUtils.getConversations();

            Method actualAction = methodInvoker.getMethod();
            if (actualAction.isAnnotationPresent(Begin.class)) {
                conversations.begin(annotation.name(), annotation.phase());
            }

            conversations.join(annotation.name(), annotation.phase(),
                    annotation.followAfter());

            BeginSubConversation beginSubConversation = actualAction
                    .getAnnotation(BeginSubConversation.class);
            if (actualAction.isAnnotationPresent(End.class)) {
                if (beginSubConversation != null) {
                    throw new RuntimeException(
                            "Can't specify both @End and @BeginSubConversation: "
                                    + actualAction.getName());
                }
                methodInvoker = new EndConversationMethodInvoker(methodInvoker);
            } else {
                if (beginSubConversation != null) {
                    methodInvoker = new BeginSubConversationMethodInvoker(
                            methodInvoker, beginSubConversation);
                }
            }
        }

        return methodInvoker;
    }

    S2Container getS2Container() {
        return YmirContext.getYmir().getApplication().getS2Container();
    }
}
