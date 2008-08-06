package org.seasar.ymir.conversation;

/**
 * Conversationを管理するためのインタフェースです。
 * <p>Conversationに関する指定や操作は通常アノテーションを使って行ないますが、
 * アノテーションを使っては操作できない複雑な操作をしたい場合は
 * このインタフェースを使って操作することができます。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface ConversationManager {
    /**
     * 現在のセッションに格納されているConversationsオブジェクトを返します。
     * <p>Conversationsオブジェクトが見つからなかった場合は新規に作成して
     * セッションに格納した上で返します。
     * 
     * @return 現在のセッションに格納されているConversationsオブジェクト。
     */
    Conversations getConversations();

    /**
     * 現在のセッションに格納されているConversationsオブジェクトを返します。
     *
     * @param create Conversationsオブジェクトが見つからなかった場合に新規に作成するかどうか。
     * 新規に作成した場合はセッションにも格納されます。
     * @return 現在のセッションに格納されているConversationsオブジェクト。
     */
    Conversations getConversations(boolean create);
}
