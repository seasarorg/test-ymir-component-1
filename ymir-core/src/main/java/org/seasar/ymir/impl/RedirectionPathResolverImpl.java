package org.seasar.ymir.impl;

import org.seasar.ymir.RedirectionPathResolver;
import org.seasar.ymir.Request;

public class RedirectionPathResolverImpl implements RedirectionPathResolver {
    public String resolve(String path, Request request) {
        if (path == null) {
            return null;
        }

        if (path.length() == 0 || path.startsWith("?") || path.startsWith(";")) {
            // 空パスへのリダイレクトはYmirの世界ではルートパスへのリダイレクトとみなすのでこうしている。
            path = "/" + path;
        }

        if (path.startsWith("/")) {
            path = request.getContextPath() + path;
        }

        return path;
    }
}
