package org.seasar.ymir.scope.impl;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Action;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.handler.ExceptionHandler;
import org.seasar.ymir.impl.PageComponentImpl;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;
import org.seasar.ymir.scope.ScopeManager;

public class ScopeInterceptor extends AbstractYmirProcessInterceptor {
    private ScopeManager scopeManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setScopeManager(ScopeManager scopeManager) {
        scopeManager_ = scopeManager;
    }

    @Override
    public Action actionInvoking(Request request, Action originalAction,
            Action action) {
        request.getCurrentDispatch().getPageComponent().accept(
                new VisitorForInjecting(request.getCurrentDispatch()));

        return action;
    }

    @Override
    public Response responseCreated(Request request, Response response) {
        PageComponent pageComponent = request.getCurrentDispatch()
                .getPageComponent();
        if (pageComponent != null) {
            // 自動生成ONの場合はpageComponentはnullになることがある。
            pageComponent.accept(new VisitorForOutjecting(request
                    .getCurrentDispatch()));
        }

        return response;
    }

    @Override
    public ExceptionHandler<?> exceptionHandlerInvoking(
            ExceptionHandler<?> originalHandler, ExceptionHandler<?> handler) {
        // 各コンテキストが持つ属性をinjectする。
        PageComponent pageComponent = new PageComponentImpl(handler, handler
                .getClass());
        // actionNameはExceptionがスローされたタイミングで未決定であったり決定できていたりする。
        // そういう不確定な情報に頼るのはよろしくないので敢えてnullとみなすようにしている。
        scopeManager_.injectScopeAttributes(pageComponent, null);
        scopeManager_.populateScopeAttributes(pageComponent, null);

        return handler;
    }

    @Override
    public Response responseCreatedByExceptionHandler(
            ExceptionHandler<?> handler, Response response) {
        PageComponent pageComponent = new PageComponentImpl(handler, handler
                .getClass());
        // 各コンテキストに属性をoutjectする。
        scopeManager_.outjectScopeAttributes(pageComponent, null);

        return response;
    }

    class VisitorForInjecting extends PageComponentVisitor<Object> {
        private Dispatch dispatch_;

        public VisitorForInjecting(Dispatch dispatch) {
            dispatch_ = dispatch;
        }

        public Object process(PageComponent pageComponent) {
            // 各コンテキストが持つ属性をinjectする。
            scopeManager_.injectScopeAttributes(pageComponent, dispatch_
                    .getActionName());

            // 各コンテキストが持つ属性をpopulateする。
            scopeManager_.populateScopeAttributes(pageComponent, dispatch_
                    .getActionName());

            return null;
        }
    }

    class VisitorForOutjecting extends PageComponentVisitor<Object> {
        private Dispatch dispatch_;

        public VisitorForOutjecting(Dispatch dispatch) {
            dispatch_ = dispatch;
        }

        public Object process(PageComponent pageComponent) {
            // 各コンテキストに属性をoutjectする。
            scopeManager_.outjectScopeAttributes(pageComponent, dispatch_
                    .getActionName());

            return null;
        }
    }
}
