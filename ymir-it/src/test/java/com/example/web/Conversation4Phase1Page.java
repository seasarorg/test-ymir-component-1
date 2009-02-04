package com.example.web;

import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.BeginSubConversation;
import org.seasar.ymir.conversation.annotation.Conversation;

@Conversation(name = "conversation", phase = "phase1")
public class Conversation4Phase1Page {
    @Begin
    public void _get() {
    }

    @BeginSubConversation(reenter = "redirect:conversation4Phase1.html?continue=")
    public String _get_beginsub() {
        return "redirect:conversation5Phase1.html";
    }

    public void _get_continue() {
    }
}
