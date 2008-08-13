package org.seasar.ymir.interceptor.impl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Request;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.hotdeploy.HotdeployEventListener;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.hotdeploy.impl.AbstractHotdeployEventListener;

/**
 * JavaRebelのような、S2以外のHOT Deploy機構を利用する際にHOT Deployイベントに関する処理を行なうインターセプタです。
 * <p>例えばJavaRebelを使って開発を行なう場合は、app.propertiesに
 * <code>s2container.disableHotdeploy=true</code>
 * というエントリを追加してS2のHOT Deploy機能を無効化した上でこのインターセプタをapp.dicon等に登録して下さい。
 * </p>
 * <p>S2のHOT Deployを利用する場合はこのインターセプタを登録しないで下さい。
 * HotdeployEventListenerのメソッドが2回呼ばれてしまいます。
 * </p>
 * 
 * @since 0.9.6
 * @author YOKOTA Takehiko
 */
public class AlternativeHotdeployInterceptor extends
        AbstractYmirProcessInterceptor {
    HotdeployManager hotdeployManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setHotdeployManager(HotdeployManager hotdeployManager) {
        hotdeployManager_ = hotdeployManager;

        hotdeployManager_
                .addEventListener(new AbstractHotdeployEventListener() {
                    @Override
                    public void stop() {
                        // アプリケーションコードやフレームワークコードからstaticメソッド呼び出しをしている場合もあると思われるので
                        // クリアするようなイベントリスナを登録しておく。
                        PropertyUtils.clearDescriptors();
                    }
                });
    }

    @Override
    public boolean enteringRequest(ServletContext context,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            String path) {
        if (isUnderDevelopment()) {
            for (HotdeployEventListener listener : hotdeployManager_
                    .getEventListeners()) {
                listener.start();
            }
        }
        return true;
    }

    boolean isUnderDevelopment() {
        return YmirContext.getYmir().isUnderDevelopment();
    }

    @Override
    public void leavingRequest(Request request) {
        if (isUnderDevelopment()) {
            for (HotdeployEventListener listener : hotdeployManager_
                    .getEventListeners()) {
                listener.stop();
            }
        }
    }
}
