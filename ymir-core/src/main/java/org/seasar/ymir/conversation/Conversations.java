package org.seasar.ymir.conversation;

/**
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 */
public interface Conversations {
    Conversation getConversation(String conversationName);

    Conversation getConversation(String conversationName, boolean create);

    Object getAttribute(String conversationName, String name);

    void setAttribute(String conversationName, String name, Object value);

    void join(String conversationName, String phase, String[] followAfter);

    void beginSubConversation(String conversationName, String reenterPath);

    String finish();
}
