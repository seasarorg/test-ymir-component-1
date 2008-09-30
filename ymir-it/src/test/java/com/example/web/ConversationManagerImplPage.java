package com.example.web;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.conversation.ConversationManager;
import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.Conversation;

@Conversation(name = "conversation1", phase = "start")
public class ConversationManagerImplPage {
    private ConversationManager conversationManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setConversationManager(ConversationManager conversationManager) {
        conversationManager_ = conversationManager;
    }

    @Begin
    public void _get() {
    }

    public ConversationManager getConversationManager() {
        return conversationManager_;
    }
}
