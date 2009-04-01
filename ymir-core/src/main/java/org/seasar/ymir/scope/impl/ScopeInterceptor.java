package org.seasar.ymir.scope.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ComponentMetaDataFactory;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.Phase;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.impl.PageComponentImpl;
import org.seasar.ymir.impl.VisitorForInvoking;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.util.ClassUtils;

public class ScopeInterceptor extends AbstractYmirProcessInterceptor {

    private ActionManager actionManager_;

    private ComponentMetaDataFactory componentMetaDataFactory_;

    private ScopeManager scopeManager_;

    private PageComponentVisitor<?> visitorForInvokingInPhaseObjectInjected_;

    private ThreadLocal<Boolean> injected_ = new ThreadLocal<Boolean>();

    private static final Log log_ = LogFactory.getLog(ScopeInterceptor.class);

    @Binding(bindingType = BindingType.MUST)
    public void setActionManager(ActionManager actionManager) {
        actionManager_ = actionManager;
        initPageComponentVisitor();
    }

    @Binding(bindingType = BindingType.MUST)
    public void setComponentMetaDataFactory(
            ComponentMetaDataFactory componentMetaDataFactory) {
        componentMetaDataFactory_ = componentMetaDataFactory;
        initPageComponentVisitor();
    }

    @Binding(bindingType = BindingType.MUST)
    public void setScopeManager(ScopeManager scopeManager) {
        scopeManager_ = scopeManager;
    }

    void initPageComponentVisitor() {
        if (actionManager_ == null || componentMetaDataFactory_ == null) {
            return;
        }

        visitorForInvokingInPhaseObjectInjected_ = new VisitorForInvoking(
                Phase.OBJECT_INJECTED, actionManager_,
                componentMetaDataFactory_);
    }

    boolean isInjected() {
        Boolean injected = injected_.get();
        return injected != null && injected.booleanValue();
    }

    void setInjected(boolean injected) {
        if (injected) {
            injected_.set(Boolean.TRUE);
        } else {
            injected_.set(null);
        }
    }

    @Override
    public void enteringDispatch(Request request, String path,
            Dispatcher dispatcher) {
        // 念のため。
        setInjected(false);
    }

    @Override
    public Action actionInvoking(Request request, Action action) {
        Dispatch dispatch = request.getCurrentDispatch();
        PageComponent pageComponent = dispatch.getPageComponent();
        String actionName = dispatch.getActionName();

        pageComponent.accept(new VisitorForInjecting(actionName));

        pageComponent.accept(visitorForInvokingInPhaseObjectInjected_);

        pageComponent.accept(new VisitorForPopulating(actionName));

        setInjected(true);

        return action;
    }

    @Override
    public Response responseCreated(Request request, Response response) {
        PageComponent pageComponent = request.getCurrentDispatch()
                .getPageComponent();
        if (pageComponent != null) {
            // 自動生成ONの場合はpageComponentはnullになることがある。
            pageComponent.accept(new VisitorForOutjecting(request
                    .getCurrentDispatch().getActionName()));
        }

        return response;
    }

    @Override
    public Action exceptionHandlerActionInvoking(Request request,
            Action action, boolean global) {
        // グローバルハンドラ、またはローカルハンドラでインジェクト済みでない場合は各コンテキストが持つ属性をinjectする。
        if (global) {
            Object handler = action.getTarget();
            PageComponent pageComponent = new PageComponentImpl(handler,
                    handler.getClass());
            // actionNameはExceptionがスローされたタイミングで未決定であったり決定できていたりする。
            // そういう不確定な情報に頼るのはよろしくないので敢えてnullとみなすようにしている。
            pageComponent.accept(new VisitorForInjecting(null));
            pageComponent.accept(new VisitorForPopulating(null));
        } else if (!isInjected()) {
            PageComponent pageComponent = request.getCurrentDispatch()
                    .getPageComponent();
            if (pageComponent == null) {
                Object handler = action.getTarget();
                pageComponent = new PageComponentImpl(handler, handler
                        .getClass());
            }

            // ローカルハンドラの場合は、アクションが確定していればアクションに紐づくハンドラが呼ばれた方が
            // うれしいと思われるため、アクション名を見るようにしている。
            String actionName = request.getCurrentDispatch().getActionName();
            pageComponent.accept(new VisitorForInjecting(actionName));
            pageComponent.accept(new VisitorForPopulating(actionName));
        }

        return action;
    }

    @Override
    public Response responseCreatedByExceptionHandler(Request request,
            Response response, Object handler, boolean global) {
        // グローバルハンドラの場合は各コンテキストに属性をoutjectする。
        // ローカルハンドラの場合はresponseCreated()でoutjectするので問題ない。
        if (global) {
            PageComponent pageComponent = new PageComponentImpl(handler,
                    handler.getClass());
            pageComponent.accept(new VisitorForOutjecting(null));
        }

        return response;
    }

    @Override
    public void leftDispatch(Request request) {
        setInjected(false);
    }

    class VisitorForInjecting extends PageComponentVisitor<Object> {
        private String actionName_;

        public VisitorForInjecting(String actionName) {
            actionName_ = actionName;
        }

        public Object process(PageComponent pageComponent) {
            // 各コンテキストが持つ属性をinjectする。
            if (log_.isDebugEnabled()) {
                log_.debug("Injection to "
                        + ClassUtils
                                .getPrettyName(pageComponent.getPageClass())
                        + " start");
            }
            scopeManager_.injectScopeAttributes(pageComponent, actionName_);
            if (log_.isDebugEnabled()) {
                log_.debug("Injection to "
                        + ClassUtils
                                .getPrettyName(pageComponent.getPageClass())
                        + " end");
            }

            return null;
        }
    }

    class VisitorForPopulating extends PageComponentVisitor<Object> {
        private String actionName_;

        public VisitorForPopulating(String actionName) {
            actionName_ = actionName;
        }

        public Object process(PageComponent pageComponent) {
            // 各コンテキストが持つ属性をpopulateする。
            if (log_.isDebugEnabled()) {
                log_.debug("Population to "
                        + ClassUtils
                                .getPrettyName(pageComponent.getPageClass())
                        + " start");
            }
            scopeManager_.populateScopeAttributes(pageComponent, actionName_);
            if (log_.isDebugEnabled()) {
                log_.debug("Population to "
                        + ClassUtils
                                .getPrettyName(pageComponent.getPageClass())
                        + " end");
            }

            return null;
        }
    }

    class VisitorForOutjecting extends PageComponentVisitor<Object> {
        private String actionName_;

        public VisitorForOutjecting(String actionName) {
            actionName_ = actionName;
        }

        public Object process(PageComponent pageComponent) {
            // 各コンテキストに属性をoutjectする。
            if (log_.isDebugEnabled()) {
                log_.debug("Outjection from "
                        + ClassUtils
                                .getPrettyName(pageComponent.getPageClass())
                        + " start");
            }
            scopeManager_.outjectScopeAttributes(pageComponent, actionName_);
            if (log_.isDebugEnabled()) {
                log_.debug("Outjection from "
                        + ClassUtils
                                .getPrettyName(pageComponent.getPageClass())
                        + " end");
            }

            return null;
        }
    }
}
