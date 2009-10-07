package org.seasar.ymir.batch;

import java.io.File;
import java.net.URL;

import javax.servlet.ServletContextEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.kvasir.util.io.impl.FileResource;
import org.seasar.ymir.mock.servlet.MockServletContext;
import org.seasar.ymir.mock.servlet.MockServletContextImpl;
import org.seasar.ymir.servlet.YmirListener;

public class BatchLauncher implements Batch {
    @SuppressWarnings("unused")
    private final Log log = LogFactory.getLog(getClass());

    private MockServletContext servletContext;

    private YmirListener listener;

    private S2Container container;

    private Class<? extends Batch> batchClass;

    private Batch batch;

    public BatchLauncher(Class<? extends Batch> batchClass) {
        this.batchClass = batchClass;
    }

    public final boolean init(String[] args) throws Exception {
        servletContext = new MockServletContextImpl("/");
        servletContext.setRoot(new FileResource(getBuildDir(Bootstrap.class).getParentFile().getParentFile()));
        servletContext.setInitParameter(YmirListener.CONFIG_PATH_KEY, "ymir.dicon");

        listener = new YmirListener();
        listener.contextInitialized(new ServletContextEvent(servletContext));

        container = SingletonS2ContainerFactory.getContainer();

        batch = (Batch) container.getComponent(batchClass);

        return batch.init(args);
    }

    public final int execute() throws Exception {
        return batch.execute();
    }

    public final void destroy() throws Exception {
        try {
            batch.destroy();
        } finally {
            listener.contextDestroyed(new ServletContextEvent(servletContext));
        }
    }

    public S2Container getContainer() {
        return container;
    }

    private File getBuildDir(Class<?> clazz) {
        return getBuildDir(ResourceUtil.getResourcePath(clazz));
    }

    // S2.4.20のResourceUtil.getBuildDir(String)はjarのパスを与えた
    // 場合にjar自身を差すFileオブジェクトを返してしまう不具合があるため
    // 自前で実装している。
    private File getBuildDir(String path) {
        File dir = null;
        URL url = ResourceUtil.getResource(path);
        if ("file".equals(url.getProtocol())) {
            int num = path.split("/").length;
            dir = new File(ResourceUtil.getFileName(url));
            for (int i = 0; i < num; ++i, dir = dir.getParentFile()) {
            }
        } else {
            dir = new File(JarFileUtil.toJarFilePath(url)).getParentFile();
        }
        return dir;
    }
}
