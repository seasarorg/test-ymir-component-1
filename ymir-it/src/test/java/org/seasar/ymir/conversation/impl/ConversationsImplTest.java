package org.seasar.ymir.conversation.impl;

import org.seasar.ymir.conversation.Conversation;
import org.seasar.ymir.conversation.IllegalTransitionRuntimeException;

import junit.framework.TestCase;

public class ConversationsImplTest extends TestCase {
    private ConversationsImpl target_ = new ConversationsImpl();

    public void test_最初の画面から最後まで普通に遷移できること() throws Exception {
        assertNull(target_.getCurrentConversation());

        try {
            target_.begin("registerUser", "input");
        } catch (IllegalTransitionRuntimeException ex) {
            fail();
        }
        Conversation conversation = target_.getCurrentConversation();
        assertNotNull(conversation);
        assertEquals("input", conversation.getPhase());

        try {
            target_.join("registerUser", "input", new String[0]);
        } catch (IllegalTransitionRuntimeException ex) {
            fail();
        }
        conversation = target_.getCurrentConversation();
        assertNotNull(conversation);
        assertEquals("input", conversation.getPhase());

        try {
            target_.join("registerUser", "confirm", new String[] { "input" });
        } catch (IllegalTransitionRuntimeException ex) {
            fail();
        }
        conversation = target_.getCurrentConversation();
        assertNotNull(conversation);
        assertEquals("confirm", conversation.getPhase());

        try {
            target_
                    .join("registerUser", "complete",
                            new String[] { "confirm" });
        } catch (IllegalTransitionRuntimeException ex) {
            fail();
        }
        conversation = target_.getCurrentConversation();
        assertNotNull(conversation);
        assertEquals("complete", conversation.getPhase());

        Object reenterResponse = target_.end();

        assertNull(reenterResponse);
        assertNull(target_.getCurrentConversation());
    }

    public void test_開始せずに途中の画面にアクセスできないこと() throws Exception {
        try {
            target_.join("registerUser", "input", new String[0]);
            fail();
        } catch (IllegalTransitionRuntimeException expected) {
        }
    }

    public void test_開始してから途中の画面を飛ばすような遷移はできないこと() throws Exception {
        target_.begin("registerUser", "input");
        target_.join("registerUser", "input", new String[0]);

        try {
            target_
                    .join("registerUser", "complete",
                            new String[] { "confirm" });
            fail();
        } catch (IllegalTransitionRuntimeException expected) {
        }
    }

    public void test_subConversationに遷移して終わったら元のconversationに戻ること()
            throws Exception {
        target_.begin("register", "input");
        target_.join("register", "input", new String[0]);
        target_.join("register", "confirm", new String[] { "input" });
        target_.beginSubConversation("loginUser", "redirect:/completeRegister");
        target_.begin("loginUser", "input");
        target_.join("loginUser", "input", new String[0]);
        target_.join("loginUser", "complete", new String[] { "input" });
        Object reenterResponse = target_.end();

        assertEquals("redirect:/completeRegister", reenterResponse);
        Conversation conversation = target_.getCurrentConversation();
        assertNotNull(conversation);
        assertEquals("register", conversation.getName());
        assertEquals("confirm", conversation.getPhase());
    }

    public void test_開始してから終了しなくても別のconversationを開始できること() throws Exception {
        target_.begin("register", "input");
        target_.join("register", "input", new String[0]);

        try {
            target_.begin("loginUser", "input");
        } catch (IllegalTransitionRuntimeException ex) {
            fail();
        }

        Conversation conversation = target_.getCurrentConversation();
        assertNotNull(conversation);
        assertEquals("loginUser", conversation.getName());
        assertTrue(target_.getConversationStack().isEmpty());
    }

    public void test_開始してから同一conversationを開始しても何も起きないこと() throws Exception {
        target_.begin("register", "input");
        target_.join("register", "input", new String[0]);

        try {
            target_.begin("register", "input");
        } catch (IllegalTransitionRuntimeException ex) {
            fail();
        }

        Conversation conversation = target_.getCurrentConversation();
        assertNotNull(conversation);
        assertEquals("register", conversation.getName());
        assertEquals("input", conversation.getPhase());
    }

    public void test_getSuperConversation() throws Exception {
        assertNull("conversationに参加していない場合はnullが返されること", target_
                .getSuperConversation());
        assertNull("conversationに参加していない場合はnullが返されること", target_
                .getSuperConversationName());

        target_.begin("register", "input");
        target_.join("register", "input", new String[0]);
        target_.join("register", "confirm", new String[] { "input" });

        assertNull("sub-conversationに参加していない場合はnullが返されること", target_
                .getSuperConversation());
        assertNull("sub-conversationに参加していない場合はnullが返されること", target_
                .getSuperConversationName());

        Conversation superConvresation = target_.getCurrentConversation();
        target_.beginSubConversation("loginUser", "redirect:/completeRegister");

        assertSame("sub-conversationに参加している場合は元のconversationが返されること",
                superConvresation, target_.getSuperConversation());
        assertEquals("sub-conversationに参加していない場合は元のconversationの名前が返されること",
                "register", target_.getSuperConversationName());

        target_.begin("loginUser", "input");
        target_.join("loginUser", "input", new String[0]);
        target_.join("loginUser", "complete", new String[] { "input" });
        Object reenterResponse = target_.end();

        assertEquals("redirect:/completeRegister", reenterResponse);
        Conversation conversation = target_.getCurrentConversation();
        assertNotNull(conversation);
        assertEquals("register", conversation.getName());
        assertEquals("confirm", conversation.getPhase());
    }

}
