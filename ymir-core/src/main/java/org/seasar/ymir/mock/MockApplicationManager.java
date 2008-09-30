package org.seasar.ymir.mock;

import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.mock.MockApplication;

public class MockApplicationManager implements ApplicationManager {
    private Application application_ = new MockApplication();

    public void addApplication(Application application) {
        throw new UnsupportedOperationException();
    }

    public Application findContextApplication() {
        return application_;
    }

    public Application[] getApplications() {
        return new Application[] { application_ };
    }

    public Application getContextApplication() {
        return application_;
    }

    public void setBaseApplication(Application application) {
        application_ = application;
    }

    public void setContextApplication(Application application) {
        application_ = application;
    }
}
