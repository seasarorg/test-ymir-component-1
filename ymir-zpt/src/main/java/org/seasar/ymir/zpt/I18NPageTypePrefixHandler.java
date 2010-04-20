package org.seasar.ymir.zpt;

import java.net.MalformedURLException;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.seasar.kvasir.util.LocaleUtils;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.webapp.ServletVariableResolver;

public class I18NPageTypePrefixHandler extends YmirPageTypePrefixHandler {
    @Override
    protected String filterPath(TemplateContext context,
            VariableResolver varResolver, String path) {
        ServletContext servletContext = (ServletContext) varResolver
                .getVariable(context, ServletVariableResolver.VAR_APPLICATION);
        Locale locale = (Locale) varResolver.getVariable(context,
                ServletVariableResolver.VAR_LOCALE);
        if (servletContext != null && locale != null) {
            path = determineResourcePath(servletContext, path, locale);
        }
        return path;
    }

    String determineResourcePath(ServletContext servletContext, String path,
            Locale locale) {
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
