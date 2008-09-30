package org.seasar.ymir.conversation;

import org.seasar.ymir.Request;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.ConversationBeginDisablePage;

public class ConversationBeginDisableITest extends
        PageTestCase<ConversationBeginDisablePage> {
    @Override
    protected Class<ConversationBeginDisablePage> getPageClass() {
        return ConversationBeginDisablePage.class;
    }

    public void test_disableの時はBeginのないページにアクセスしても不正遷移にならないこと()
            throws Exception {
        disableBeginCheck();

        Request request = prepareForProcessing(
                "/conversationBeginDisable.html", Request.METHOD_GET);
        try {
            processRequest(request);
        } catch (IllegalTransitionRuntimeException ex) {
            fail();
        }
    }
}
