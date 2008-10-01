package org.seasar.ymir.conversation.impl;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.conversation.ConversationUtils;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.ConversationScopeITest3Page;

public class ConversationScopeITest extends YmirTestCase {
    public void test_別のconversationから移動してきた場合はInで何もインジェクションされないこと()
            throws Exception {
        Request request = prepareForProcessing("/conversationScopeITest1.html",
                HttpMethod.GET);
        process(request);

        request = prepareForProcessing("/conversationScopeITest2.html",
                HttpMethod.GET);
        process(request);

        request = prepareForProcessing("/conversationScopeITest3.html",
                HttpMethod.GET);
        ConversationScopeITest3Page actual = getComponent(ConversationScopeITest3Page.class);
        process(request);

        assertNull(actual.getValue());
    }

    public void test_別のconversationに移動する場合はOutで何もアウトジェクションされないこと()
            throws Exception {
        Request request = prepareForProcessing("/conversationScopeITest4.html",
                HttpMethod.GET, "begin=");
        process(request);

        request = prepareForProcessing("/conversationScopeITest4.html",
                HttpMethod.GET, "next=");
        process(request);

        request = prepareForProcessing("/conversationScopeITest5.html",
                HttpMethod.GET);
        process(request, new Test() {
            @Override
            protected void test() throws Throwable {
                Conversations conversations = ConversationUtils
                        .getConversations();
                assertNull(conversations.getAttribute("value"));
            }
        });
    }
}
