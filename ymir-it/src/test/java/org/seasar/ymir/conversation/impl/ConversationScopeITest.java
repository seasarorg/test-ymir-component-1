package org.seasar.ymir.conversation.impl;

import org.seasar.ymir.conversation.ConversationUtils;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.ConversationScopeITest1Page;
import com.example.web.ConversationScopeITest2Page;
import com.example.web.ConversationScopeITest3Page;
import com.example.web.ConversationScopeITest4Page;
import com.example.web.ConversationScopeITest5Page;

public class ConversationScopeITest extends YmirTestCase {
    public void test_別のconversationから移動してきた場合はInで何もインジェクションされないこと()
            throws Exception {
        process(ConversationScopeITest1Page.class);
        process(ConversationScopeITest2Page.class);
        process(ConversationScopeITest3Page.class);

        ConversationScopeITest3Page actual = getComponent(ConversationScopeITest3Page.class);

        assertNull(actual.getValue());
    }

    public void test_別のconversationに移動する場合はOutで何もアウトジェクションされないこと()
            throws Exception {
        process(ConversationScopeITest4Page.class, "begin");
        process(ConversationScopeITest4Page.class, "next");
        process(ConversationScopeITest5Page.class);

        Conversations conversations = ConversationUtils.getConversations();
        assertNull(conversations.getAttribute("value"));
    }
}
