package org.seasar.ymir.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.seasar.cms.pluggable.servlet.PluggableListener;
import org.seasar.ymir.YmirBootstrap;

/**
 * フレームワークの初期化と終了を行なうための{@link ServletContextListener}クラスです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class YmirListener extends PluggableListener {
    public static final String ATTR_YMIR = "org.seasar.ymir.ymir";

    private YmirBootstrap bootstrap_ = new YmirBootstrap();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        super.contextInitialized(sce);

        ServletContext sc = sce.getServletContext();
        try {
            sc.setAttribute(ATTR_YMIR, bootstrap_.init());
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    protected void preInit(ServletContextEvent sce) {
        bootstrap_.prepare(sce.getServletContext());
    }

    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        sc.removeAttribute(ATTR_YMIR);
        bootstrap_.destroy();

        super.contextDestroyed(sce);
    }
}
