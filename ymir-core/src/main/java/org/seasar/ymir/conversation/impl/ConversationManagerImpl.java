package org.seasar.ymir.conversation.impl;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.ForTesting;
import org.seasar.ymir.Globals;
import org.seasar.ymir.LifecycleListener;
import org.seasar.ymir.conversation.ConversationManager;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.window.WindowManager;

public class ConversationManagerImpl implements ConversationManager,
        LifecycleListener {
    /**
     * Conversationsオブジェクトをセッションに格納する際のキーです。
     */
    private static final String ATTR_CONVERSATIONS = Globals.IDPREFIX
            + "conversation.conversations";

    private S2Container container_;

    private ApplicationManager applicationManager_;

    private HotdeployManager hotdeployManager_;

    private WindowManager windowManager_;

    S2Container getS2Container() {
        if (container_ != null) {
            return container_;
        } else {
            return applicationManager_.getContextApplication().getS2Container();
        }
    }

    @ForTesting
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

    @Binding(bindingType = BindingType.MUST)
    public void setWindowManager(WindowManager windowManager) {
        windowManager_ = windowManager;
    }

    public void init() {
        windowManager_.addStraddlingAttributeNamePattern(ATTR_CONVERSATIONS
                .replace(".", "\\."));
    }

    public void destroy() {
    }

    public Conversations getConversations() {
        return getConversations(true);
    }

    public Conversations getConversations(boolean create) {
        Conversations conversations = windowManager_
                .getScopeAttribute(ATTR_CONVERSATIONS);
        if (conversations == null && create) {
            conversations = newConversations();
            windowManager_.setScopeAttribute(ATTR_CONVERSATIONS, conversations);
        }
        return conversations;
    }

    protected Conversations newConversations() {
        ConversationsImpl impl = new ConversationsImpl();
        impl.setHotdeployManager(hotdeployManager_);
        impl.setApplicationManager(applicationManager_);
        return impl;
    }
}
