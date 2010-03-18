package org.seasar.ymir.scaffold.maintenance.web;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.annotation.DefaultReturn;
import org.seasar.ymir.annotation.SuppressUpdating;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.scaffold.util.ClassScanner;
import org.seasar.ymir.scaffold.util.PageBase;

@SuppressUpdating
@DefaultReturn("/maintenance/list.template.html")
public class MaintenanceListPage extends PageBase {
    @Binding(bindingType = BindingType.MUST)
    protected YmirNamingConvention ymirNamingConvention;

    private List<String> entityNames = new ArrayList<String>();

    public void _get() {
        String webRootPackageName = ymirNamingConvention.getRootPackageNames()[0]
                + ".web";
        int afterDot = webRootPackageName.length() + 1;
        for (String className : new ClassScanner().scanNames(
                webRootPackageName, "[^\\.]+\\.maintenance\\.IndexPage")) {
            entityNames.add(className.substring(afterDot, className.indexOf(
                    '.', afterDot)));
        }
    }

    public List<String> getEntityNames() {
        return entityNames;
    }
}
