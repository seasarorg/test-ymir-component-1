package org.seasar.ymir.impl;

import org.seasar.cms.pluggable.hotdeploy.AbstractHotdeployEventListener;
import org.seasar.cms.pluggable.util.HotdeployEventUtils;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;

public class ApplicationManagerImpl implements ApplicationManager {
    private Application[] applications_ = new Application[0];

    private Application baseApplication_;

    private ThreadLocal<Application> application_ = new ThreadLocal<Application>();

    public void addApplication(final Application application) {
        applications_ = (Application[]) ArrayUtil.add(applications_,
                application);

        HotdeployEventUtils.add(new AbstractHotdeployEventListener() {
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
        return (Application) application_.get();
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
