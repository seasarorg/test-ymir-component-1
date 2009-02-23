package com.example.web;

import org.seasar.ymir.conversation.annotation.Conversation;

@Conversation(name = "conversation7", phase = "phase2", followAfter = "phase1")
public class Conversation7Phase2Page {
    public void _get() {
    }
}
