package org.seasar.ymir.conversation;

import org.seasar.ymir.annotation.ForTesting;

/**
 * conversationに関する定数を定義するインタフェースです。
 * 
 * @author YOKOTA Takehiko
 */
public interface Globals extends org.seasar.ymir.Globals {
    /**
     * conversationに関するアプリケーションプロパティのキーの接頭辞です。
     */
    String APPKEYPREFIX_CORE_CONVERSATION = APPKEYPREFIX_CORE + "conversation.";

    /**
     * 開始時のチェックを行なわないようにするかどうかを表すアプリケーションプロパティのキーです。
     * <p>このアプリケーションプロパティはテストコードの中で利用するために用意されています。
     * </p>
     */
    @ForTesting
    String APPKEY_CORE_CONVERSATION_DISABLEBEGINCHECK = APPKEYPREFIX_CORE_CONVERSATION
            + "disableBeginCheck";

    /**
     * convresationスコープの代わりにsessionスコープを使うようにするかどうかを表すアプリケーションプロパティのキーです。
     * <p>このアプリケーションプロパティはテストコードの中で利用するために用意されています。
     * </p>
     */
    @ForTesting
    String APPKEY_CORE_CONVERSATION_USESESSIONSCOPEASCONVERSATIONSCOPE = APPKEYPREFIX_CORE_CONVERSATION
            + "useSessionScopeAsConversationScope";

    String APPKEY_CORE_CONVERSATION_ACCEPTBROWSERSBACKBUTTON = APPKEYPREFIX_CORE_CONVERSATION
            + "acceptBrowsersBackButton";
}
