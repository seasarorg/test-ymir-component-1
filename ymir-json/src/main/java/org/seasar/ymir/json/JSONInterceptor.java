package org.seasar.ymir.json;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.ymir.Action;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.Request;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;

import net.sf.json.JSONObject;

public class JSONInterceptor extends AbstractYmirProcessInterceptor {
    private TypeConversionManager typeConversionManager_;

    private static final Set<String> CONTENT_TYPES = Collections
            .unmodifiableSet(new HashSet<String>(Arrays.asList(new String[] {
                "text/javascript", "application/json" })));

    @Binding(bindingType = BindingType.MUST)
    public void setTypeConversionManager(
            TypeConversionManager typeConversionManager) {
        typeConversionManager_ = typeConversionManager;
    }

    @Override
    public Action actionInvoking(Request request, Action originalAction,
            Action action) throws PermissionDeniedException {
        JSONObject jsonObject = parseJSONRequest();
        if (jsonObject != null) {
            request.getCurrentDispatch().getPageComponent().accept(
                    newVisitor(jsonObject));
        }
        return action;
    }

    protected PageComponentVisitor newVisitor(JSONObject jsonObject) {
        return new JSONInjectionVisitor(jsonObject, typeConversionManager_);
    }

    protected JSONObject parseJSONRequest() {
        HttpServletRequest httpRequest = getHttpServletRequest();
        if (!isJSONRequest(httpRequest)) {
            return null;
        }

        try {
            if (httpRequest.getCharacterEncoding() == null) {
                httpRequest.setCharacterEncoding("UTF-8");
            }
            return JSONObject.fromObject(IOUtils.readString(httpRequest
                    .getReader(), false));
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
        return (HttpServletRequest) YmirContext.getYmir().getApplication()
                .getS2Container().getComponent(HttpServletRequest.class);
    }
}
