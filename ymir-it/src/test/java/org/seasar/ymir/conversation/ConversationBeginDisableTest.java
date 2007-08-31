package org.seasar.ymir.conversation;

import org.seasar.ymir.PageTestCase;
import org.seasar.ymir.Request;

import com.example.web.ConversationBeginDisablePage;

public class ConversationBeginDisableTest extends
        PageTestCase<ConversationBeginDisablePage> {
    @Override
    protected Class<ConversationBeginDisablePage> getPageClass() {
        return ConversationBeginDisablePage.class;
    }

    public void test_disableの時はBeginのないページにアクセスしても不正遷移にならないこと()
            throws Exception {
        disableBeginCheck();

        Request request = prepareForPrecessing(
                "/conversationBeginDisable.html", Request.METHOD_GET);
        try {
            processRequest(request);
        } catch (IllegalTransitionRuntimeException ex) {
            fail();
        }
    }
}
