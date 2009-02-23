package com.example.web;

import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.BeginSubConversation;
import org.seasar.ymir.conversation.annotation.Conversation;

@Conversation(name = "conversation6", phase = "phase1")
public class Conversation6Phase1Page {
    @Begin
    public void _get() {
    }

    @BeginSubConversation(reenter = "/conversation6Phase1.html?reenter=")
    public void _get_beginSub() {
    }
}
