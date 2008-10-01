package org.seasar.ymir;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.ymir.impl.YmirImpl;
import org.seasar.ymir.mock.MockApplication;

abstract public class ComponentClientTestCase extends TestCase {
    private S2Container container_;

    @Override
    protected void setUp() throws Exception {
        YmirContext.setYmir(new YmirImpl() {
            @Override
            public boolean isUnderDevelopment() {
                return false;
            }
        });

        container_ = S2ContainerFactory.create("ymir-component.dicon");
        container_.init();
        getComponent(ApplicationManager.class).setContextApplication(
                new MockApplication().setS2Container(container_));
    }

    protected S2Container getContainer() {
        return container_;
    }

    protected void register(Object component) {
        container_.register(component);
    }

    protected void register(Class<?> component) {
        container_.register(component);
    }

    @SuppressWarnings("unchecked")
    public <T> T getComponent(Class<T> type) {
        return (T) container_.getComponent(type);
    }
}
