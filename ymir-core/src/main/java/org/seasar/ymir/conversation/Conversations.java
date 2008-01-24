package org.seasar.ymir.conversation;

/**
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 */
public interface Conversations {
    /**
     * 現在のconversationを表すConversationオブジェクトを返します。
     * <p>conversationに参加していない場合はnullを返します。
     * </p>
     * 
     * @return 現在のconversationを表すConversationオブジェクト。
     */
    Conversation getCurrentConversation();

    /**
     * 現在のconversationの名前を返します。
     * <p>conversationに参加していない場合はnullを返します。
     * </p>
     * 
     * @return 現在のconversationの名前。
     */
    String getCurrentConversationName();

    /**
     * super-conversationを表すConversationオブジェクトを返します。
     * <p>現在のconversationがsub-conversationでない場合はnullを返します。
     * </p>
     * 
     * @return super-conversationを表すConversationオブジェクト。
     */
    Conversation getSuperConversation();

    /**
     * super-conversationの名前を返します。
     * <p>現在のconversationがsub-conversationでない場合はnullを返します。
     * </p>
     * 
     * @return super-conversationを表すConversationオブジェクト。
     */
    String getSuperConversationName();

    Object getAttribute(String name);

    void setAttribute(String name, Object value);

    void begin(String conversationName, String phase);

    void begin(String conversationName, String phase, boolean alwaysBegin);

    void join(String conversationName, String phase, String[] followAfter);

    Object end();

    boolean isInSubConversation();

    @Deprecated
    void beginSubConversation(String conversationName, Object reenterResponse);

    void beginSubConversation(Object reenterResponse);
}
