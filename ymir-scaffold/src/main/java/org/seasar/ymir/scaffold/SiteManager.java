package org.seasar.ymir.scaffold;

import java.util.Collection;
import java.util.List;

public interface SiteManager {
    String ATTR_LOGINUSER = "loginUser";

    <T> List<Class<? super T>> getPageClasses(Class<T> assignableClass);

    String getLoginPagePath();

    Collection<PageBean> getMaintenancePages();

    String getMaintenanceListPagePath();

    PageBean getMaintenancePage(String entityName);

    boolean isLogined();
}
