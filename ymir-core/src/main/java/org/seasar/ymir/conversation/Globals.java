package org.seasar.ymir.conversation;

/**
 * conversationに関する定数を定義するインタフェースです。
 * 
 * @author YOKOTA Takehiko
 */
public interface Globals extends org.seasar.ymir.Globals {
    /**
     * conversationに関するアプリケーションプロパティのキーの接頭辞です。
     */
    String APPKEYPREFIX_CONVERSATION = APPKEYPREFIX_CORE + "conversation.";

    /**
     * 開始時のチェックを行なわないようにするかどうかを表すアプリケーションプロパティのキーです。
     * <p>このアプリケーションプロパティはテストコードの中で利用するために用意されています。
     * </p>
     */
    String APPKEY_DISABLEBEGINCHECK = APPKEYPREFIX_CONVERSATION
            + "disableBeginCheck";

    /**
     * convresationスコープの代わりにsessionスコープを使うようにするかどうかを表すアプリケーションプロパティのキーです。
     * <p>このアプリケーションプロパティはテストコードの中で利用するために用意されています。
     * </p>
     */
    String APPKEY_USESESSIONSCOPEASCONVERSATIONSCOPE = APPKEYPREFIX_CONVERSATION
            + "useSessionScopeAsConversationScope";
}
