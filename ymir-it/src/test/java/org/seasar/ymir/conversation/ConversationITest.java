package org.seasar.ymir.conversation;

import org.seasar.ymir.Request;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.Conversation2Phase2Page;

public class ConversationITest extends YmirTestCase {
    public void test_subConversationからEndする時に返り値がvoidなどのActionを経由した場合のエラーを分かりやすくする()
            throws Exception {
        Request request = prepareForProcessing("/conversation.html",
                Request.METHOD_GET);
        processRequest(request);

        request = prepareForProcessing("/conversation.html",
                Request.METHOD_GET, "beginSubConversation=");
        processRequest(request);

        request = prepareForProcessing("/conversation.html", Request.METHOD_GET);
        processRequest(request);

        request = prepareForProcessing("/conversation.html",
                Request.METHOD_GET, "end=");
        try {
            processRequest(request);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(ex.getClass() == RuntimeException.class);
        }
    }

    public void test_alwaysBeginがtrueの場合は既に同一conversationが始まっていても新たにconversationを開始すること()
            throws Exception {
        Request request = prepareForProcessing("/conversation2Phase1.html",
                Request.METHOD_GET);
        processRequest(request);

        request = prepareForProcessing("/conversation2Phase2.html",
                Request.METHOD_GET, "push=");
        processRequest(request);

        request = prepareForProcessing("/conversation2Phase1.html",
                Request.METHOD_GET);
        processRequest(request);

        request = prepareForProcessing("/conversation2Phase2.html",
                Request.METHOD_GET, "pop=");
        processRequest(request);

        Conversation2Phase2Page page = getComponent(Conversation2Phase2Page.class);

        assertNull(page.getCurrentValue());
    }

    public void test_alwaysBeginがfalseの場合は既に同一conversationが始まっていれば新たにconversationを開始しないこと()
            throws Exception {
        Request request = prepareForProcessing("/conversation2Phase1.html",
                Request.METHOD_GET);
        processRequest(request);

        request = prepareForProcessing("/conversation2Phase2.html",
                Request.METHOD_GET, "push=");
        processRequest(request);

        request = prepareForProcessing("/conversation2Phase1.html",
                Request.METHOD_GET, "continuing=true");
        processRequest(request);

        request = prepareForProcessing("/conversation2Phase2.html",
                Request.METHOD_GET, "pop=");
        processRequest(request);

        Conversation2Phase2Page page = getComponent(Conversation2Phase2Page.class);

        assertEquals("value", page.getCurrentValue());
    }

    public void test_不正な遷移をした場合に正しく検出されること() throws Exception {
        Request request = prepareForProcessing("/conversation3Phase1.html",
                Request.METHOD_GET);
        processRequest(request);

        try {
            request = prepareForProcessing("/conversation3Phase2.html",
                    Request.METHOD_GET);
            processRequest(request);

            fail();
        } catch (IllegalTransitionRuntimeException expected) {
            Conversations conversations = ConversationUtils
                    .getConversations(false);
            assertNotNull(conversations);
            Conversation conversation = conversations.getCurrentConversation();
            assertNotNull(conversation);
            assertEquals("以前の状態が保たれていること", "conversation", conversation
                    .getName());
            assertEquals("以前の状態が保たれていること", "phase1", conversation.getPhase());
        }
    }
}
