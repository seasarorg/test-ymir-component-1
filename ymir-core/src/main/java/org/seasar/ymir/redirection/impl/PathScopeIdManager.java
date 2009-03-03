package org.seasar.ymir.redirection.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.util.ResponseUtils;

public class PathScopeIdManager extends AbstractScopeIdManager {
    public String getScopeId() {
        return getRequest().getPath();
    }

    public String populateScopeId(boolean scopeExists) {
        if (!scopeExists) {
            return null;
        }

        Response response = getResponse();
        if (!ResponseUtils.isTransitionResponse(response)) {
            return null;
        }

        return strip(response.getPath());
    }

    String strip(String path) {
        if (path == null) {
            return null;
        }
        int question = path.indexOf('?');
        if (question >= 0) {
            return stripPathParameter(path.substring(0, question));
        } else {
            return stripPathParameter(path);
        }
    }

    String stripPathParameter(String path) {
        if (path == null) {
            return null;
        }
        int semi = path.indexOf(';');
        if (semi >= 0) {
            return path.substring(0, semi);
        } else {
            return path;
        }
    }
}
