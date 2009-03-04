package org.seasar.ymir.conversation.impl;

import org.seasar.ymir.conversation.Conversation;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.ConversationManagerImplPage;

public class ConversationManagerImplITest extends
        PageTestCase<ConversationManagerImplPage> {
    public void test() throws Exception {
        process(ConversationManagerImplPage.class);

        ConversationManagerImplPage page = getPage();

        assertNotNull(page.getConversationManager());
        Conversation conversation = page.getConversationManager()
                .getConversations().getCurrentConversation();
        assertNotNull(conversation);
        assertEquals("conversation1", conversation.getName());
        assertEquals("start", conversation.getPhase());
    }
}
