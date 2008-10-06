package org.seasar.ymir.zpt;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.seasar.ymir.YmirContext;
import org.seasar.ymir.locale.LocaleManager;

import net.skirnir.freyja.webapp.FreyjaServlet;

public class YmirFreyjaServlet extends FreyjaServlet {
    private static final long serialVersionUID = 4772325478215847574L;

    @Override
    protected Locale getLocale(HttpServletRequest request) {
        return ((LocaleManager) YmirContext.getYmir().getApplication()
                .getS2Container().getComponent(LocaleManager.class))
                .getLocale();
    }
}
