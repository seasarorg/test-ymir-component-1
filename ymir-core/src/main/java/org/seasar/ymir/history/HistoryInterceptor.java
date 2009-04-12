package org.seasar.ymir.history;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Action;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Path;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.conversation.ConversationManager;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.history.impl.HistoryElementImpl;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;
import org.seasar.ymir.util.RequestUtils;
import org.seasar.ymir.util.ResponseUtils;
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
        if (shouldRecordHistory(request, response)) {
            // アクションがオリジナルのままである＝正常に処理が実行される場合だけ記録するようにする。
            recordHistory(request);
        }
        return response;
    }

    protected boolean shouldRecordHistory(Request request, Response response) {
        if (!historyManager_.isRecording()) {
            return false;
        }

        if (!RequestUtils.isOriginalActionInvoked(request)) {
            return false;
        }

        if (ResponseUtils.isRedirect(response)) {
            // レスポンスがリダイレクト系の場合はこの画面へのアクセスは単なる入り口でしかないと考えた方が都合が良いので
            // ヒストリを記録しない。
            return false;
        }

        return true;
    }

    protected void recordHistory(Request request) {
        historyManager_.getHistory().pushElement(newHistoryElement(request));
    }

    protected HistoryElement newHistoryElement(Request request) {
        HistoryElementImpl element = new HistoryElementImpl();
        Dispatch dispatch = request.getCurrentDispatch();
        element.setPath(new Path(dispatch.getPath(), dispatch
                .getQueryParameterMap()));

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
