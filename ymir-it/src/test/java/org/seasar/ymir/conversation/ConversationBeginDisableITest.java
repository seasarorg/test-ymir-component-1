package org.seasar.ymir.conversation;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.ymir.testing.PageTestCase;

import com.example.web.ConversationBeginDisablePage;

public class ConversationBeginDisableITest extends
        PageTestCase<ConversationBeginDisablePage> {
    @Override
    public void setUpConfiguration(Configuration configuration) {
        disableBeginCheck(configuration);
    }

    public void test_disableの時はBeginのないページにアクセスしても不正遷移にならないこと()
            throws Exception {
        try {
            process();
        } catch (IllegalTransitionRuntimeException ex) {
            fail();
        }
    }
}
