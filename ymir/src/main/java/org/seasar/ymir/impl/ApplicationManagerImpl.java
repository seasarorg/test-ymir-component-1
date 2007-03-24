package org.seasar.ymir.impl;

import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.framework.util.ArrayUtil;

public class ApplicationManagerImpl implements ApplicationManager {

    private Application[] applications_ = new Application[0];

    private Application baseApplication_;

    private ThreadLocal<Application> application_ = new ThreadLocal<Application>();

    public void addApplication(Application application) {

        applications_ = (Application[]) ArrayUtil.add(applications_,
                application);
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

    public void setContextApplication(Application application) {

        application_.set(application);
    }

    public void setBaseApplication(Application application) {

        baseApplication_ = application;
    }
}