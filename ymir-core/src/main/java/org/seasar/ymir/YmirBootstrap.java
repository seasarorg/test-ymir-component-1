package org.seasar.ymir;

import javax.servlet.ServletContext;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.cms.pluggable.hotdeploy.LocalHotdeployS2Container;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.ymir.impl.SingleApplication;
import org.seasar.ymir.servlet.YmirListener;

/**
 * WebアプリケーションでYmirを利用するためのブートストラップクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 *
 * @see YmirListener
 * @author YOKOTA Takehiko
 */
public class YmirBootstrap {
    private Ymir ymir_;

    /**
     * フレームワークを初期化する前に行なっておくべき準備を行ないます。
     * <p>このメソッドは{@link #init()}よりも前に呼び出す必要があります。
     * また、このメソッドはS2コンテナの初期化よりも先に呼び出す必要があります。
     * </p>
     * 
     * @param servletContext サーブレットコンテキスト。
     */
    public void prepare(ServletContext servletContext) {
        initializeApplication(servletContext);
    }

    /**
     * フレームワークを初期化してYmirオブジェクトを構築します。
     * <p>このメソッドは{@link #prepare(ServletContext)}よりも後に呼び出す必要があります。
     * また、このメソッドはS2コンテナの初期化よりも後に呼び出す必要があります。
     * </p>
     * 
     * @return 構築したYmirオブジェクト。
     */
    public Ymir init() {
        ymir_ = (Ymir) getContainer().getComponent(Ymir.class);
        YmirContext.setYmir(ymir_);
        ymir_.init();
        return ymir_;
    }

    void initializeApplication(ServletContext servletContext) {
        Class<?> landmark = null;
        try {
            landmark = Class.forName(Globals.LANDMARK_CLASSNAME);
        } catch (ClassNotFoundException ignored) {
        }
        ComponentDef cd = getContainer().getComponentDef(
                LocalHotdeployS2Container.class);
        Application application = new SingleApplication(servletContext,
                getConfiguration(), landmark, getContainer(),
                (LocalHotdeployS2Container) cd.getComponent(),
                (PathMappingProvider) getContainer().getComponent(
                        PathMappingProvider.class));

        ApplicationManager applicationManager = (ApplicationManager) getContainer()
                .getComponent(ApplicationManager.class);
        applicationManager.setBaseApplication(application);
    }

    Configuration getConfiguration() {
        return (Configuration) getContainer().getComponent(Configuration.class);
    }

    /**
     * フレームワークの利用を終了します。
     */
    public void destroy() {
        if (ymir_ != null) {
            ymir_.destroy();
        }
        YmirContext.setYmir(null);
    }

    S2Container getContainer() {
        return SingletonS2ContainerFactory.getContainer();
    }
}
