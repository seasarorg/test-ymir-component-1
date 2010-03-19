package org.seasar.ymir.scaffold.impl;

import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.cache.CacheManager;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.scaffold.SiteManager;
import org.seasar.ymir.scaffold.auth.web.YsAuthLoginPage;
import org.seasar.ymir.scaffold.util.ClassScanner;

public class SiteManagerImpl implements SiteManager {
    private static final Object NULL = new Object();

    @Binding(bindingType = BindingType.MUST)
    protected Ymir ymir;

    @Binding(bindingType = BindingType.MUST)
    protected YmirNamingConvention ymirNamingConvention;

    private Map<Key, Object> map;

    @Binding(bindingType = BindingType.MUST)
    public void setCacheManager(CacheManager cacheManager) {
        map = cacheManager.newMap();
    }

    private enum Key {
        LOGIN_PAGE_PATH;
    }

    public String getLoginPagePath() {
        String loginPagePath;
        if (contains(Key.LOGIN_PAGE_PATH)) {
            loginPagePath = getValue(Key.LOGIN_PAGE_PATH);
        } else {
            loginPagePath = null;

            for (Class<?> pageClass : new ClassScanner().scan(
                    ymirNamingConvention.getRootPackageNames()[0] + ".web",
                    ".+Page")) {
                if (YsAuthLoginPage.class.isAssignableFrom(pageClass)) {
                    loginPagePath = ymir.getPathOfPageClass(pageClass);
                    break;
                }
            }
            setValue(Key.LOGIN_PAGE_PATH, loginPagePath);
        }
        return loginPagePath;
    }

    private boolean contains(Key key) {
        return map.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    <T> T getValue(Key key) {
        Object value = map.get(key);
        if (value == NULL) {
            return null;
        }
        return (T) value;
    }

    void setValue(Key key, Object value) {
        if (value == null) {
            map.put(key, NULL);
        } else {
            map.put(key, value);
        }
    }

    void removeValue(Key key) {
        map.remove(key);
    }
}
