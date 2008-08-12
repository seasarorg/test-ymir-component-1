package org.seasar.ymir.session.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Request;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;
import org.seasar.ymir.session.SessionManager;

/**
 * リクエスト内で変更された可能性のあるセッション属性値を更新するためのインターセプタです。
 * <p>このインターセプタはセッションレプリケーションを行なうクラスタリング環境以外では不要です。
 * </p>
 * 
 * @author yokota
 */
public class SessionInterceptor extends AbstractYmirProcessInterceptor
        implements AttributeListener {
    private SessionManager sessionManager_;

    private ThreadLocal<Set<String>> nameSets_ = new ThreadLocal<Set<String>>();

    @Binding(bindingType = BindingType.MUST)
    public void setSessionManager(SessionManager sessionManager) {
        if (!(sessionManager instanceof SessionManagerImpl)) {
            throw new RuntimeException(
                    "SessionManager must be instance of SessionManagerImpl");
        }
        ((SessionManagerImpl) sessionManager).setAttributeListener(this);
        sessionManager_ = sessionManager;
    }

    @Override
    public Request requestCreated(Request request) {
        nameSets_.set(new HashSet<String>());
        return request;
    }

    @Override
    public void leavingRequest(Request request) {
        try {
            for (Iterator<String> itr = nameSets_.get().iterator(); itr
                    .hasNext();) {
                sessionManager_.refreshAttribute(itr.next());
            }
        } finally {
            nameSets_.set(null);
        }
    }

    public void notifyGetAttribute(String name) {
        Set<String> nameSet = nameSets_.get();
        // nameSetは通常nullにはならないが、
        // YmirTestCaseを使ったテストではこうしておいた方が都合がよいのでこうしている。
        if (nameSet != null) {
            nameSet.add(name);
        }
    }

    public void notifySetAttribute(String name) {
        Set<String> nameSet = nameSets_.get();
        // nameSetは通常nullにはならないが、
        // YmirTestCaseを使ったテストではこうしておいた方が都合がよいのでこうしている。
        if (nameSet != null) {
            nameSet.add(name);
        }
    }
}
