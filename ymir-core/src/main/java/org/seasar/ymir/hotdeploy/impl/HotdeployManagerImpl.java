package org.seasar.ymir.hotdeploy.impl;

import java.beans.Introspector;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.seasar.cms.pluggable.util.HotdeployEventUtils;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.hotdeploy.HotdeployEventListener;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.hotdeploy.fitter.HotdeployFitter;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.util.ContainerUtils;

public class HotdeployManagerImpl implements HotdeployManager {
    private ApplicationManager applicationManager_;

    private HotdeployFitter<?>[] hotdeployFitters_ = new HotdeployFitter<?>[0];

    private HotdeployEventListener[] hotdeployEventListeners_ = new HotdeployEventListener[0];

    private ThreadLocal<Integer> fitDepth_ = new ThreadLocal<Integer>();

    private ThreadLocal<Map<Object, Object>> fittedMap_ = new ThreadLocal<Map<Object, Object>>();

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setHotdeployFitters(HotdeployFitter<?>[] hotdeployFitters) {
        hotdeployFitters_ = hotdeployFitters;
    }

    @InitMethod
    public void init() {
        addEventListener(new AbstractHotdeployEventListener() {
            @Override
            public void stop() {
                // アプリケーションコードやフレームワークコードからstaticメソッド呼び出しをしている場合もあると思われるので
                // クリアするようなイベントリスナを登録しておく。
                PropertyUtils.clearDescriptors();

                Introspector.flushCaches();
            }
        });
    }

    /**
     * 指定されたオブジェクトを現在のコンテキストクラスローダからロードされるクラスと整合性が取れるように変換します。
     * <p>オブジェクトは以下のルールで変換されます。</p>
     * <ul>
     * <li>nullは変換されません。</li>
     * <li>HotdeployClassLoader由来のクラスは現在のコンテキストクラスローダからロードした
     * 同名のクラスのオブジェクトに変換されます。
     * ただしMapまたはCollectionインタフェースを実装している場合は、Hotdeploy由来かどうかに関わらず変換されます。</li>
     * <li>配列やMapやCollectionの場合は中身の要素も上記ルールに従って変換されます。</li>
     * <p>変換はオブジェクトの持つ各フィールドについて再帰的に行なわれますが、
     * 変換されなかったフィールドについてはそれ以上再帰的な変換を行いません。</p>
     * 
     * @param value 変換したいオブジェクト。nullでも構いません。nullでない場合、オブジェクトのクラスは
     * デフォルトコンストラクタを持つ必要があります。
     * @return 変換結果のオブジェクト。
     */
    public Object fit(Object value) {
        enterFit();
        try {
            return fit0(value, false);
        } finally {
            leaveFit();
        }
    }

    private void enterFit() {
        int depth;
        Integer depthObj = fitDepth_.get();
        if (depthObj == null) {
            depth = 0;
        } else {
            depth = depthObj.intValue();
        }

        if (depth == 0) {
            fittedMap_.set(new IdentityHashMap<Object, Object>());
        }
        fitDepth_.set(++depth);
    }

    private void leaveFit() {
        int depth = fitDepth_.get().intValue() - 1;
        if (depth == 0) {
            fittedMap_.set(null);
            fitDepth_.set(null);
        } else {
            fitDepth_.set(depth);
        }
    }

    @SuppressWarnings("unchecked")
    private Object fit0(Object value, boolean replace) {
        if (value == null) {
            return value;
        }

        Class<?> sourceClass = value.getClass();
        if (isImmutable(sourceClass)) {
            return value;
        }

        Map<Object, Object> fittedMap = fittedMap_.get();
        if (fittedMap.containsKey(value)) {
            return fittedMap.get(value);
        }

        Class<?> destinationClass = getContextClass(sourceClass);
        if (sourceClass != destinationClass) {
            Object destination;

            if (sourceClass.isArray()) {
                destination = Array.newInstance(destinationClass
                        .getComponentType(), Array.getLength(value));
            } else {
                try {
                    destination = destinationClass.newInstance();
                } catch (InstantiationException ex) {
                    throw new RuntimeException(
                            "Can't instanciate an object of class: "
                                    + destinationClass, ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(
                            "Can't instanciate an object of class: "
                                    + destinationClass, ex);
                }
            }
            fittedMap.put(value, destination);

            if (sourceClass.isArray()) {
                int length = Array.getLength(value);
                for (int i = 0; i < length; i++) {
                    Array.set(destination, i, fit(Array.get(value, i)));
                }
            } else {
                fitContent(value, destination);
            }

            return destination;
        } else {
            fittedMap.put(value, value);

            if (sourceClass.isArray()) {
                int length = Array.getLength(value);
                for (int i = 0; i < length; i++) {
                    Array.set(value, i, fit(Array.get(value, i)));
                }
                return value;
            } else {
                HotdeployFitter<?> fitter = getHotdeployFitterBag().findFitter(
                        destinationClass);
                if (fitter != null) {
                    ((HotdeployFitter<Object>) fitter).fitContent(value);
                } else {
                    fitContent(value);
                }

                return value;
            }
        }
    }

    private boolean isImmutable(Class<?> clazz) {
        return (clazz.isPrimitive() || ClassUtils.isWrapper(clazz)
                || clazz == String.class || clazz == BigInteger.class || clazz == BigDecimal.class);
    }

    HotdeployFitterBag getHotdeployFitterBag() {
        // synchronizedしていないのは、たまたま同時に呼ばれて2回HotdeployFitterBagが生成されてしまっても実害がないから。

        Application application = applicationManager_.findContextApplication();
        HotdeployFitterBag fitterBag = application
                .getRelatedObject(HotdeployFitterBag.class);
        if (fitterBag == null) {
            fitterBag = new HotdeployFitterBag(ContainerUtils.merge(
                    hotdeployFitters_, (HotdeployFitter[]) ContainerUtils
                            .findAllComponents(application.getS2Container(),
                                    HotdeployFitter.class)));
            application.setRelatedObject(HotdeployFitterBag.class, fitterBag);
        }
        return fitterBag;
    }

    void fitContent(Object source, Object destination) {
        if (source == null || destination == null) {
            return;
        }

        Class<?> destinationClass = destination.getClass();
        Map<String, Field> sourceFieldMap = new HashMap<String, Field>();
        for (Field field : getFields(source.getClass())) {
            sourceFieldMap.put(field.getName(), field);
        }

        for (Field destinationField : getFields(destinationClass)) {
            Field sourceField = sourceFieldMap.get(destinationField.getName());
            if (sourceField != null) {
                sourceField.setAccessible(true);
                destinationField.setAccessible(true);
                try {
                    if (Modifier.isFinal(destinationField.getModifiers())) {
                        fitContent(sourceField.get(source), destinationField
                                .get(destination));
                    } else {
                        Object fitted = fit(sourceField.get(source));
                        if (fitted == null
                                || destinationField.getType().isAssignableFrom(
                                        fitted.getClass())) {
                            destinationField.set(destination, fitted);
                        }
                    }
                } catch (IllegalArgumentException ex) {
                    throw new RuntimeException("May logic error", ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException("Can't happen!", ex);
                }
            }
        }
    }

    void fitContent(Object object) {
        for (Field field : getFields(object.getClass())) {
            field.setAccessible(true);
            try {
                if (Modifier.isFinal(field.getModifiers())) {
                    fitContent(field.get(object));
                } else {
                    Object fitted = fit(field.get(object));
                    if (fitted == null
                            || field.getType().isAssignableFrom(
                                    fitted.getClass())) {
                        // フィールドの型がHotdeploy対象型の場合はassignableであることがある。
                        field.set(object, fitted);
                    }
                }
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException("May logic error", ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Can't happen!", ex);
            }
        }
    }

    Field[] getFields(Class<?> clazz) {
        List<Field> list = new ArrayList<Field>();
        while (clazz != Object.class) {
            list.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return list.toArray(new Field[0]);
    }

    Class<?> getContextClass(Class<?> clazz) {
        ClassLoader contextClassLoader = Thread.currentThread()
                .getContextClassLoader();
        try {
            return contextClassLoader.loadClass(clazz.getName());
        } catch (ClassNotFoundException ex) {
            return clazz;
        }
    }

    static class HotdeployFitterBag {
        private HotdeployFitter<?>[] fitters_;

        private Map<Class<?>, HotdeployFitter<?>> fitterMap_;

        public HotdeployFitterBag(HotdeployFitter<?>[] fitters) {
            fitters_ = fitters;
            fitterMap_ = new HashMap<Class<?>, HotdeployFitter<?>>();
            for (int i = 0; i < fitters.length; i++) {
                fitterMap_.put(fitters[i].getTargetClass(), fitters[i]);
            }
        }

        HotdeployFitter<?> findFitter(Class<?> clazz) {
            HotdeployFitter<?> fitter = fitterMap_.get(clazz);
            if (fitter != null) {
                return fitter;
            }

            for (int i = 0; i < fitters_.length; i++) {
                if (fitters_[i].getTargetClass().isAssignableFrom(clazz)) {
                    return fitters_[i];
                }
            }

            return null;
        }

        public HotdeployFitter<?>[] getFitters() {
            return fitters_;
        }
    }

    public void addEventListener(HotdeployEventListener listener) {
        hotdeployEventListeners_ = (HotdeployEventListener[]) ArrayUtil.add(
                hotdeployEventListeners_, listener);
        HotdeployEventUtils.add(new S2HotdeployEventListenerAdapter(listener));
    }

    public HotdeployEventListener[] getEventListeners() {
        return hotdeployEventListeners_;
    }
}
