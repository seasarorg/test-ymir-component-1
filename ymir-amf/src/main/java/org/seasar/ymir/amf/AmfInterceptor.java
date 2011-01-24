package org.seasar.ymir.amf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.kvasir.util.io.impl.ByteArrayInputStreamFactory;
import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.MethodInvokerWrapper;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.cache.CacheManager;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;
import org.seasar.ymir.response.SelfContainedResponse;
import org.seasar.ymir.response.VoidResponse;
import org.seasar.ymir.util.ClassUtils;

import flex.messaging.io.amf.ActionContext;
import flex.messaging.messages.RemotingMessage;

public class AmfInterceptor extends AbstractYmirProcessInterceptor {
    private ActionManager actionManager_;

    private AnnotationHandler annotationHandler_;

    private ThreadLocal<Boolean> enabled_ = new ThreadLocal<Boolean>();

    private ThreadLocal<ActionContext> actionContext_ = new ThreadLocal<ActionContext>();

    private Map<Class<?>, Map<String, Method>> methodMap_;

    @Binding(bindingType = BindingType.MUST)
    public void setActionManager(ActionManager actionManager) {
        actionManager_ = actionManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setAnnotationHandler(AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setCacheManager(CacheManager cacheManager) {
        methodMap_ = cacheManager.newMap();
    }

    @Override
    public double getPriority() {
        return 0;
    }

    @Override
    public Response enteringRequest(ServletContext context, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, String path) {
        if (!AmfUtils.isAmfType(httpRequest)) {
            return null;
        }

        ActionContext actionContext;
        try {
            actionContext = AmfUtils.createActionContext(httpRequest);
        } catch (Throwable t) {
            AmfUtils.createActionContextForErrorResponse(t, getOutputStream(httpResponse));
            return VoidResponse.INSTANCE;
        }

        if (AmfUtils.processIfCommandMessage(actionContext)) {
            AmfUtils.sendResponseMessage(actionContext, getOutputStream(httpResponse));
            return VoidResponse.INSTANCE;
        }

        enabled_.set(Boolean.TRUE);
        actionContext_.set(actionContext);
        return null;
    }

    OutputStream getOutputStream(HttpServletResponse httpResponse) {
        try {
            return httpResponse.getOutputStream();
        } catch (IOException ex) {
            throw new IORuntimeException("Can't open response stream", ex);
        }
    }

    @Override
    public PageComponent pageComponentCreated(Request request, PageComponent pageComponent) {
        if (!isEnabled()) {
            return pageComponent;
        }

        Class<?> pageClass = pageComponent.getPageClass();
        if (!methodMap_.containsKey(pageClass)) {
            Map<String, Method> map = new HashMap<String, Method>();
            for (Method method : ClassUtils.getMethods(pageClass)) {
                Amf amf = annotationHandler_.getAnnotation(method, Amf.class);
                if (amf == null) {
                    continue;
                }
                map.put(getName(amf, method), method);
            }
            methodMap_.put(pageClass, Collections.unmodifiableMap(map));
        }
        return pageComponent;
    }

    String getName(Amf amf, Method method) {
        if (amf.value().length() > 0) {
            return amf.value();
        } else {
            return method.getName();
        }
    }

    @Override
    public Action actionInvoking(Request request, Action action) {
        if (!isEnabled()) {
            return action;
        }

        return createAction(request.getCurrentDispatch().getPageComponent());
    }

    Action createAction(PageComponent pageComponent) {
        final RemotingMessage message = AmfUtils.getRemotingMessage(getActionContext());
        if (message == null) {
            return null;
        }
        Method method = findMethod(pageComponent.getPageClass(), message.getOperation());
        if (method == null) {
            return null;
        }

        Action action = actionManager_.newAction(pageComponent.getPage(), pageComponent.getPageClass(), method, message
                .getParameters().toArray());
        return actionManager_.newAction(action, new MethodInvokerWrapper(action.getMethodInvoker()) {
            @Override
            public Class<? extends Object> getReturnType() {
                return Response.class;
            }

            @Override
            public Object invoke(Object component, Object[] parameters) throws WrappingRuntimeException {
                Object returned = super.invoke(component, parameters);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                AmfUtils.sendResopnse(getActionContext(), message, returned, baos);
                return new SelfContainedResponse(new ByteArrayInputStreamFactory(baos.toByteArray()));
            }
        });
    }

    Method findMethod(Class<?> pageClass, String name) {
        Map<String, Method> map = methodMap_.get(pageClass);
        if (map == null) {
            return null;
        } else {
            return map.get(name);
        }
    }

    @Override
    public void leavingRequest(Request request) {
        if (!isEnabled()) {
            return;
        }

        enabled_.set(null);
        actionContext_.set(null);
    }

    @Override
    public Response exceptionProcessingStarted(Request request, Throwable t) {
        if (!isEnabled()) {
            return null;
        }

        ActionContext actionContext = getActionContext();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AmfUtils.handleError(actionContext, t, baos);
        return new SelfContainedResponse(new ByteArrayInputStreamFactory(baos.toByteArray()));
    }

    boolean isEnabled() {
        Boolean enabled = enabled_.get();
        return enabled != null ? enabled.booleanValue() : false;
    }

    ActionContext getActionContext() {
        return actionContext_.get();
    }
}
