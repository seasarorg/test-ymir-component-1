package org.seasar.ymir.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.RedirectionPathResolver;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;

public class RedirectionPathResolverImpl implements RedirectionPathResolver {
    public String resolve(String path, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, Request request, Response response) {
        if (path == null) {
            return null;
        }

        path = resolvePath(path);

        if (path.startsWith("/")) {
            path = request.getContextPath() + path;
        }

        return path;
    }

    public String resolveForProceed(String path, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, Request request, Response response) {
        if (path == null) {
            return null;
        }

        return resolvePath(path);
    }

    protected String resolvePath(String path) {
        if (path.length() == 0 || path.startsWith("?") || path.startsWith(";")) {
            // 空パスへのリダイレクトはYmirの世界ではルートパスへのリダイレクトとみなすのでこうしている。
            path = "/" + path;
        }
        return path;
    }
}
