package com.example.web;

import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.conversation.impl.ConversationScope;
import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Out;

@Conversation(name = "conversation", phase = "phase2", followAfter = "phase1")
public class Conversation2Phase2Page {
    private String value_;

    @In(scopeClass = ConversationScope.class, actionName = "_get_pop")
    public void setValue(String value) {
        value_ = value;
    }

    public void _get_push() {
    }

    public void _get_pop() {
    }

    @Out(scopeClass = ConversationScope.class, actionName = "_get_push")
    public String getValue() {
        return "value";
    }

    public String getCurrentValue() {
        return value_;
    }
}
