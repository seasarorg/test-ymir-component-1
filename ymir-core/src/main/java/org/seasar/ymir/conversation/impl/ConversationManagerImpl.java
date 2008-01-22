package org.seasar.ymir.conversation.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.conversation.ConversationManager;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.hotdeploy.HotdeployManager;

public class ConversationManagerImpl implements ConversationManager {
    private S2Container container_;

    private ApplicationManager applicationManager_;

    private HotdeployManager hotdeployManager_;

    S2Container getS2Container() {
        if (container_ != null) {
            return container_;
        } else {
            return YmirContext.getYmir().getApplication().getS2Container();
        }
    }

    void setS2Container(S2Container container) {
        container_ = container;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setHotdeployManager(HotdeployManager hotdeployManager) {
        hotdeployManager_ = hotdeployManager;
    }

    public Conversations getConversations() {
        return getConversations(true);
    }

    public Conversations getConversations(boolean create) {
        HttpSession session = getSession(create);
        if (session == null) {
            return null;
        } else {
            synchronized (session.getId().intern()) {
                Conversations conversations = (Conversations) session
                        .getAttribute(ATTR_CONVERSATIONS);
                if (conversations == null && create) {
                    conversations = newConversations();
                    session.setAttribute(ATTR_CONVERSATIONS, conversations);
                }
                return conversations;
            }
        }
    }

    protected Conversations newConversations() {
        ConversationsImpl impl = new ConversationsImpl();
        impl.setHotdeployManager(hotdeployManager_);
        impl.setApplicationManager(applicationManager_);
        return impl;
    }

    HttpSession getSession() {
        return getSession(true);
    }

    HttpSession getSession(boolean create) {
        return ((HttpServletRequest) getS2Container().getExternalContext()
                .getRequest()).getSession(create);
    }
}
