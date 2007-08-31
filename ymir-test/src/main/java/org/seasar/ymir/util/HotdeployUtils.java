package org.seasar.ymir.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.seasar.framework.container.hotdeploy.HotdeployClassLoader;
import org.seasar.framework.container.hotdeploy.HotdeployUtil;
import org.seasar.ymir.YmirContext;

public class HotdeployUtils extends HotdeployUtil {
    protected HotdeployUtils() {
    }

    public static Object rebuildValue(Object value) {
        if (YmirContext.isUnderDevelopment()) {
            return rebuildValueInternal(value);
        }
        return value;
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
    @Deprecated
    @SuppressWarnings("unchecked")
    public static Object fit(Object value) {
        if (value == null) {
            return value;
        }

        Class<?> sourceClass = value.getClass();
        Class<?> destinationClass = getContextClass(sourceClass);
        if (!Collection.class.isAssignableFrom(sourceClass)
                && !Map.class.isAssignableFrom(sourceClass)) {
            if (!isHotdeployClass(sourceClass)) {
                return value;
            }

            if (destinationClass == sourceClass) {
                return value;
            }
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

    @SuppressWarnings("unchecked")
    static void copy(Object source, Object destination) {
        if (source == null || destination == null) {
            return;
        }

        Class<?> destinationClass = destination.getClass();
        if (destinationClass.isArray()) {
            copyArray(source, destination);
        } else if (Collection.class.isAssignableFrom(destinationClass)) {
            Collection sourceCollection = (Collection) source;
            Collection destinationCollection = (Collection) destination;
            for (Iterator itr = sourceCollection.iterator(); itr.hasNext();) {
                destinationCollection.add(fit(itr.next()));
            }
        } else if (Map.class.isAssignableFrom(destinationClass)) {
            Map sourceMap = (Map) source;
            Map destinationMap = (Map) destination;
            for (Iterator<Map.Entry> itr = sourceMap.entrySet().iterator(); itr
                    .hasNext();) {
                Map.Entry entry = itr.next();
                destinationMap.put(fit(entry.getKey()), fit(entry.getValue()));
            }
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

    static void copyArray(Object source, Object destination) {
        int length = Array.getLength(destination);
        for (int i = 0; i < length; i++) {
            Array.set(destination, i, fit(Array.get(source, i)));
        }
    }

    static Field[] getFields(Class<?> clazz) {
        List<Field> list = new ArrayList<Field>();
        while (clazz != Object.class) {
            list.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return list.toArray(new Field[0]);
    }

    public static Class getContextClass(Class<?> clazz) {
        ClassLoader contextClassLoader = Thread.currentThread()
                .getContextClassLoader();
        try {
            if (!clazz.isArray()) {
                return contextClassLoader.loadClass(clazz.getName());
            } else {
                return Array.newInstance(
                        contextClassLoader.loadClass(clazz.getComponentType()
                                .getName()), 0).getClass();
            }
        } catch (ClassNotFoundException ex) {
            return clazz;
        }
    }

    public static boolean isHotdeployClass(Class<?> clazz) {
        return clazz.getClassLoader() instanceof HotdeployClassLoader;
    }
}
