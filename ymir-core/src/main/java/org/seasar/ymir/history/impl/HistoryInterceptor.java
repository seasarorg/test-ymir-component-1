package org.seasar.ymir.history.impl;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Action;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Path;
import org.seasar.ymir.Request;
import org.seasar.ymir.history.HistoryElement;
import org.seasar.ymir.history.HistoryManager;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;
import org.seasar.ymir.util.RequestUtils;
import org.seasar.ymir.window.WindowManager;

public class HistoryInterceptor extends AbstractYmirProcessInterceptor {
    private HistoryManager historyManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setHistoryManager(HistoryManager historyManager) {
        historyManager_ = historyManager;
    }

    @Override
    public Action actionInvoking(Request request, Action originalAction,
            Action action) {
        if (action == originalAction && shouldRecordHistory(request)) {
            // アクションがオリジナルのままである＝正常に処理が実行される場合だけ記録するようにする。
            recordHistory(request, action);
        }
        return action;
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

        return shouldRecoredHistoryWhenHttpMethodIs(request.getMethod());
    }

    protected boolean shouldRecoredHistoryWhenHttpMethodIs(HttpMethod method) {
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
        return element;
    }
}
