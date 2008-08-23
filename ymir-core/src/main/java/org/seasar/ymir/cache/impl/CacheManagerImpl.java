package org.seasar.ymir.cache.impl;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.cache.CacheManager;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.hotdeploy.impl.AbstractHotdeployEventListener;

public class CacheManagerImpl implements CacheManager {
    private HotdeployManager hotdeployManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setHotdeployManager(HotdeployManager hotdeployManager) {
        hotdeployManager_ = hotdeployManager;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Map> mapClass_ = ConcurrentHashMap.class;

    private boolean isSynchronized_ = true;

    @Binding(bindingType = BindingType.MAY)
    public void setMapClass(Class<? extends Map<?, ?>> mapClass) {
        mapClass_ = mapClass;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setSynchronized(boolean isSynchronized) {
        isSynchronized_ = isSynchronized;
    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> newMap() {
        Map<K, V> m;
        try {
            m = (Map<K, V>) mapClass_.newInstance();
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

        final Map<K, V> map;
        if (!isSynchronized_) {
            map = Collections.synchronizedMap(m);
        } else {
            map = m;
        }

        hotdeployManager_
                .addEventListener(new AbstractHotdeployEventListener() {
                    @Override
                    public void stop() {
                        map.clear();
                    }
                });

        return map;
    }
}
