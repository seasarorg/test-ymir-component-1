package org.seasar.ymir.history.impl;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Action;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Path;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.conversation.ConversationManager;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.history.Conversation;
import org.seasar.ymir.history.HistoryElement;
import org.seasar.ymir.history.HistoryManager;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;
import org.seasar.ymir.util.RequestUtils;
import org.seasar.ymir.window.WindowManager;

public class HistoryInterceptor extends AbstractYmirProcessInterceptor {
    private ConversationManager conversationManager_;

    private HistoryManager historyManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setConversationManager(ConversationManager conversationManager) {
        conversationManager_ = conversationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setHistoryManager(HistoryManager historyManager) {
        historyManager_ = historyManager;
    }

    @Override
    public Response actionInvoked(Request request, Response response) {
        //        // TODO 記録するのはActionが終わった時点にしたいのと、
        //        // Actionの判定で、単に処理を追加したくてラップしたものも対象にしないといけない。
        //        if (action == originalAction && shouldRecordHistory(request)) {
        //            // アクションがオリジナルのままである＝正常に処理が実行される場合だけ記録するようにする。
        //            recordHistory(request, action);
        //        }
        //        return action;
        return response;
    }

    protected boolean shouldRecordHistory(Request request) {
        if (request.getCurrentDispatch().getDispatcher() != Dispatcher.REQUEST
                && !RequestUtils.isProceeded(request)) {
            // 直接アクセスの場合かproceedされた場合だけを記録するようにする。
            return false;
        }

        if (!historyManager_.isRecording()) {
            return false;
        }

        return shouldRecordHistoryWhenHttpMethodIs(request.getMethod());
    }

    protected boolean shouldRecordHistoryWhenHttpMethodIs(HttpMethod method) {
        return method == HttpMethod.GET;
    }

    protected void recordHistory(Request request, Action action) {
        historyManager_.getHistory().pushElement(
                newHistoryElement(request, action));
    }

    protected HistoryElement newHistoryElement(Request request, Action action) {
        HistoryElementImpl element = new HistoryElementImpl();
        Dispatch dispatch = request.getCurrentDispatch();
        element
                .setPath(new Path(dispatch.getPath(), dispatch
                        .getParameterMap()));

        Conversations conversations = conversationManager_
                .getConversations(false);
        if (conversations != null) {
            org.seasar.ymir.conversation.Conversation conversation = conversations
                    .getCurrentConversation();
            if (conversation != null) {
                element.setConversation(new Conversation(
                        conversation.getName(), conversation.getPhase()));
            }
        }
        return element;
    }
}
