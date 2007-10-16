package org.seasar.ymir.conversation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Globals;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.conversation.impl.ConversationsImpl;
import org.seasar.ymir.hotdeploy.HotdeployManager;

public class ConversationUtils {
    public static final String ATTRPREFIX_CONVERSATION = Globals.IDPREFIX
            + "conversation.";

    public static final String ATTR_CONVERSATIONS = ATTRPREFIX_CONVERSATION
            + "conversations";

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

    static void setS2Container(S2Container container) {
        container_ = container;
    }

    public static Conversations getConversations() {
        return getConversations(true);
    }

    public static Conversations getConversations(boolean create) {
        HttpSession session = getSession(create);
        if (session == null) {
            return null;
        } else {
            synchronized (session.getId().intern()) {
                Conversations conversations = (Conversations) session
                        .getAttribute(ATTR_CONVERSATIONS);
                if (conversations == null && create) {
                    ConversationsImpl impl = new ConversationsImpl();
                    impl.setHotdeployManager(getHotdeployManager());
                    impl.setApplicationManager(getApplicationManager());
                    conversations = impl;
                    session.setAttribute(ATTR_CONVERSATIONS, conversations);
                }
                return conversations;
            }
        }
    }

    static ApplicationManager getApplicationManager() {
        return (ApplicationManager) getS2Container().getComponent(
                ApplicationManager.class);
    }

    static HotdeployManager getHotdeployManager() {
        return (HotdeployManager) getS2Container().getComponent(
                HotdeployManager.class);
    }

    static HttpSession getSession() {
        return getSession(true);
    }

    static HttpSession getSession(boolean create) {
        return ((HttpServletRequest) getS2Container().getExternalContext()
                .getRequest()).getSession(create);
    }
}
