package org.seasar.ymir.json;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Request;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;
import org.seasar.ymir.util.YmirUtils;

import net.sf.json.JSONObject;

public class JSONInterceptor extends AbstractYmirProcessInterceptor {
    private ApplicationManager applicationManager_;

    private static final Set<String> CONTENT_TYPES = Collections
            .unmodifiableSet(new HashSet<String>(Arrays.asList(new String[] {
                "text/javascript", "application/json" })));

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Override
    public Request requestCreated(Request request) {
        JSONObject jsonObject = parseJSONRequest();
        if (jsonObject == null) {
            return request;
        }

        Map<String, Object> extendedParameterMap = new HashMap<String, Object>();
        for (Iterator<?> itr = jsonObject.keys(); itr.hasNext();) {
            String name = (String) itr.next();
            extendedParameterMap.put(name, jsonObject.get(name));
        }
        YmirUtils.toFrameworkRequest(request).setExtendedParameterMap(
                Collections.unmodifiableMap(extendedParameterMap));

        return request;
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
        return (HttpServletRequest) applicationManager_
                .findContextApplication().getS2Container().getComponent(
                        HttpServletRequest.class);
    }
}
