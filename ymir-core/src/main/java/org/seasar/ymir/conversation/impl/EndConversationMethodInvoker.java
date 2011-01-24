package org.seasar.ymir.conversation.impl;

import org.seasar.ymir.ActionManager;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.MethodInvokerWrapper;
import org.seasar.ymir.Response;
import org.seasar.ymir.conversation.ConversationUtils;

public class EndConversationMethodInvoker extends MethodInvokerWrapper {
    private ActionManager actionManager_;

    public EndConversationMethodInvoker(MethodInvoker methodInvoker,
            ActionManager actionManager) {
        super(methodInvoker);
        actionManager_ = actionManager;
    }

    public Object invoke(Object component, Object[] parameters) {
        RuntimeException exception = null;
        Object returned = null;
        try {
            returned = methodInvoker_.invoke(component);
        } catch (RuntimeException ex) {
            exception = ex;
        } finally {
            // 例外がスローされた場合でもConversationを終了する。
            // そうしたくない場合、例えば楽観的排他制御をしていて他人に更新されていたので
            // 入力フォームに戻したい場合などは、更新処理の後に一度リダイレクトして、
            // リダイレクト先のアクションに@Endアノテーションをつけるなどすれば良い。
            Object reenterResponse = ConversationUtils.getConversations().end();
            if (exception != null) {
                throw exception;
            } else if (reenterResponse != null) {
                if (methodInvoker_.getReturnType() == Response.class) {
                    // Pageオブジェクトとしてnullを渡しているのは、今のところsub conversationを開始したPageオブジェクトを保存するようになっていないから。
                    // 従って、reenterとして「path(a,b)」形式のようなパスを指定することはできない。
                    returned = actionManager_.constructResponse(null,
                            reenterResponse.getClass(), reenterResponse);
                } else {
                    returned = reenterResponse;
                }
            }
        }
        return returned;
    }
}
