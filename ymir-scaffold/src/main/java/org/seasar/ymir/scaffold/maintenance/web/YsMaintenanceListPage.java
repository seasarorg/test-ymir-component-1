package org.seasar.ymir.scaffold.maintenance.web;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.annotation.DefaultReturn;
import org.seasar.ymir.annotation.SuppressUpdating;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.scaffold.maintenance.dto.EntityLinkDto;
import org.seasar.ymir.scaffold.util.ClassScanner;
import org.seasar.ymir.scaffold.web.ScaffoldPageBase;

@SuppressUpdating
@DefaultReturn("/WEB-INF/zpt/scaffold/maintenance/list.html")
public class YsMaintenanceListPage extends ScaffoldPageBase {
    private static List<EntityLinkDto> theEntityLinks;

    @Binding(bindingType = BindingType.MUST)
    protected Ymir ymir;

    @Binding(bindingType = BindingType.MUST)
    protected YmirNamingConvention ymirNamingConvention;

    private List<EntityLinkDto> entityLinks;

    public void _get() {
    }

    public void _prerender() {
        entityLinks = prepareEntityLinks();
    }

    protected List<EntityLinkDto> prepareEntityLinks() {
        if (theEntityLinks == null) {
            List<EntityLinkDto> entityLinks = new ArrayList<EntityLinkDto>();
            String webRootPackageName = ymirNamingConvention
                    .getRootPackageNames()[0]
                    + ".web";
            int afterDot = webRootPackageName.length() + 1;
            for (Class<?> pageClass : new ClassScanner().scan(
                    webRootPackageName, "(.*\\.)?IndexPage")) {
                if (YsMaintenancePage.class.isAssignableFrom(pageClass)) {
                    String className = pageClass.getName();
                    entityLinks.add(new EntityLinkDto(className.substring(
                            afterDot, className.indexOf('.', afterDot)), ymir
                            .getPathOfPageClass(pageClass)));
                }
            }
            theEntityLinks = entityLinks;
        }
        return theEntityLinks;
    }

    public List<EntityLinkDto> getEntityLinks() {
        return entityLinks;
    }
}
