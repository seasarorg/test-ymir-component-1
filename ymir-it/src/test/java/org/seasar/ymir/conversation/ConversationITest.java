package org.seasar.ymir.conversation;

import org.seasar.ymir.Request;
import org.seasar.ymir.test.YmirTestCase;

public class ConversationITest extends YmirTestCase {
    public void test_subConversationからEndする時に返り値がvoidなどのActionを経由した場合のエラーを分かりやすくする()
            throws Exception {
        Request request = prepareForProcessing("/conversation.html",
                Request.METHOD_GET);
        processRequest(request);

        request = prepareForProcessing("/conversation.html",
                Request.METHOD_GET, "beginSubConversation=");
        processRequest(request);

        request = prepareForProcessing("/conversation.html", Request.METHOD_GET);
        processRequest(request);

        request = prepareForProcessing("/conversation.html",
                Request.METHOD_GET, "end=");
        try {
            processRequest(request);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(ex.getClass() == RuntimeException.class);
            System.out.println(ex.toString());
        }
    }
}
