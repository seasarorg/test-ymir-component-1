package org.seasar.ymir.interceptor.impl;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.ComponentMetaData;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.Globals;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.Request;
import org.seasar.ymir.TypeConversionManager;

/**
 * リクエストパラメータをインジェクトするためのインターセプタです。
 * <p>アプリケーションプロパティ<code>core.requestParameter.strictInjection</code>
 * がfalseである場合にリクエストパラメータをインジェクトするためのInterceptorです。
 * </p>
 * <p>このインターセプタはYmir1.0.x系で削除されます。
 * </p>
 * @see Globals#APPKEY_CORE_REQUESTPARAMETER_STRICTINJECTION
 * @author yokota
 */
public class RequestParameterInjectionInterceptor extends
        AbstractYmirProcessInterceptor {
    private ApplicationManager applicationManager_;

    private TypeConversionManager typeConversionManager_;

    private static final Log log_ = LogFactory
            .getLog(RequestParameterInjectionInterceptor.class);

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setTypeConversionManager(
            TypeConversionManager typeConversionManager) {
        typeConversionManager_ = typeConversionManager;
    }

    @Override
    public PageComponent pageComponentCreated(Request request,
            PageComponent pageComponent) {
        if (!isStrictInjection()) {
            pageComponent.accept(new VisitorForInjectingRequestParameter(
                    request));
        }
        return pageComponent;
    }

    protected class VisitorForInjectingRequestParameter extends
            PageComponentVisitor<Object> {
        private Request request_;

        public VisitorForInjectingRequestParameter(Request request) {
            request_ = request;
        }

        public Object process(PageComponent pageComponent) {
            Object page = pageComponent.getPage();
            ComponentMetaData metaData = pageComponent
                    .getRelatedObject(ComponentMetaData.class);

            // リクエストパラメータをinjectする。
            injectProperties(page, metaData, request_.getParameterMap());

            // FormFileのリクエストパラメータをinjectする。
            injectFormFileProperties(page, metaData, request_
                    .getFileParameterMap());

            return null;
        }
    }

    void injectProperties(Object page, ComponentMetaData metaData,
            Map<String, String[]> properties) {
        if (properties == null) {
            return;
        }

        for (Iterator<String> itr = properties.keySet().iterator(); itr
                .hasNext();) {
            String name = itr.next();
            if (name == null || metaData.isProtected(name)) {
                continue;
            }
            try {
                typeConversionManager_.setProperty(page, name, properties
                        .get(name));
            } catch (Throwable t) {
                if (log_.isDebugEnabled()) {
                    log_.debug("Can't set property '" + name + "'", t);
                }
            }
        }
    }

    void injectFormFileProperties(Object page, ComponentMetaData metaData,
            Map<String, FormFile[]> properties) {
        if (properties == null) {
            return;
        }

        for (Iterator<String> itr = properties.keySet().iterator(); itr
                .hasNext();) {
            String name = itr.next();
            if (name == null || metaData.isProtected(name)) {
                continue;
            }
            try {
                typeConversionManager_.copyProperty(page, name, properties
                        .get(name));
            } catch (Throwable t) {
                if (log_.isDebugEnabled()) {
                    log_.debug("Can't copy property '" + name + "'", t);
                }
            }
        }
    }

    boolean isStrictInjection() {
        return PropertyUtils.valueOf(applicationManager_
                .findContextApplication().getProperty(
                        Globals.APPKEY_CORE_REQUESTPARAMETER_STRICTINJECTION),
                false);
    }
}
