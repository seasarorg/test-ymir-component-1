package org.seasar.ymir.impl;

import org.seasar.ymir.RedirectionPathResolver;
import org.seasar.ymir.Request;

public class DefaultRedirectionPathResolver implements RedirectionPathResolver {

    public static final String SELF = ".";

    public String resolve(String path, Request request) {
        if (path == null) {
            return null;
        }

        if (path.startsWith("/")) {
            path = request.getContextPath() + path;
        } else if (path.length() == 0 || path.startsWith("?")
                || path.startsWith(";")) {
            // 空パスへのリダイレクトはYmirの世界ではルートパスへのリダイレクトとみなすのでこうしている。
            path = request.getContextPath() + "/" + path;
        } else if (path.startsWith(SELF)) {
            // 自分自身へのリダイレクト。
            path = path.substring(SELF.length());
        }

        return path;
    }
}