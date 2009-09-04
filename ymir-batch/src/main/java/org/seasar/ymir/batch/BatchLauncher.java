package org.seasar.ymir.batch;

import javax.servlet.ServletContextEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.kvasir.util.ClassUtils;
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
        servletContext.setRoot(new FileResource(ClassUtils.getBaseDirectory(Bootstrap.class).getParentFile()
                .getParentFile()));
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
}
