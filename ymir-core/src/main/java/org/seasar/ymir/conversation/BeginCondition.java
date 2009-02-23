package org.seasar.ymir.conversation;

/**
 * conversationを開始する条件を表します。
 * 
 * @since 1.0.2
 */
public enum BeginCondition {
    /** 常にConversationを開始することを表します。 */
    ALWAYS,

    /** 同一Conversationかつ同一フェーズの場合は開始しないことを表します。 */
    EXCEPT_FOR_SAME_CONVERSATION_AND_SAME_PHASE,

    /** 同一Conversationの場合は開始しないことを表します。 */
    EXCEPT_FOR_SAME_CONVERSATION;
}
