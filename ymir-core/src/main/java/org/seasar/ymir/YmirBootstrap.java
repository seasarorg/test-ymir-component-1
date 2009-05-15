package org.seasar.ymir;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.cms.pluggable.Configuration;
import org.seasar.cms.pluggable.hotdeploy.LocalHotdeployS2Container;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.impl.SingleApplication;
import org.seasar.ymir.impl.YmirImpl;
import org.seasar.ymir.servlet.YmirListener;
import org.seasar.ymir.util.ArtifactMetaData;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.util.YmirUtils;

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
    private static final String LS = System.getProperty("line.separator");

    private static final String PATHPREFIX_POM_PROPERTIES = "/META-INF/maven/";

    private static final String PATHSUFFIX_POM_PROPERTIES = "pom.properties";

    private Ymir ymir_;

    private final Log log_ = LogFactory.getLog(YmirBootstrap.class);

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
        Configuration configuration = getConfiguration();
        ComponentDef cd = getContainer().getComponentDef(
                LocalHotdeployS2Container.class);
        Application application = new SingleApplication(servletContext,
                configuration, getLandmarks(configuration), getContainer(),
                (LocalHotdeployS2Container) cd.getComponent(),
                (PathMappingProvider) getContainer().getComponent(
                        PathMappingProvider.class));

        ApplicationManager applicationManager = (ApplicationManager) getContainer()
                .getComponent(ApplicationManager.class);
        applicationManager.setBaseApplication(application);

        String signature = null;
        ArtifactMetaData metaData = ArtifactMetaData
                .newInstance(findApplicationPomProperties(servletContext));
        if (metaData != null) {
            signature = metaData.getSignature();
        }
        if (signature == null) {
            signature = "unknown";
        }
        log_.info("Application '" + signature + "' has been initialized");
    }

    @SuppressWarnings("unchecked")
    private URL findApplicationPomProperties(ServletContext servletContext) {
        String groupPath = null;
        Set<String> paths = servletContext
                .getResourcePaths(PATHPREFIX_POM_PROPERTIES);
        if (paths == null) {
            if (log_.isDebugEnabled()) {
                log_.debug("Can't find child resource of '"
                        + PATHPREFIX_POM_PROPERTIES + "'");
            }
            return null;
        }
        for (String path : paths) {
            if (path.endsWith("/")) {
                groupPath = path;
                break;
            }
        }
        if (groupPath == null) {
            if (log_.isDebugEnabled()) {
                log_.debug("Can't find child resource of '"
                        + PATHPREFIX_POM_PROPERTIES + "'");
            }
            return null;
        }

        String artifactPath = null;
        paths = servletContext.getResourcePaths(groupPath);
        if (paths == null) {
            if (log_.isDebugEnabled()) {
                log_.debug("Can't find child resource of '" + groupPath + "'");
            }
            return null;
        }
        for (String path : paths) {
            if (path.endsWith("/")) {
                artifactPath = path;
                break;
            }
        }
        if (artifactPath == null) {
            if (log_.isDebugEnabled()) {
                log_.debug("Can't find child resource of '" + groupPath + "'");
            }
            return null;
        }

        try {
            return servletContext.getResource(artifactPath
                    + PATHSUFFIX_POM_PROPERTIES);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    Class<?>[] getLandmarks(Configuration configuration) {
        List<Class<?>> landmarkList = new ArrayList<Class<?>>();
        for (String landmarkClassName : PropertyUtils.toLines(configuration
                .getProperty(Globals.APPKEY_LANDMARK,
                        Globals.LANDMARK_CLASSNAME))) {
            try {
                landmarkList.add(ClassUtils.forName(landmarkClassName));
            } catch (ClassNotFoundException ex) {
                throw new IllegalClientCodeRuntimeException("Landmark class ("
                        + landmarkClassName + ") not found. Can't boot Ymir."
                        + LS + "You MUST put the landmark class"
                        + " in the jar" + " or in WEB-INF/classes.");
            }
        }
        return landmarkList.toArray(new Class<?>[0]);
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
