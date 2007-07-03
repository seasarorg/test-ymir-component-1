package org.seasar.ymir.conversation;

/**
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 */
public interface Conversations {
    Conversation getCurrentConversation();

    Object getAttribute(String name);

    void setAttribute(String name, Object value);

    void begin(String conversationName, String phase);

    void join(String conversationName, String phase, String[] followAfter);

    String end();

    void beginSubConversation(String conversationName, String reenterPath);
}