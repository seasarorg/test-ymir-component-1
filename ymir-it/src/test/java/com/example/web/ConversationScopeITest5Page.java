package com.example.web;

import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.Conversation;

@Conversation(name = "conversation4", phase = "1")
public class ConversationScopeITest5Page {
    @Begin
    public void _get() {
    }
}
