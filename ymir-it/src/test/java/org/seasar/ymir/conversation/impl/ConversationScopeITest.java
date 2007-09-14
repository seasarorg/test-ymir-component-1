package org.seasar.ymir.conversation.impl;

import org.seasar.ymir.Request;
import org.seasar.ymir.conversation.ConversationUtils;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.test.YmirTestCase;

import com.example.web.ConversationScopeITest3Page;

public class ConversationScopeITest extends YmirTestCase {
    public void test_別のconversationから移動してきた場合はInで何もインジェクションされないこと()
            throws Exception {
        Request request = prepareForProcessing("/conversationScopeITest1.html",
                Request.METHOD_GET);
        process(request);

        request = prepareForProcessing("/conversationScopeITest2.html",
                Request.METHOD_GET);
        process(request);

        request = prepareForProcessing("/conversationScopeITest3.html",
                Request.METHOD_GET);
        ConversationScopeITest3Page actual = getComponent(ConversationScopeITest3Page.class);
        process(request);

        assertNull(actual.getValue());
    }

    public void test_別のconversationに移動する場合はOutで何もアウトジェクションされないこと()
            throws Exception {
        Request request = prepareForProcessing("/conversationScopeITest4.html",
                Request.METHOD_GET, "begin=");
        process(request);

        request = prepareForProcessing("/conversationScopeITest4.html",
                Request.METHOD_GET, "next=");
        process(request);

        request = prepareForProcessing("/conversationScopeITest5.html",
                Request.METHOD_GET);
        process(request);

        Conversations conversations = ConversationUtils.getConversations();
        assertNull(conversations.getAttribute("value"));
    }
}
