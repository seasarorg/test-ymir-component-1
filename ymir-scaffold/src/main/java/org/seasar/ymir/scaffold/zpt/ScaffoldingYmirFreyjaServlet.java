package org.seasar.ymir.scaffold.zpt;

import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.seasar.ymir.YmirContext;
import org.seasar.ymir.locale.LocaleManager;
import org.seasar.ymir.zpt.JavaResourceTemplateSet;
import org.seasar.ymir.zpt.YmirFreyjaServlet;

import net.skirnir.freyja.TemplateSet;
import net.skirnir.freyja.impl.CascadingTemplateSet;

public class ScaffoldingYmirFreyjaServlet extends YmirFreyjaServlet {
    private static final long serialVersionUID = 1L;

    private static final String INITPARAM_WEBROOTPACKAGEPATH = "webrootPackagePath";

    private static final String INITPARAM_ZPTROOTPACKAGEPATH = "zptrootPackagePath";

    private static final String INITPARAM_RESOURCEENCODING = "resourceEncoding";

    private static final String DEFAULT_WEBROOTPACKAGEPATH = "org/seasar/ymir/webroot";

    private static final String DEFAULT_ZPTROOTPACKAGEPATH = "org/seasar/ymir/zptroot";

    private static final String DEFAULT_RESOURCEENCODING = "UTF-8";

    private String webrootPackagePath_;

    private String zptrootPackagePath_;

    private String resourceEncoding_;

    @Override
    public void init() throws ServletException {
        String webrootPackagePath = getInitParameter(INITPARAM_WEBROOTPACKAGEPATH);
        if (webrootPackagePath == null) {
            webrootPackagePath = DEFAULT_WEBROOTPACKAGEPATH;
        }
        if (webrootPackagePath.endsWith("/")) {
            webrootPackagePath = webrootPackagePath.substring(0,
                    webrootPackagePath.length() - 1/*= "/".length() */);
        }
        webrootPackagePath_ = webrootPackagePath;

        String zptrootPackagePath = getInitParameter(INITPARAM_ZPTROOTPACKAGEPATH);
        if (zptrootPackagePath == null) {
            zptrootPackagePath = DEFAULT_ZPTROOTPACKAGEPATH;
        }
        if (zptrootPackagePath.endsWith("/")) {
            zptrootPackagePath = zptrootPackagePath.substring(0,
                    zptrootPackagePath.length() - 1/*= "/".length() */);
        }
        zptrootPackagePath_ = zptrootPackagePath;

        resourceEncoding_ = getInitParameter(INITPARAM_RESOURCEENCODING);
        if (resourceEncoding_ == null) {
            resourceEncoding_ = DEFAULT_RESOURCEENCODING;
        }

        super.init();
    }

    @Override
    protected Locale getLocale(HttpServletRequest request) {
        return ((LocaleManager) YmirContext.getYmir().getApplication()
                .getS2Container().getComponent(LocaleManager.class))
                .getLocale();
    }

    @Override
    protected TemplateSet createTemplateSet() {
        return new CascadingTemplateSet(new TemplateSet[] {
            super.createTemplateSet(),
            new JavaResourceTemplateSet(webrootPackagePath_, resourceEncoding_,
                    getTemplateEvaluator()),
            new TraversingJavaResourceTemplateSet(zptrootPackagePath_,
                    resourceEncoding_, getTemplateEvaluator()) });
    }
}
