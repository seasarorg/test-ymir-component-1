package org.seasar.ymir.hotdeploy.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.hotdeploy.fitter.HotdeployFitter;
import org.seasar.ymir.util.HotdeployUtils;

public class HotdeployManagerImpl implements HotdeployManager {
    private HotdeployFitter<?>[] hotdeployFitters_ = new HotdeployFitter<?>[0];

    private Map<Class<?>, HotdeployFitter<?>> hotdeployFitterMap_ = new HashMap<Class<?>, HotdeployFitter<?>>();

    @Binding(bindingType = BindingType.MAY)
    public void setHotdeployFitters(HotdeployFitter<?>[] hotdeployFitters) {
        hotdeployFitters_ = hotdeployFitters;
        for (int i = 0; i < hotdeployFitters.length; i++) {
            hotdeployFitterMap_.put(hotdeployFitters[i].getTargetClass(),
                    hotdeployFitters[i]);
        }
    }

    public HotdeployFitter<?>[] getHotdeployFitters() {
        return hotdeployFitters_;
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
    @SuppressWarnings("unchecked")
    public Object fit(Object value) {
        if (value == null) {
            return value;
        }

        Class<?> sourceClass = value.getClass();
        HotdeployFitter<?> fitter = findFitter(sourceClass);
        if (fitter != null) {
            return ((HotdeployFitter<Object>) fitter).copy(value);
        }

        Class<?> destinationClass = HotdeployUtils.getContextClass(sourceClass);
        if (!HotdeployUtils.isHotdeployClass(sourceClass)) {
            return value;
        }

        if (destinationClass == sourceClass) {
            return value;
        }

        Object destination;
        try {
            if (destinationClass.isArray()) {
                destination = Array.newInstance(destinationClass
                        .getComponentType(), Array.getLength(value));
            } else {
                destination = destinationClass.newInstance();
            }
        } catch (InstantiationException ex) {
            throw new RuntimeException("Can't instanciate an object of class: "
                    + destinationClass, ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Can't instanciate an object of class: "
                    + destinationClass, ex);
        }

        copy(value, destination);

        return destination;
    }

    HotdeployFitter<?> findFitter(Class<?> clazz) {
        HotdeployFitter<?> fitter = hotdeployFitterMap_.get(clazz);
        if (fitter != null) {
            return fitter;
        }

        for (int i = 0; i < hotdeployFitters_.length; i++) {
            if (hotdeployFitters_[i].getTargetClass().isAssignableFrom(clazz)) {
                return hotdeployFitters_[i];
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    void copy(Object source, Object destination) {
        if (source == null || destination == null) {
            return;
        }

        Class<?> destinationClass = destination.getClass();
        if (destinationClass.isArray()) {
            copyArray(source, destination);
        } else {
            Field[] sourceFields = getFields(source.getClass());
            Map<String, Field> sourceFieldMap = new HashMap<String, Field>();
            for (int i = 0; i < sourceFields.length; i++) {
                sourceFieldMap.put(sourceFields[i].getName(), sourceFields[i]);
            }

            Field[] destinationFields = getFields(destinationClass);
            for (int i = 0; i < destinationFields.length; i++) {
                Field sourceField = sourceFieldMap.get(destinationFields[i]
                        .getName());
                if (sourceField != null) {
                    sourceField.setAccessible(true);
                    destinationFields[i].setAccessible(true);
                    try {
                        destinationFields[i].set(destination, fit(sourceField
                                .get(source)));
                    } catch (IllegalArgumentException ex) {
                        throw new RuntimeException("May logic error", ex);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException("Can't happen!", ex);
                    }
                }
            }
        }
    }

    void copyArray(Object source, Object destination) {
        int length = Array.getLength(destination);
        for (int i = 0; i < length; i++) {
            Array.set(destination, i, fit(Array.get(source, i)));
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
}
