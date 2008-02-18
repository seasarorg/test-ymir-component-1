package com.example.web;

import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.Conversation;

@Conversation(name = "conversation", phase = "phase1")
public class Conversation3Phase1Page {
    @Begin
    public void _get() {
    }
}
