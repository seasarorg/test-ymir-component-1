package org.seasar.ymir.conversation;

/**
 * 現在のConversationの情報を表すインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @see Conversations
 * @author YOKOTA Takehiko
 */
public interface Conversation {
    /**
     * Conversationの名前を返します。
     * 
     * @return Conversationの名前。
     */
    String getName();

    /**
     * このフェーズにバインドされている属性のうち、
     * 指定されている名前の属性の値を返します。
     * 
     * @param name 属性の名前。
     * @return 属性の値。存在しない場合はnullを返します。
     */
    Object getAttribute(String name);

    /**
     * 指定された名前と値を持つ属性をこのフェーズにバインドします。
     * 
     * @param name 属性の名前。
     * @param value 属性の値。nullを指定すると属性が削除されます。
     */
    void setAttribute(String name, Object value);

    /**
     * 現在のフェーズ名を返します。
     * 
     * @return 現在のフェーズ名。
     */
    String getPhase();

    /**
     * 現在のフェーズ名を設定します。
     * 
     * @param phase 現在のフェーズ名。
     */
    void setPhase(String phase);

    /**
     * SubConversationが終了した時の戻り先を表すオブジェクトを返します。
     * 
     * @return SubConversationが終了した時の戻り先を表すオブジェクト。
     */
    Object getReenterResponse();

    /**
     * SubConversationが終了した時の戻り先を表すオブジェクトを設定します。
     * 
     * @param reenterResponse SubConversationが終了した時の戻り先を表すオブジェクト。
     */
    void setReenterResponse(Object reenterResponse);
}
