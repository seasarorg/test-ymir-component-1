package org.seasar.ymir.conversation.impl;

import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.conversation.ConversationUtils;
import org.seasar.ymir.conversation.annotation.BeginSubConversation;
import org.seasar.ymir.impl.MethodInvokerWrapper;

public class BeginSubConversationMethodInvoker extends MethodInvokerWrapper {
    private BeginSubConversation annotation_;

    public BeginSubConversationMethodInvoker(MethodInvoker methodInvoker,
            BeginSubConversation annotation) {
        super(methodInvoker);
        annotation_ = annotation;
    }

    public Object invoke(Object component) {
        Object returned = methodInvoker_.invoke(component);
        ConversationUtils.getConversations().beginSubConversation(
                annotation_.name(), annotation_.reenter());
        return returned;
    }
}
