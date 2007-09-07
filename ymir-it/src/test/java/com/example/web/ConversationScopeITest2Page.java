package com.example.web;

import org.seasar.ymir.conversation.annotation.Conversation;

@Conversation(name = "conversation1", phase = "2", followAfter = { "1" })
public class ConversationScopeITest2Page {
    public void _get() {
    }
}
