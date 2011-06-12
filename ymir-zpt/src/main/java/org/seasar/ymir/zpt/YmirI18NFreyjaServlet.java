package org.seasar.ymir.zpt;

import java.net.MalformedURLException;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.seasar.kvasir.util.LocaleUtils;

/**
 * ZPTテンプレートをロケールに従って切り替えるような{@link YmirFreyjaServlet}のサブクラスです。
 * <p><strong>注：</storng>このクラスはFreyja 1.0.18以降とともに使用する必要があります。</p>
 * 
 * @author skirnir
 * @since 1.0.8
 */
public class YmirI18NFreyjaServlet extends YmirFreyjaServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected String getPath(HttpServletRequest request) {
        return determineResourcePath(super.getPath(request), getLocale(request));
    }

    protected String determineResourcePath(String path, Locale locale) {
        if (!path.startsWith("/")) {
            return path;
        }

        String pathPrefix;
        String pathSuffix;
        int dot = path.lastIndexOf('.');
        int slash = path.lastIndexOf('/');
        if (slash < dot) {
            pathPrefix = path.substring(0, dot);
            pathSuffix = path.substring(dot);
        } else {
            pathPrefix = path;
            pathSuffix = "";
        }

        ServletContext servletContext = getServletContext();
        String[] suffixes = LocaleUtils.getSuffixes(locale);
        for (int i = 0; i < suffixes.length; i++) {
            String localizedPath = pathPrefix + "_" + suffixes[i] + pathSuffix;
            try {
                if (servletContext.getResource(localizedPath) != null) {
                    return localizedPath;
                }
            } catch (MalformedURLException ignore) {
            }
        }
        return path;
    }
}
