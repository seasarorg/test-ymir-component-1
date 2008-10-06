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
import org.seasar.ymir.scope.ScopeMetaData;
import org.seasar.ymir.scope.handler.ScopeAttributeInjector;
import org.seasar.ymir.scope.handler.ScopeAttributeOutjector;
import org.seasar.ymir.scope.handler.ScopeAttributePopulator;

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
    public ExceptionHandler<?> exceptionHandlerInvoking(
            ExceptionHandler<?> originalHandler, ExceptionHandler<?> handler) {
        // 各コンテキストが持つ属性をinjectする。
        PageComponent pageComponent = new PageComponentImpl(handler, handler
                .getClass());
        // actionNameはExceptionがスローされたタイミングで未決定であったり決定できていたりする。
        // そういう不確定な情報に頼るのはよろしくないので敢えてnullとみなすようにしている。
        populateScopeAttributes(pageComponent, null);
        injectScopeAttributes(pageComponent, null);

        return handler;
    }

    @Override
    public Response responseCreatedByExceptionHandler(
            ExceptionHandler<?> handler, Response response) {
        PageComponent pageComponent = new PageComponentImpl(handler, handler
                .getClass());
        // 各コンテキストに属性をoutjectする。
        outjectScopeAttributes(pageComponent, null);

        return response;
    }

    void populateScopeAttributes(PageComponent pageComponent, String actionName) {
        ScopeMetaData metaData = scopeManager_.getMetaData(pageComponent
                .getPageClass());
        ScopeAttributePopulator[] populators = metaData
                .getScopeAttributePopulators();
        for (int i = 0; i < populators.length; i++) {
            populators[i].populateTo(pageComponent.getPage(), actionName);
        }
    }

    @Override
    public Response responseCreated(Request request, Response response) {
        request.getCurrentDispatch().getPageComponent().accept(
                new VisitorForOutjecting(request.getCurrentDispatch()));

        return response;
    }

    void injectScopeAttributes(PageComponent pageComponent, String actionName) {
        ScopeMetaData metaData = scopeManager_.getMetaData(pageComponent
                .getPageClass());
        ScopeAttributeInjector[] injectors = metaData
                .getScopeAttributeInjectors();
        for (int i = 0; i < injectors.length; i++) {
            injectors[i].injectTo(pageComponent.getPage(), actionName);
        }
    }

    void outjectScopeAttributes(PageComponent pageComponent, String actionName) {
        ScopeMetaData metaData = scopeManager_.getMetaData(pageComponent
                .getPageClass());
        ScopeAttributeOutjector[] outjectors = metaData
                .getScopeAttributeOutjectors();
        for (int i = 0; i < outjectors.length; i++) {
            outjectors[i].outjectFrom(pageComponent.getPage(), actionName);
        }
    }

    class VisitorForInjecting extends PageComponentVisitor<Object> {
        private Dispatch dispatch_;

        public VisitorForInjecting(Dispatch dispatch) {
            dispatch_ = dispatch;
        }

        public Object process(PageComponent pageComponent) {
            // 各コンテキストが持つ属性をpopulateする。
            populateScopeAttributes(pageComponent, dispatch_.getActionName());

            // 各コンテキストが持つ属性をinjectする。
            injectScopeAttributes(pageComponent, dispatch_.getActionName());

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
            outjectScopeAttributes(pageComponent, dispatch_.getActionName());

            return null;
        }
    }
}
