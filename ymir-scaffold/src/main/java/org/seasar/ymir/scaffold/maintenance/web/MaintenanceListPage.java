package org.seasar.ymir.scaffold.maintenance.web;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Response;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.scaffold.util.ClassScanner;
import org.seasar.ymir.scaffold.util.Forward;
import org.seasar.ymir.scaffold.util.PageBase;

public class MaintenanceListPage extends PageBase {
    @Binding(bindingType = BindingType.MUST)
    protected YmirNamingConvention ymirNamingConvention;

    private List<String> entityNames = new ArrayList<String>();

    public Response _get() {
        String webRootPackageName = ymirNamingConvention.getRootPackageNames()[0]
                + ".web";
        int afterDot = webRootPackageName.length() + 1;
        for (String className : new ClassScanner().scanNames(
                webRootPackageName, "[^\\.]+\\.maintenance\\.IndexPage")) {
            entityNames.add(className.substring(afterDot, className.indexOf(
                    '.', afterDot)));
        }

        return Forward.to("/maintenance/list.template.html");
    }

    public List<String> getEntityNames() {
        return entityNames;
    }
}
