package com.example.web;

import org.seasar.ymir.annotation.Out;
import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.BeginSubConversation;
import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.conversation.impl.ConversationScope;

@Conversation(name = "conversation3", phase = "1")
public class ConversationScopeITest4Page {
    @Begin
    public void _get_begin() {
    }

    @BeginSubConversation(name = "conversation4", reenter = "/conversationScopeITest6.html")
    public void _get_next() {
    }

    @Out(scopeClass = ConversationScope.class)
    public String getValue() {
        return "conversation3";
    }
}
