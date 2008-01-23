package com.example.web;

import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.BeginSubConversation;
import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.conversation.annotation.End;

@Conversation(name = "conversation")
public class ConversationPage {
    @Begin
    public void _get() {
    }

    @BeginSubConversation(reenter = "/reenter.html")
    public void _get_beginSubConversation() {
    }

    @End
    public void _get_end() {
    }
}
