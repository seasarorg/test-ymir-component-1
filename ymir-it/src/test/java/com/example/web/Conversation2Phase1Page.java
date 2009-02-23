package com.example.web;

import org.seasar.ymir.conversation.BeginCondition;
import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.conversation.impl.ConversationScope;
import org.seasar.ymir.scope.annotation.In;

@Conversation(name = "conversation", phase = "phase1", followAfter = "phase2")
public class Conversation2Phase1Page {
    private String value_;

    @In(scopeClass = ConversationScope.class, actionName = "_get")
    public void setValue(String value) {
        value_ = value;
    }

    @Begin
    public void _get() {
    }

    @Begin(where = BeginCondition.EXCEPT_FOR_SAME_CONVERSATION)
    public void _get_continuing() {
    }

    @Begin(where = BeginCondition.EXCEPT_FOR_SAME_CONVERSATION_AND_SAME_PHASE)
    public void _get_continuing2() {
    }

    public String getCurrentValue() {
        return value_;
    }
}
