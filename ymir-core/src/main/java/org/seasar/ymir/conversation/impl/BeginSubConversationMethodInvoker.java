package org.seasar.ymir.conversation.impl;

import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.MethodInvokerWrapper;
import org.seasar.ymir.conversation.ConversationUtils;
import org.seasar.ymir.conversation.annotation.BeginSubConversation;

public class BeginSubConversationMethodInvoker extends MethodInvokerWrapper {
    private BeginSubConversation annotation_;

    public BeginSubConversationMethodInvoker(MethodInvoker methodInvoker,
            BeginSubConversation annotation) {
        super(methodInvoker);
        annotation_ = annotation;
    }

    public Object invoke(Object component, Object[] parameters) {
        Object returned = methodInvoker_.invoke(component, parameters);
        ConversationUtils.getConversations().beginSubConversation(
                annotation_.reenter());
        return returned;
    }
}
