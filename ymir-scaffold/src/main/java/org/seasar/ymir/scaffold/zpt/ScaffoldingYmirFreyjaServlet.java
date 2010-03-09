package org.seasar.ymir.scaffold.zpt;

import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.seasar.ymir.YmirContext;
import org.seasar.ymir.locale.LocaleManager;
import org.seasar.ymir.zpt.YmirFreyjaServlet;

import net.skirnir.freyja.TemplateSet;
import net.skirnir.freyja.impl.CascadingTemplateSet;

public class ScaffoldingYmirFreyjaServlet extends YmirFreyjaServlet {
    private static final long serialVersionUID = 1L;

    private static final String INITPARAM_ROOTPACKAGEPATH = "rootPackagePath";

    private static final String DEFAULT_ROOTPACKAGEPATH = "org/seasar/ymir/zptroot";

    private String rootPackagePath_;

    @Override
    public void init() throws ServletException {
        String rootPackagePath = getInitParameter(INITPARAM_ROOTPACKAGEPATH);
        if (rootPackagePath == null) {
            rootPackagePath = DEFAULT_ROOTPACKAGEPATH;
        }
        if (rootPackagePath.endsWith("/")) {
            rootPackagePath = rootPackagePath.substring(0, rootPackagePath
                    .length() - 1/*= "/".length() */);
        }
        rootPackagePath_ = rootPackagePath;

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
        return new CascadingTemplateSet(super.createTemplateSet(),
                new TraversingJavaResourceTemplateSet(rootPackagePath_,
                        getPageEncoding(), getTemplateEvaluator()));
    }
}
