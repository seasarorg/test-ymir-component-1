package org.seasar.ymir.gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.MethodInvokerWrapper;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.gson.annotation.JSONResponse;
import org.seasar.ymir.gson.util.JSON;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;

import com.google.gson.Gson;

public class GsonInterceptor extends AbstractYmirProcessInterceptor {
    private ActionManager actionManager_;

    private ApplicationManager applicationManager_;

    private ThreadLocal<String> rawJson_ = new ThreadLocal<String>();

    private static final Set<String> CONTENT_TYPES = Collections
            .unmodifiableSet(new HashSet<String>(Arrays.asList(new String[] {
                "text/javascript", "application/json" })));

    @Binding(bindingType = BindingType.MUST)
    public void setActionManager(ActionManager actionManager) {
        actionManager_ = actionManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Override
    public Response enteringRequest(ServletContext context,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            String path) {
        if (!isJSONRequest(httpRequest)) {
            return null;
        }

        rawJson_.set(parseJSONRequest());
        return null;
    }

    @Override
    public Action actionInvoking(Request request, Action action) {
        boolean onlyResonse = false;
        if (!isEnabled()) {
            if (action.getMethodInvoker().getMethod().isAnnotationPresent(
                    JSONResponse.class)) {
                onlyResonse = true;
            } else {
                return action;
            }
        }

        return wrapAction(request.getCurrentDispatch().getPageComponent(),
                action, onlyResonse);
    }

    Action wrapAction(PageComponent pageComponent, Action action,
            boolean onlyResponse) {
        MethodInvoker methodInvoker = action.getMethodInvoker();
        Method method = methodInvoker.getMethod();
        Class<?>[] types = method.getParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();
        Object[] params = methodInvoker.getParameters().clone();

        Gson gson = newGson();

        if (!onlyResponse) {
            // JSONオブジェクトをインジェクトする。
            // アノテーションがあるものはYmirがスコープからインジェクトするかもしれないので無視。
            // primitive型とString型はボタンのパラメータがインジェクトされるかもしれないので無視。
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i].length == 0 && !types[i].isPrimitive()
                        && types[i] != String.class) {
                    Object json = gson.fromJson(rawJson_.get(), method
                            .getParameterTypes()[i]);
                    params[i] = json;
                    break;
                }
            }
        }

        return actionManager_
                .newAction(action, new ParametersReplacedMethodInvoker(
                        methodInvoker, params, gson));
    }

    protected Gson newGson() {
        return new Gson();
    }

    @Override
    public void leavingRequest(Request request) {
        if (!isEnabled()) {
            return;
        }

        rawJson_.set(null);
    }

    boolean isEnabled() {
        return rawJson_.get() != null;
    }

    protected String parseJSONRequest() {
        HttpServletRequest httpRequest = getHttpServletRequest();
        try {
            if (httpRequest.getCharacterEncoding() == null) {
                httpRequest.setCharacterEncoding("UTF-8");
            }
            return IOUtils.readString(httpRequest.getReader(), false);
        } catch (IOException ex) {
            throw new IORuntimeException("Can't parse JSON request", ex);
        }
    }

    protected boolean isJSONRequest(HttpServletRequest httpRequest) {
        String contentType = httpRequest.getContentType();
        if (contentType == null) {
            return false;
        }
        int semi = contentType.indexOf(';');
        if (semi >= 0) {
            contentType = contentType.substring(0, semi).trim();
        }
        return CONTENT_TYPES.contains(contentType);
    }

    HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) applicationManager_
                .findContextApplication().getS2Container().getComponent(
                        HttpServletRequest.class);
    }

    public static class ParametersReplacedMethodInvoker extends
            MethodInvokerWrapper {
        private Object[] params;

        private Gson gson;

        public ParametersReplacedMethodInvoker(MethodInvoker methodInvoker,
                Object[] params, Gson gson) {
            super(methodInvoker);
            this.params = params;
            this.gson = gson;
        }

        @Override
        public Class<? extends Object> getReturnType() {
            return Response.class;
        }

        @Override
        public Object[] getParameters() {
            return params;
        }

        @Override
        public Object invoke(Object component, Object[] parameters)
                throws WrappingRuntimeException {
            Object returned = super.invoke(component, getParameters());
            if (returned == null || returned instanceof Response) {
                return returned;
            }

            return JSON.of(gson, returned, getMethodInvoker().getReturnType());
        }
    }
}
