package org.seasar.ymir.impl;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.hotdeploy.impl.AbstractHotdeployEventListener;

public class ApplicationManagerImpl implements ApplicationManager {
    private HotdeployManager hotdeployManager_;

    private Application[] applications_ = new Application[0];

    private Application baseApplication_;

    private ThreadLocal<Application> application_ = new ThreadLocal<Application>();

    @Binding(bindingType = BindingType.MUST)
    public void setHotdeployManager(HotdeployManager hotdeployManager) {
        hotdeployManager_ = hotdeployManager;
    }

    public void addApplication(final Application application) {
        applications_ = (Application[]) ArrayUtil.add(applications_,
                application);

        hotdeployManager_
                .addEventListener(new AbstractHotdeployEventListener() {
                    @Override
                    public void stop() {
                        application.clear();
                    }
                });
    }

    public Application[] getApplications() {
        return applications_;
    }

    public Application getContextApplication() {
        return application_.get();
    }

    public Application findContextApplication() {
        Application application = getContextApplication();
        if (application == null) {
            application = baseApplication_;
        }
        return application;
    }

    @Binding(bindingType = BindingType.NONE)
    public void setContextApplication(Application application) {
        application_.set(application);
    }

    @Binding(bindingType = BindingType.NONE)
    public void setBaseApplication(Application application) {
        baseApplication_ = application;
        if (application != null) {
            addApplication(application);
        }
    }
}
