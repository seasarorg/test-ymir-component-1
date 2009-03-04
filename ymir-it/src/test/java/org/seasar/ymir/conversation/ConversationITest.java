package org.seasar.ymir.conversation;

import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.Response;
import org.seasar.ymir.response.RedirectResponse;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.Conversation2Phase1Page;
import com.example.web.Conversation2Phase2Page;
import com.example.web.Conversation3Phase1Page;
import com.example.web.Conversation3Phase2Page;
import com.example.web.Conversation4Phase1Page;
import com.example.web.Conversation5Phase1Page;
import com.example.web.Conversation6Phase1Page;
import com.example.web.Conversation7Phase1Page;
import com.example.web.Conversation7Phase2Page;
import com.example.web.ConversationPage;

public class ConversationITest extends YmirTestCase {
    public void test_subConversationからEndする時に返り値がvoidなどのActionを経由した場合のエラーを分かりやすくする()
            throws Exception {
        process(ConversationPage.class);
        process(ConversationPage.class, "beginSubConversation");
        process(ConversationPage.class);

        try {
            process(ConversationPage.class, "end");
            fail();
        } catch (RuntimeException ex) {
            assertEquals(IllegalClientCodeRuntimeException.class, ex.getClass());
        }
    }

    public void test_conditionがALWAYSの場合は既に同一conversationが始まっていても新たにconversationを開始すること()
            throws Exception {
        process(Conversation2Phase1Page.class);
        process(Conversation2Phase2Page.class, "push");
        process(Conversation2Phase1Page.class);

        Conversation2Phase1Page page1 = getComponent(Conversation2Phase1Page.class);

        assertNull(page1.getCurrentValue());
    }

    public void test_conditionがEXCEPT_FOR_SAME_CONVERSATIONの場合は既に同一conversationが始まっていれば新たにconversationを開始しないこと()
            throws Exception {
        process(Conversation2Phase1Page.class);
        process(Conversation2Phase2Page.class, "push");
        process(Conversation2Phase1Page.class, "continuing");
        process(Conversation2Phase2Page.class, "pop");

        Conversation2Phase2Page page = getComponent(Conversation2Phase2Page.class);

        assertEquals("value", page.getCurrentValue());
    }

    public void test_conditionがEXCEPT_FOR_SAME_CONVERSATION_AND_SAME_PHASEの場合は既に同一conversationの同一フェーズにいれば新たにconversationを開始しないこと1()
            throws Exception {
        process(Conversation2Phase1Page.class);
        process(Conversation2Phase2Page.class, "push");
        process(Conversation2Phase1Page.class, "continuing2");
        process(Conversation2Phase2Page.class, "pop");

        Conversation2Phase2Page page = getComponent(Conversation2Phase2Page.class);

        assertNull("同一conversationでもフェーズが違う場合は新た開始されること", page
                .getCurrentValue());
    }

    public void test_conditionがEXCEPT_FOR_SAME_CONVERSATION_AND_SAME_PHASEの場合は既に同一conversationの同一フェーズにいれば新たにconversationを開始しないこと2()
            throws Exception {
        process(Conversation2Phase1Page.class);
        process(Conversation2Phase2Page.class, "push");
        process(Conversation2Phase2Page.class, "continuing");
        process(Conversation2Phase2Page.class, "pop");

        Conversation2Phase2Page page = getComponent(Conversation2Phase2Page.class);

        assertEquals("同一conversationでフェーズも同一の場合は新た開始されないこと", "value", page
                .getCurrentValue());
    }

    public void test_不正な遷移をした場合に正しく検出されること() throws Exception {
        process(Conversation3Phase1Page.class);

        try {
            process(Conversation3Phase2Page.class);

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
        process(Conversation4Phase1Page.class);

        // sub conversationの開始準備をする。
        process(Conversation4Phase1Page.class, "beginsub");

        // sub conversationを開始する。
        process(Conversation5Phase1Page.class);

        try {
            // sub conversationを終了する。（返り値型void）
            process(Conversation5Phase1Page.class, "endVoid");

            fail();
        } catch (IllegalClientCodeRuntimeException expected) {
        }
    }

    public void test_YMIR_313_返り値型がObject型であるアクションにEndアノテーションを付与できること()
            throws Exception {
        // conversationを開始する。
        process(Conversation4Phase1Page.class);

        // sub conversationの開始準備をする。
        process(Conversation4Phase1Page.class, "beginsub");

        // sub conversationを開始する。
        process(Conversation5Phase1Page.class);

        // sub conversationを終了する。（返り値型Object）
        process(Conversation5Phase1Page.class, "endObject");

        Response response = getResponse();
        assertTrue(response instanceof RedirectResponse);
        assertEquals("conversation4Phase1.html?continue=",
                ((RedirectResponse) response).getPath());
    }

    public void test_YMIR_313_返り値型がString型であるアクションにEndアノテーションを付与できること()
            throws Exception {
        // conversationを開始する。
        process(Conversation4Phase1Page.class);

        // sub conversationの開始準備をする。
        process(Conversation4Phase1Page.class, "beginsub");

        // sub conversationを開始する。
        process(Conversation5Phase1Page.class);

        // sub conversationを終了する。（返り値型String）
        process(Conversation5Phase1Page.class, "endString");

        Response response = getResponse();
        assertTrue(response instanceof RedirectResponse);
        assertEquals("conversation4Phase1.html?continue=",
                ((RedirectResponse) response).getPath());
    }

    public void test_YMIR_313_返り値型がResponse型であるアクションにEndアノテーションを付与できること()
            throws Exception {
        // conversationを開始する。
        process(Conversation4Phase1Page.class);

        // sub conversationの開始準備をする。
        process(Conversation4Phase1Page.class, "beginsub");

        // sub conversationを開始する。
        process(Conversation5Phase1Page.class);

        // sub conversationを終了する。（返り値型Response）
        process(Conversation5Phase1Page.class, "endResponse");

        Response response = getResponse();
        assertTrue(response instanceof RedirectResponse);
        assertEquals("conversation4Phase1.html?continue=",
                ((RedirectResponse) response).getPath());
    }

    public void test_YMIR_320_subConversationで再Beginするケースでは親Conversationの情報をクリアしてしまわないこと()
            throws Exception {
        process(Conversation6Phase1Page.class);
        process(Conversation6Phase1Page.class, "beginSub");
        process(Conversation7Phase1Page.class);
        process(Conversation7Phase2Page.class);
        process(Conversation7Phase1Page.class);
        process(Conversation7Phase1Page.class, "end");

        ConversationManager conversationManager = getComponent(ConversationManager.class);

        assertEquals("conversation6", conversationManager.getConversations()
                .getCurrentConversationName());
    }
}
