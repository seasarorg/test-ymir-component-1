package com.example.web;

import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.Conversation;

@Conversation(name = "conversation", phase = "phase1", followAfter = "phase2")
public class Conversation2Phase1Page {
    @Begin
    public void _get() {
    }

    @Begin(alwaysBegin = false)
    public void _get_continuing() {
    }
}
