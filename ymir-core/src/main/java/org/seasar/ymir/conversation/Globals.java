package org.seasar.ymir.conversation;

public interface Globals extends org.seasar.ymir.Globals {
    String APPKEYPREFIX_CONVERSATION = APPKEYPREFIX_CORE + "conversation.";

    String APPKEY_DISABLEBEGINCHECK = APPKEYPREFIX_CONVERSATION
            + "disableBeginCheck";

    String APPKEY_USESESSIONSCOPEASCONVERSATIONSCOPE = APPKEYPREFIX_CONVERSATION
            + "useSessionScopeAsConversationScope";
}
