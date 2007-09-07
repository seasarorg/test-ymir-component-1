package com.example.web;

import org.seasar.ymir.annotation.In;
import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.conversation.impl.ConversationScope;

@Conversation(name = "conversation2", phase = "1")
public class ConversationScopeITest3Page {
    private String value_;

    @In(ConversationScope.class)
    public void setValue(String value) {
        value_ = value;
    }

    @Begin
    public void _get() {
    }

    public String getValue() {
        return value_;
    }
}
