package org.seasar.ymir.conversation;

public interface Globals {
    String APPKEYPREFIX_CONVERSATION = "core.conversation.";

    String APPKEY_DISABLEBEGINCHECK = APPKEYPREFIX_CONVERSATION
            + "disableBeginCheck";

    String APPKEY_USESESSIONSCOPEASCONVERSATIONSCOPE = APPKEYPREFIX_CONVERSATION
            + "useSessionScopeAsConversationScope";
}
