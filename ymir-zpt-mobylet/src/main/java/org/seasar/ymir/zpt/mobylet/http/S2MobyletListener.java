package org.seasar.ymir.zpt.mobylet.http;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.mobylet.core.util.SingletonUtils;
import org.seasar.mobylet.holder.S2MobyletSingletonHolder;
import org.seasar.mobylet.http.S2MobyletFilter;

/**
 * Mobylet-Seasar2連携のための初期化を行なうServletContextListener実装です。
 * <p>Ymir+ZPTでは{@link S2MobyletFilter}が実質使えないので、代わりにこのクラスで初期化を行なうようにして下さい。
 * </p>
 * 
 * @author yokota
 * @since 1.0.7
 */
public class S2MobyletListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (!SingletonUtils.isInitialized()) {
            SingletonUtils.initialize(S2MobyletSingletonHolder.class);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
