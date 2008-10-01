package org.seasar.ymir.conversation;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.testing.YmirTestCase;

public class ConversationUtilsTest extends YmirTestCase {
    public void test() throws Exception {
        prepareForProcessing("/index.html", HttpMethod.GET);
        try {
            ConversationUtils.getConversations();
        } catch (NullPointerException ex) {
            fail();
        }
    }
}
