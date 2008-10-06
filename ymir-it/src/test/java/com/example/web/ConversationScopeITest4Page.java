package com.example.web;

import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.BeginSubConversation;
import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.conversation.impl.ConversationScope;
import org.seasar.ymir.scope.annotation.Out;

@Conversation(name = "conversation3", phase = "1")
public class ConversationScopeITest4Page {
    @Begin
    public void _get_begin() {
    }

    @BeginSubConversation(reenter = "/conversationScopeITest6.html")
    public void _get_next() {
    }

    @Out(scopeClass = ConversationScope.class)
    public String getValue() {
        return "conversation3";
    }
}
