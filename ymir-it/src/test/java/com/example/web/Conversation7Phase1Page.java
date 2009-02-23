package com.example.web;

import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.conversation.annotation.End;

@Conversation(name = "conversation7", phase = "phase1", followAfter = "phase2")
public class Conversation7Phase1Page {
    @Begin
    public void _get() {
    }

    @End
    public String _get_end() {
        return null;
    }
}
