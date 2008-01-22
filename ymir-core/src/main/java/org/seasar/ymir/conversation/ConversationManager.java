package org.seasar.ymir.conversation;

import org.seasar.ymir.Globals;

public interface ConversationManager {
    String ATTRPREFIX_CONVERSATION = Globals.IDPREFIX + "conversation.";

    String ATTR_CONVERSATIONS = ATTRPREFIX_CONVERSATION + "conversations";

    Conversations getConversations();

    Conversations getConversations(boolean create);
}
