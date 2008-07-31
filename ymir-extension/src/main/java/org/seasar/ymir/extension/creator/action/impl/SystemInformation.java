package org.seasar.ymir.extension.creator.action.impl;

import java.util.Arrays;

import org.seasar.ymir.Application;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.extension.creator.util.SourceCreatorUtils;

public class SystemInformation {
    private Application application_;

    private YmirNamingConvention namingConvention_;

    private String[] hotDeployEnabledPackageNames_ = new String[0];

    private String[] hotDeployDisabledPackageNames_ = new String[0];

    public SystemInformation(Application application,
            YmirNamingConvention namingConvention) {
        application_ = application;
        namingConvention_ = namingConvention;

        initialize(namingConvention);
    }

    void initialize(YmirNamingConvention namingConvention) {
        if (namingConvention == null) {
            return;
        }

        if (namingConvention.isHotdeployableOnlyPackageForCreator()) {
            hotDeployEnabledPackageNames_ = namingConvention
                    .getTargetPackageNames();
        } else {
            hotDeployEnabledPackageNames_ = namingConvention
                    .getRootPackageNames();
            hotDeployDisabledPackageNames_ = namingConvention
                    .getIgnorePackageNames();
        }

        Arrays.sort(hotDeployEnabledPackageNames_);
        Arrays.sort(hotDeployDisabledPackageNames_);
    }

    public String[] getHotdeployEnabledPackageNames() {
        return hotDeployEnabledPackageNames_;
    }

    public String[] getHotdeployDisabledPackageNames() {
        return hotDeployDisabledPackageNames_;
    }

    public Application getApplication() {
        return application_;
    }

    public boolean isProjectRootDetectedAutomatically() {
        return SourceCreatorUtils.getOriginalProjectRoot(application_) == null;
    }
}
