package org.seasar.ymir.redirection.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.util.ResponseUtils;

public class RequestParameterScopeIdManager extends AbstractScopeIdManager {
    public String getScopeId() {
        return getRequest().getParameter(getScopeIdKey());
    }

    public String populateScopeId(boolean scopeExists) {
        String scopeId = getScopeIdForNextRequest();

        if (scopeExists) {
            Response response = getResponse();
            if (ResponseUtils.isProceed(response)
                    || response.getType() == ResponseType.REDIRECT) {
                String path = response.getPath();
                StringBuilder sb = new StringBuilder();
                sb.append(path);

                int question = path.indexOf('?');
                if (question < 0) {
                    // /context/path 形式。
                    sb.append("?");
                } else if (question < path.length() - 1) {
                    // /context/path?a=b 形式。
                    sb.append("&");
                } else {
                    // /context/path? 形式。
                }

                try {
                    sb.append(getScopeIdKey()).append("=").append(
                            URLEncoder.encode(scopeId, getRequest()
                                    .getCharacterEncoding()));
                } catch (UnsupportedEncodingException ex) {
                    throw new RuntimeException(ex);
                }
                response.setPath(sb.toString());
            }
        }

        return scopeId;
    }
}
