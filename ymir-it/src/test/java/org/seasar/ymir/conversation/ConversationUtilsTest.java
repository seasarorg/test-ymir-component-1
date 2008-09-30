package org.seasar.ymir.conversation;

import org.seasar.ymir.Request;
import org.seasar.ymir.testing.YmirTestCase;

public class ConversationUtilsTest extends YmirTestCase {
    public void test() throws Exception {
        prepareForProcessing("/index.html", Request.METHOD_GET);
        try {
            ConversationUtils.getConversations();
        } catch (NullPointerException ex) {
            fail();
        }
    }
}
