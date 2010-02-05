package com.example.web;

import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.BeginSubConversation;
import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.conversation.impl.ConversationScope;
import org.seasar.ymir.scope.annotation.Out;

@Conversation(name = "conversation6", phase = "phase1")
public class Conversation6Phase1Page {
    @Begin
    public void _get() {
    }

    @BeginSubConversation(reenter = "/conversation6Phase1.html?reenter=")
    public void _get_beginSub() {
    }

    @Out(scopeClass = ConversationScope.class, actionName = "_get_beginSub")
    public String getMessageForParent() {
        return "MESSAGE";
    }
}
