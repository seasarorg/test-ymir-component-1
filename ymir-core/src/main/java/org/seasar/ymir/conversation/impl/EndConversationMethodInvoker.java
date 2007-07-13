package org.seasar.ymir.conversation.impl;

import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.conversation.ConversationUtils;
import org.seasar.ymir.impl.MethodInvokerWrapper;

public class EndConversationMethodInvoker extends MethodInvokerWrapper {
    public EndConversationMethodInvoker(MethodInvoker methodInvoker) {
        super(methodInvoker);
    }

    public Object invoke(Object component) {
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
                returned = reenterResponse;
            }
        }
        return returned;
    }
}
