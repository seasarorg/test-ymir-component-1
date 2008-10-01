package org.seasar.ymir.conversation.impl;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.conversation.Conversation;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.ConversationManagerImplPage;

public class ConversationManagerImplITest extends
        PageTestCase<ConversationManagerImplPage> {
    @Override
    protected Class<ConversationManagerImplPage> getPageClass() {
        return ConversationManagerImplPage.class;
    }

    public void test() throws Exception {
        Request request = prepareForProcessing("/conversationManagerImpl.html",
                HttpMethod.GET);
        processRequest(request);
        ConversationManagerImplPage page = getPage();

        assertNotNull(page.getConversationManager());
        Conversation conversation = page.getConversationManager()
                .getConversations().getCurrentConversation();
        assertNotNull(conversation);
        assertEquals("conversation1", conversation.getName());
        assertEquals("start", conversation.getPhase());
    }
}
