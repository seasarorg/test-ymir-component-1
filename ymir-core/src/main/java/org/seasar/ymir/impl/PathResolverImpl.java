package org.seasar.ymir.impl;

import org.seasar.ymir.PathResolver;
import org.seasar.ymir.Request;

public class PathResolverImpl implements PathResolver {
    public static final String SELF = ".";

    public String resolve(String path, Request request) {
        if (path == null) {
            return null;
        }

        if (path.equals(SELF)
                || path.startsWith(SELF)
                && (path.charAt(SELF.length()) == '?' || path.charAt(SELF
                        .length()) == ';')) {
            // 自分自身へのリダイレクト。
            path = request.getPath() + path.substring(SELF.length());
        }
        if (path.length() == 0 || path.startsWith("?") || path.startsWith(";")) {
            // 空パスへのリダイレクトはYmirの世界ではルートパスへのリダイレクトとみなすのでこうしている。
            path = "/" + path;
        }

        return path;
    }
}
