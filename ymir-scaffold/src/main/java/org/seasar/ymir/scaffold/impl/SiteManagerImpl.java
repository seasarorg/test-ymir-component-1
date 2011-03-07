package org.seasar.ymir.scaffold.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.cache.CacheManager;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.hotdeploy.impl.AbstractHotdeployEventListener;
import org.seasar.ymir.scaffold.PageBean;
import org.seasar.ymir.scaffold.SiteManager;
import org.seasar.ymir.scaffold.auth.web.YsAuthLoginPage;
import org.seasar.ymir.scaffold.maintenance.web.YsMaintenanceListPage;
import org.seasar.ymir.scaffold.maintenance.web.YsMaintenancePage;
import org.seasar.ymir.scaffold.util.ClassScanner;
import org.seasar.ymir.session.SessionManager;

public class SiteManagerImpl implements SiteManager {
    private static final String ATTR_LOGINUSER = "loginUser";

    @Binding(bindingType = BindingType.MUST)
    protected Ymir ymir;

    @Binding(bindingType = BindingType.MUST)
    protected YmirNamingConvention ymirNamingConvention;

    @Binding(bindingType = BindingType.MUST)
    protected SessionManager sessionManager;

    private Map<Class<?>, List<Class<?>>> pageClassesMap;

    private boolean loginPagePathIsSet;

    private String loginPagePath;

    private boolean maintenancePageMapIsSet;

    private Map<String, PageBean> maintenancePageMap;

    private boolean maintenanceListPagePathIsSet;

    private String maintenanceListPagePath;

    @Binding(bindingType = BindingType.MUST)
    public void setCacheManager(CacheManager cacheManager) {
        pageClassesMap = cacheManager.newMap();
        maintenancePageMap = cacheManager.newMap();
    }

    @Binding(bindingType = BindingType.MUST)
    public void setHotdeployManager(HotdeployManager hotdeployManager) {
        hotdeployManager.addEventListener(new AbstractHotdeployEventListener() {
            @Override
            public void stop() {
                clearCache();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T> List<Class<? super T>> getPageClasses(Class<T> assignableClass) {
        List pageClasses = pageClassesMap.get(assignableClass);
        if (pageClasses == null) {
            pageClasses = gatherPageClasses(assignableClass);
            pageClassesMap.put(assignableClass, pageClasses);
        }
        return pageClasses;
    }

    @SuppressWarnings("unchecked")
    <T> List<Class<? super T>> gatherPageClasses(Class<T> assignableClass) {
        List pageClasses = new ArrayList();
        for (Class<?> pageClass : new ClassScanner().scan(
                getWebRootPackageName(), ".+Page")) {
            if (assignableClass.isAssignableFrom(pageClass)) {
                pageClasses.add(pageClass);
            }
        }
        return pageClasses;
    }

    protected String getWebRootPackageName() {
        return ymirNamingConvention.getRootPackageNames()[0] + ".web";
    }

    protected void clearCache() {
        loginPagePathIsSet = false;
        loginPagePath = null;
        maintenanceListPagePathIsSet = false;
        maintenanceListPagePath = null;
        maintenancePageMapIsSet = false;
    }

    public String getLoginPagePath() {
        String loginPagePath = this.loginPagePath;
        if (!loginPagePathIsSet) {
            List<Class<? super YsAuthLoginPage>> pageClasses = getPageClasses(YsAuthLoginPage.class);
            if (pageClasses.isEmpty()) {
                loginPagePath = null;
            } else {
                loginPagePath = ymir.getPathOfPageClass(pageClasses.get(0));
            }
            this.loginPagePath = loginPagePath;
            loginPagePathIsSet = true;
        }
        return loginPagePath;
    }

    public Collection<PageBean> getMaintenancePages() {
        if (!maintenancePageMapIsSet) {
            loadMaintenancePages();
            maintenanceListPagePathIsSet = true;
        }
        return maintenancePageMap.values();
    }

    void loadMaintenancePages() {
        for (Class<? super YsMaintenancePage> pageClass : getPageClasses(YsMaintenancePage.class)) {
            String entityName = getEntityName(pageClass);
            maintenancePageMap.put(entityName, new PageBean(entityName, ymir
                    .getPathOfPageClass(pageClass), pageClass));
        }
    }

    public PageBean getMaintenancePage(String entityName) {
        if (!maintenancePageMapIsSet) {
            loadMaintenancePages();
            maintenanceListPagePathIsSet = true;
        }
        return maintenancePageMap.get(entityName);
    }

    public String getMaintenanceListPagePath() {
        String maintenanceListPagePath = this.maintenanceListPagePath;
        if (!maintenanceListPagePathIsSet) {
            List<Class<? super YsMaintenanceListPage>> pageClasses = getPageClasses(YsMaintenanceListPage.class);
            if (pageClasses.isEmpty()) {
                maintenanceListPagePath = null;
            } else {
                maintenanceListPagePath = ymir.getPathOfPageClass(pageClasses
                        .get(0));
            }
            this.maintenanceListPagePath = maintenanceListPagePath;
            maintenanceListPagePathIsSet = true;
        }
        return maintenanceListPagePath;
    }

    String getEntityName(Class<?> maintenancePageClass) {
        String name = maintenancePageClass.getName();
        int start = getWebRootPackageName().length() + 1;
        return name.substring(start, name.indexOf('.', start));
    }

    public boolean isLogined() {
        return sessionManager.getAttribute(ATTR_LOGINUSER) != null;
    }
}
