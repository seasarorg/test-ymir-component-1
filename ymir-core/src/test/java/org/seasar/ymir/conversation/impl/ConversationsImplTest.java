package org.seasar.ymir.conversation.impl;

import org.seasar.ymir.conversation.BeginCondition;
import org.seasar.ymir.conversation.Conversation;
import org.seasar.ymir.conversation.IllegalTransitionRuntimeException;

import junit.framework.TestCase;

public class ConversationsImplTest extends TestCase {
    private ConversationsImpl target_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        target_ = new ConversationsImpl();
    }

    public void testBegin() throws Exception {
        assertNull(target_.getCurrentConversationName());
        assertNull(target_.getSuperConversation());

        target_.begin("conversation1", "phase1", BeginCondition.ALWAYS);

        assertEquals("conversation1", target_.getCurrentConversationName());
        assertEquals("phase1", target_.getCurrentConversation().getPhase());
        assertNull(target_.getCurrentConversation().getReenterResponse());
        assertNull(target_.getSuperConversation());
    }

    public void testEnd() throws Exception {
        target_.begin("conversation1", "phase1", BeginCondition.ALWAYS);

        assertEquals("conversation1", target_.getCurrentConversationName());

        Object response = target_.end();

        assertNull(response);
        assertNull(target_.getCurrentConversationName());
        assertNull(target_.getCurrentConversation());
        assertNull(target_.getSuperConversation());
    }

    public void testJoin() throws Exception {
        target_.begin("conversation1", "phase1", BeginCondition.ALWAYS);

        assertEquals("conversation1", target_.getCurrentConversationName());
        assertEquals("phase1", target_.getCurrentConversation().getPhase());

        target_.join("conversation1", "phase2", new String[] { "phase1" },
                false);

        assertEquals("conversation1", target_.getCurrentConversationName());
        assertEquals("phase2", target_.getCurrentConversation().getPhase());
    }

    public void testJoin_別のconversationに遷移できないこと() throws Exception {
        target_.begin("conversation1", "phase1", BeginCondition.ALWAYS);

        try {
            target_.join("conversation2", "phase2", new String[] { "phase1" },
                    false);
            fail();
        } catch (IllegalTransitionRuntimeException expected) {
        }
    }

    public void testJoin_followAfter以外のphaseからは遷移できないこと() throws Exception {
        target_.begin("conversation1", "phase1", BeginCondition.ALWAYS);

        try {
            target_.join("conversation2", "phase3", new String[] { "phase2" },
                    false);
            fail();
        } catch (IllegalTransitionRuntimeException expected) {
        }
    }

    public void testBeginSubconversation() throws Exception {
        target_.begin("conversation1", "phase1", BeginCondition.ALWAYS);

        target_.beginSubConversation("/reenter.html");

        assertNull(target_.getCurrentConversation());
        assertNull(target_.getCurrentConversationName());
        assertNotNull(target_.getSuperConversation());
        assertEquals("conversation1", target_.getSuperConversation().getName());
        assertEquals("/reenter.html", target_.getSuperConversation()
                .getReenterResponse());

        target_.begin("conversation2", "phase1", BeginCondition.ALWAYS);

        assertEquals("conversation2", target_.getCurrentConversationName());
        assertNotNull(target_.getSuperConversation());
        assertEquals("conversation1", target_.getSuperConversation().getName());

        Object response = target_.end();

        assertEquals("/reenter.html", response);
        assertEquals("conversation1", target_.getCurrentConversationName());
        assertEquals("phase1", target_.getCurrentConversation().getPhase());
    }

    public void testBeginSubconversation_元々conversationの中にいない場合はエラーになること()
            throws Exception {
        try {
            target_.beginSubConversation("/reenter.html");
            fail();
        } catch (RuntimeException expected) {
        }
    }

    public void testBeginSubconversation_subconversationからsubconversationを開始することもできること()
            throws Exception {
        target_.begin("conversation1", "phase1", BeginCondition.ALWAYS);
        target_.beginSubConversation("/reenter1.html");
        target_.begin("conversation2", "phase1", BeginCondition.ALWAYS);

        target_.beginSubConversation("/reenter2.html");
        target_.begin("conversation3", "phase1", BeginCondition.ALWAYS);

        assertEquals("conversation3", target_.getCurrentConversationName());
        assertNotNull(target_.getSuperConversation());
        assertEquals("conversation2", target_.getSuperConversation().getName());
        assertEquals("/reenter2.html", target_.getSuperConversation()
                .getReenterResponse());

        Object response = target_.end();

        assertEquals("/reenter2.html", response);
        assertEquals("conversation2", target_.getCurrentConversationName());
        assertEquals("phase1", target_.getCurrentConversation().getPhase());

        response = target_.end();

        assertEquals("/reenter1.html", response);
        assertEquals("conversation1", target_.getCurrentConversationName());
        assertEquals("phase1", target_.getCurrentConversation().getPhase());
    }

    public void testJoin_acceptBrowsersBackButtonがtrueの場合はfollowAfter以外のphaseから遷移できること()
            throws Exception {
        target_.begin("conversation1", "phase1", BeginCondition.ALWAYS);
        target_.join("conversation1", "phase2", new String[] { "phase1" },
                false);

        try {
            target_.join("conversation1", "phase1", new String[0], true);
        } catch (IllegalTransitionRuntimeException ex) {
            fail();
        }
    }

    public void testJoin_acceptBrowsersBackButtonがtrueの場合はsubConversationから元のconversationに戻れること()
            throws Exception {
        target_.begin("conversation1", "phase1", BeginCondition.ALWAYS);
        target_.join("conversation1", "phase2", new String[] { "phase1" },
                false);
        target_.beginSubConversation("/reenter1.html");
        target_.begin("subConversation1", "phase1", BeginCondition.ALWAYS);

        try {
            target_.join("conversation1", "phase1", new String[0], true);
        } catch (IllegalTransitionRuntimeException ex) {
            fail();
        }

        Conversation actual = target_.getCurrentConversation();
        assertNotNull(actual);
        assertEquals("conversation1", actual.getName());
        assertEquals("phase1", actual.getPhase());
    }

    public void testJoin_acceptBrowsersBackButtonがtrueの場合でもsubConversationから戻ろうとするconversationがconversationStackに存在しない場合は不正な遷移になること()
            throws Exception {
        target_.begin("conversation1", "phase1", BeginCondition.ALWAYS);
        target_.join("conversation1", "phase2", new String[] { "phase1" },
                false);
        target_.beginSubConversation("/reenter1.html");
        target_.begin("subConversation1", "phase1", BeginCondition.ALWAYS);

        try {
            target_.join("conversation0", "phase1", new String[0], true);
            fail();
        } catch (IllegalTransitionRuntimeException expected) {
        }
    }

    public void testBegin_acceptBrowsersBackButtonがtrueの場合でもsubConversationから戻ろうとするconversationがconversationStackに存在しない場合は不正な遷移になること()
            throws Exception {
        target_.begin("conversation1", "phase1", BeginCondition.ALWAYS);
        target_.join("conversation1", "phase2", new String[] { "phase1" },
                false);
        target_.beginSubConversation("/reenter1.html");
        target_.begin("subConversation1", "phase1", BeginCondition.ALWAYS);

        try {
            target_.join("conversation0", "phase1", new String[0], true);
            fail();
        } catch (IllegalTransitionRuntimeException expected) {
        }
    }

    public void testBegin_同じカンバセーションがスタックにあればそれ以降のカンバセーションが破棄されること()
            throws Exception {
        target_.begin("conversation1", "phase1", BeginCondition.ALWAYS);
        target_.beginSubConversation("/reenter1.html");
        target_.begin("conversation2", "phase1", BeginCondition.ALWAYS);
        target_.beginSubConversation("/reenter2.html");
        target_.begin("conversation3", "phase1", BeginCondition.ALWAYS);

        try {
            target_.begin("conversation2", "phase2", BeginCondition.ALWAYS);
        } catch (IllegalTransitionRuntimeException ex) {
            fail();
        }

        assertEquals(1, target_.getConversationStack().size());
        assertEquals("conversation1", target_.getConversationStack().get(0)
                .getName());
        assertEquals("conversation2", target_.getCurrentConversation()
                .getName());
        assertEquals("phase2", target_.getCurrentConversation().getPhase());
    }
}
