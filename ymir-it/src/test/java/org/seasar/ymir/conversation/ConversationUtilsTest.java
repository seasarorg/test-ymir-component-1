package org.seasar.ymir.conversation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.seasar.ymir.mock.servlet.MockFilterChainImpl;
import org.seasar.ymir.testing.YmirTestCase;

public class ConversationUtilsTest extends YmirTestCase {
    public void test() throws Exception {
        process("/index.html", new MockFilterChainImpl() {
            @Override
            public void doFilter(ServletRequest request,
                    ServletResponse response) throws IOException,
                    ServletException {
                try {
                    ConversationUtils.getConversations();
                } catch (NullPointerException ex) {
                    fail();
                }
            }
        });
    }
}
