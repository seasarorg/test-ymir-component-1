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

    /**
     * 生成するMapのクラスを設定します。
     * <p>このメソッドを呼び出した場合は、
     * {@link #setSynchronized(boolean)}メソッドを呼び出して
     * Mapクラスが同期化されているかどうかを指定して下さい。
     * </p>
     * 
     * @param mapClass Mapクラス。
     * @see #setSynchronized(boolean)
     */
    @Binding(bindingType = BindingType.MAY)
    public void setMapClass(Class<? extends Map<?, ?>> mapClass) {
        mapClass_ = mapClass;
    }

    /**
     * 生成するMapクラスが同期化されているかどうかを設定します。
     * <p>{@link #setMapClass(Class)}メソッドで設定するMapクラスが同期化されている場合は
     * このメソッドでtrueを指定して下さい。
     * 同期化されていないMapクラスを設定した場合は
     * このメソッドでfalseを指定して下さい。
     * </p>
     * 
     * @param isSynchronized Mapクラスが同期化されているかどうか。
     * @see #setMapClass(Class)
     */
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
