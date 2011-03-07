package org.seasar.ymir.scaffold.maintenance.web;

import java.util.Collection;
import java.util.LinkedList;

import org.seasar.ymir.annotation.DefaultReturn;
import org.seasar.ymir.annotation.SuppressUpdating;
import org.seasar.ymir.scaffold.PageBean;
import org.seasar.ymir.scaffold.maintenance.Globals;
import org.seasar.ymir.scaffold.web.ScaffoldPageBase;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.scope.impl.SessionScope;

@SuppressUpdating
@DefaultReturn("/WEB-INF/zpt/scaffold/maintenance/list.html")
public class YsMaintenanceListPage extends ScaffoldPageBase implements Globals {
    public void _get() {
    }

    public void _prerender() {
    }

    @Out(name = Globals.ATTR_RETURNPATHLIST, scopeClass = SessionScope.class)
    public LinkedList<String> getReturnPathList() {
        return null;
    }

    public Collection<PageBean> getEntityLinks() {
        return siteManager.getMaintenancePages();
    }
}
