package com.example.web;

import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.conversation.impl.ConversationScope;
import org.seasar.ymir.scope.annotation.Out;

@Conversation(name = "conversation1", phase = "1")
public class ConversationScopeITest1Page {
    @Begin
    public void _get() {
    }

    @Out(ConversationScope.class)
    public String getValue() {
        return "conversation1";
    }
}
