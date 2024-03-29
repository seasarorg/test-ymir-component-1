package org.seasar.ymir.conversation;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.YmirContext;

/**
 * conversationを操作するためのユーティリティクラスです。
 * <p>可能であれば{@link ConversationManager}を使ってconversationを操作するようにして下さい。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class ConversationUtils {
    private static S2Container container_;

    private ConversationUtils() {
    }

    static S2Container getS2Container() {
        if (container_ != null) {
            return container_;
        } else {
            return YmirContext.getYmir().getApplication().getS2Container();
        }
    }

    public static Conversations getConversations() {
        return getConversationManager().getConversations();
    }

    public static Conversations getConversations(boolean create) {
        return getConversationManager().getConversations(create);
    }

    static ConversationManager getConversationManager() {
        return (ConversationManager) getS2Container().getComponent(
                ConversationManager.class);
    }
}
