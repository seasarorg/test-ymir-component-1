package org.seasar.ymir.conversation;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.response.RedirectResponse;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.Conversation2Phase2Page;

public class ConversationITest extends YmirTestCase {
    public void test_subConversationからEndする時に返り値がvoidなどのActionを経由した場合のエラーを分かりやすくする()
            throws Exception {
        Request request = prepareForProcessing("/conversation.html",
                HttpMethod.GET);
        processRequest(request);

        request = prepareForProcessing("/conversation.html", HttpMethod.GET,
                "beginSubConversation=");
        processRequest(request);

        request = prepareForProcessing("/conversation.html", HttpMethod.GET);
        processRequest(request);

        request = prepareForProcessing("/conversation.html", HttpMethod.GET,
                "end=");
        try {
            processRequest(request);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(ex.getClass() == IllegalClientCodeRuntimeException.class);
        }
    }

    public void test_alwaysBeginがtrueの場合は既に同一conversationが始まっていても新たにconversationを開始すること()
            throws Exception {
        Request request = prepareForProcessing("/conversation2Phase1.html",
                HttpMethod.GET);
        processRequest(request);

        request = prepareForProcessing("/conversation2Phase2.html",
                HttpMethod.GET, "push=");
        processRequest(request);

        request = prepareForProcessing("/conversation2Phase1.html",
                HttpMethod.GET);
        processRequest(request);

        request = prepareForProcessing("/conversation2Phase2.html",
                HttpMethod.GET, "pop=");
        processRequest(request);

        Conversation2Phase2Page page = getComponent(Conversation2Phase2Page.class);

        assertNull(page.getCurrentValue());
    }

    public void test_alwaysBeginがfalseの場合は既に同一conversationが始まっていれば新たにconversationを開始しないこと()
            throws Exception {
        Request request = prepareForProcessing("/conversation2Phase1.html",
                HttpMethod.GET);
        processRequest(request);

        request = prepareForProcessing("/conversation2Phase2.html",
                HttpMethod.GET, "push=");
        processRequest(request);

        request = prepareForProcessing("/conversation2Phase1.html",
                HttpMethod.GET, "continuing=true");
        processRequest(request);

        request = prepareForProcessing("/conversation2Phase2.html",
                HttpMethod.GET, "pop=");
        processRequest(request);

        Conversation2Phase2Page page = getComponent(Conversation2Phase2Page.class);

        assertEquals("value", page.getCurrentValue());
    }

    public void test_不正な遷移をした場合に正しく検出されること() throws Exception {
        Request request = prepareForProcessing("/conversation3Phase1.html",
                HttpMethod.GET);
        processRequest(request);

        try {
            request = prepareForProcessing("/conversation3Phase2.html",
                    HttpMethod.GET);
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

    public void test_YMIR_313_返り値型がvoid型であるアクションにEndアノテーションを付与できないこと()
            throws Exception {
        // conversationを開始する。
        Request request = prepareForProcessing("/conversation4Phase1.html",
                HttpMethod.GET);
        processRequest(request);

        // sub conversationの開始準備をする。
        request = prepareForProcessing("/conversation4Phase1.html?beginsub=",
                HttpMethod.GET);
        processRequest(request);

        // sub conversationを開始する。
        request = prepareForProcessing("/conversation5Phase1.html",
                HttpMethod.GET);
        processRequest(request);

        // sub conversationを終了する。（返り値型void）
        request = prepareForProcessing("/conversation5Phase1.html?endVoid=",
                HttpMethod.GET);
        try {
            // この時点で失敗する。
            processRequest(request);
            fail();
        } catch (IllegalClientCodeRuntimeException expected) {
        }
    }

    public void test_YMIR_313_返り値型がObject型であるアクションにEndアノテーションを付与できること()
            throws Exception {
        // conversationを開始する。
        Request request = prepareForProcessing("/conversation4Phase1.html",
                HttpMethod.GET);
        processRequest(request);

        // sub conversationの開始準備をする。
        request = prepareForProcessing("/conversation4Phase1.html?beginsub=",
                HttpMethod.GET);
        processRequest(request);

        // sub conversationを開始する。
        request = prepareForProcessing("/conversation5Phase1.html",
                HttpMethod.GET);
        processRequest(request);

        // sub conversationを終了する。（返り値型Object）
        request = prepareForProcessing("/conversation5Phase1.html?endObject=",
                HttpMethod.GET);
        Response response = processRequest(request);
        assertTrue(response instanceof RedirectResponse);
        assertEquals("conversation4Phase1.html?continue=",
                ((RedirectResponse) response).getPath());
    }

    public void test_YMIR_313_返り値型がString型であるアクションにEndアノテーションを付与できること()
            throws Exception {
        // conversationを開始する。
        Request request = prepareForProcessing("/conversation4Phase1.html",
                HttpMethod.GET);
        processRequest(request);

        // sub conversationの開始準備をする。
        request = prepareForProcessing("/conversation4Phase1.html?beginsub=",
                HttpMethod.GET);
        processRequest(request);

        // sub conversationを開始する。
        request = prepareForProcessing("/conversation5Phase1.html",
                HttpMethod.GET);
        processRequest(request);

        // sub conversationを終了する。（返り値型String）
        request = prepareForProcessing("/conversation5Phase1.html?endString=",
                HttpMethod.GET);
        Response response = processRequest(request);
        assertTrue(response instanceof RedirectResponse);
        assertEquals("conversation4Phase1.html?continue=",
                ((RedirectResponse) response).getPath());
    }

    public void test_YMIR_313_返り値型がResponse型であるアクションにEndアノテーションを付与できること()
            throws Exception {
        // conversationを開始する。
        Request request = prepareForProcessing("/conversation4Phase1.html",
                HttpMethod.GET);
        processRequest(request);

        // sub conversationの開始準備をする。
        request = prepareForProcessing("/conversation4Phase1.html?beginsub=",
                HttpMethod.GET);
        processRequest(request);

        // sub conversationを開始する。
        request = prepareForProcessing("/conversation5Phase1.html",
                HttpMethod.GET);
        processRequest(request);

        // sub conversationを終了する。（返り値型Response）
        request = prepareForProcessing(
                "/conversation5Phase1.html?endResponse=", HttpMethod.GET);
        Response response = processRequest(request);
        assertTrue(response instanceof RedirectResponse);
        assertEquals("conversation4Phase1.html?continue=",
                ((RedirectResponse) response).getPath());
    }
}
