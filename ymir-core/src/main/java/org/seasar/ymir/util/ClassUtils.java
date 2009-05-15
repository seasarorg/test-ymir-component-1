package org.seasar.ymir.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.ymir.Application;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.convention.YmirNamingConvention;

public class ClassUtils {
    private static final String[] EMPTY_STRINGS = new String[0];

    private static final String DASH = "...";

    private static final String PACKAGEPREFIX_JAVA_LANG = "java.lang.";

    private static final String SUFFIX_ARRAY = "[]";

    private static ClassPool cp_;

    private static Map<String, Class<?>> primitiveClassByNameMap_;

    private static final Map<Class<?>, Class<?>> primitiveByWrapperMap_;

    private static final Map<Class<?>, Class<?>> wrapperByPrimitiveMap_;

    private static final Map<String, Object> defaultValueMap_;

    static {
        cp_ = new ClassPool(null);
        cp_.appendSystemPath();

        HashMap<String, Class<?>> primitiveClassByNameMap = new HashMap<String, Class<?>>();
        primitiveClassByNameMap.put("boolean", Boolean.TYPE);
        primitiveClassByNameMap.put("byte", Byte.TYPE);
        primitiveClassByNameMap.put("char", Character.TYPE);
        primitiveClassByNameMap.put("double", Double.TYPE);
        primitiveClassByNameMap.put("float", Float.TYPE);
        primitiveClassByNameMap.put("int", Integer.TYPE);
        primitiveClassByNameMap.put("long", Long.TYPE);
        primitiveClassByNameMap.put("short", Short.TYPE);
        primitiveClassByNameMap_ = Collections
                .unmodifiableMap(primitiveClassByNameMap);

        Map<Class<?>, Class<?>> primitiveByWrapperMap = new HashMap<Class<?>, Class<?>>();
        primitiveByWrapperMap.put(Boolean.class, Boolean.TYPE);
        primitiveByWrapperMap.put(Byte.class, Byte.TYPE);
        primitiveByWrapperMap.put(Character.class, Character.TYPE);
        primitiveByWrapperMap.put(Double.class, Double.TYPE);
        primitiveByWrapperMap.put(Float.class, Float.TYPE);
        primitiveByWrapperMap.put(Integer.class, Integer.TYPE);
        primitiveByWrapperMap.put(Long.class, Long.TYPE);
        primitiveByWrapperMap.put(Short.class, Short.TYPE);
        primitiveByWrapperMap_ = Collections
                .unmodifiableMap(primitiveByWrapperMap);

        Map<Class<?>, Class<?>> wrapperByPrimitiveMap = new HashMap<Class<?>, Class<?>>();
        wrapperByPrimitiveMap.put(Boolean.TYPE, Boolean.class);
        wrapperByPrimitiveMap.put(Byte.TYPE, Byte.class);
        wrapperByPrimitiveMap.put(Character.TYPE, Character.class);
        wrapperByPrimitiveMap.put(Double.TYPE, Double.class);
        wrapperByPrimitiveMap.put(Float.TYPE, Float.class);
        wrapperByPrimitiveMap.put(Integer.TYPE, Integer.class);
        wrapperByPrimitiveMap.put(Long.TYPE, Long.class);
        wrapperByPrimitiveMap.put(Short.TYPE, Short.class);
        wrapperByPrimitiveMap_ = Collections
                .unmodifiableMap(wrapperByPrimitiveMap);

        HashMap<String, Object> defaultValueMap = new HashMap<String, Object>();
        defaultValueMap.put("boolean", Boolean.valueOf(false));
        defaultValueMap.put("byte", Byte.valueOf((byte) 0));
        defaultValueMap.put("char", Character.valueOf('\0'));
        defaultValueMap.put("double", Double.valueOf(0));
        defaultValueMap.put("float", Float.valueOf(0));
        defaultValueMap.put("int", Integer.valueOf(0));
        defaultValueMap.put("long", Long.valueOf(0));
        defaultValueMap.put("short", Short.valueOf((short) 0));
        defaultValueMap_ = Collections.unmodifiableMap(defaultValueMap);
    }

    private ClassUtils() {
    }

    /**
     * @since 1.0.2
     */
    public static <T> T newInstance(Class<T> clazz) {
        if (Modifier.isAbstract(clazz.getModifiers())) {
            return newInstanceFromAbstractClass(clazz);
        } else {
            try {
                return clazz.newInstance();
            } catch (InstantiationException ex) {
                throw new RuntimeException("Can't instantiate: "
                        + clazz.getName(), ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Can't access to constructor: "
                        + clazz.getName(), ex);
            }
        }
    }

    public static <T> T newInstanceFromAbstractClass(Class<T> clazz) {
        CtClass cc;
        synchronized (cp_) {
            CtClass ctClass;
            try {
                ctClass = cp_.get(clazz.getName());
            } catch (NotFoundException ex) {
                // Maven2からテストするとなぜかNotFoundになってしまうので…。
                cp_.appendClassPath(new ClassClassPath(clazz));
                try {
                    ctClass = cp_.get(clazz.getName());
                } catch (NotFoundException ex1) {
                    throw new RuntimeException("Can't happen!", ex);
                }
            }
            cc = cp_.makeClass(clazz.getName() + "$$" + new Object().hashCode()
                    + "$$", ctClass);
        }

        try {
            @SuppressWarnings("unchecked")
            T instance = (T) cc.toClass().newInstance();
            return instance;
        } catch (InstantiationException ex) {
            throw new RuntimeException("Can't instantiate: " + clazz.getName(),
                    ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Can't access to constructor: "
                    + clazz.getName(), ex);
        } catch (CannotCompileException ex) {
            throw new RuntimeException("Can't compile enhanced class of: "
                    + clazz.getName(), ex);
        }
    }

    public static Method[] getMethods(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        List<Method> methodList = new ArrayList<Method>();
        for (int i = 0; i < methods.length; i++) {
            if (!methods[i].isBridge() && !methods[i].isSynthetic()) {
                methodList.add(methods[i]);
            }
        }
        return methodList.toArray(new Method[0]);
    }

    public static Method[] getMethods(Object object, String methodName) {
        return getMethods(object.getClass(), methodName);
    }

    public static Method getMethod(Object object, String methodName,
            Class<?>... parameterTypes) {
        return getMethod(object.getClass(), methodName, parameterTypes);
    }

    public static Method[] getMethods(Class<?> clazz, String methodName) {
        Method[] methods = getMethods(clazz);
        List<Method> list = new ArrayList<Method>();
        for (int i = 0; i < methods.length; i++) {
            if (methodName.equals(methods[i].getName())) {
                list.add(methods[i]);
            }
        }
        return list.toArray(new Method[0]);
    }

    public static Method getMethod(Class<?> clazz, String methodName,
            Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(methodName, parameterTypes);
        } catch (SecurityException ex) {
            return null;
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    public static Class<?> toComponentType(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        if (clazz.isArray()) {
            return clazz.getComponentType();
        } else {
            return clazz;
        }
    }

    /**
     * @since 1.0.1
     */
    public static String getShortName(Object obj) {
        if (obj == null) {
            return null;
        }
        return getShortName(obj.getClass());
    }

    /**
     * @since 1.0.1
     */
    public static String getShortName(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        return getShortName(clazz.getName());
    }

    public static String getShortName(String className) {
        if (className == null) {
            return null;
        }
        int dot = className.lastIndexOf('.');
        if (dot >= 0) {
            return className.substring(dot + 1);
        } else {
            return className;
        }
    }

    /**
     * @since 1.0.1
     */
    public static String getShorterName(Object obj) {
        if (obj == null) {
            return null;
        }
        return getShorterName(obj.getClass());
    }

    /**
     * @since 1.0.1
     */
    public static String getShorterName(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        return getShorterName(clazz.getName());
    }

    /**
     * @since 1.0.1
     */
    public static String getShorterName(String className) {
        if (className == null) {
            return null;
        }

        String shorterName;
        int dot = className.lastIndexOf('.');
        if (dot >= 0) {
            shorterName = className.substring(dot + 1);
        } else {
            shorterName = className;
        }

        for (int i = 0; i < shorterName.length(); i++) {
            char ch = shorterName.charAt(i);
            if (!(ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch >= 'A'
                    && ch <= 'Z' || ch == '_')) {
                return shorterName.substring(0, i);
            }
        }

        return shorterName;
    }

    static String[] getRootPackageNames() {
        Application application = YmirContext.getYmir().getApplication();
        if (application == null) {
            return EMPTY_STRINGS;
        }

        YmirNamingConvention namingConvention;
        try {
            namingConvention = (YmirNamingConvention) application
                    .getS2Container().getComponent(YmirNamingConvention.class);
        } catch (ComponentNotFoundRuntimeException ex) {
            return EMPTY_STRINGS;
        }
        return namingConvention.getRootPackageNames();
    }

    /**
     * @since 1.0.3
     */
    public static String getPrettyName(Object obj) {
        if (obj == null) {
            return null;
        }
        return getPrettyName(obj.getClass());
    }

    /**
     * @since 1.0.3
     */
    public static String getPrettyName(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        return getPrettyName(clazz.getName());
    }

    /**
     * @since 1.0.3
     */
    public static String getPrettyName(String className) {
        if (className == null) {
            return null;
        }

        String prettyName = null;
        for (String rootPackageName : getRootPackageNames()) {
            if (className.startsWith(rootPackageName + ".")) {
                prettyName = DASH
                        + className
                                .substring(rootPackageName.length() + 1/*= ".".length() */);
                break;
            }
        }
        if (prettyName == null) {
            int dot = className.lastIndexOf('.');
            if (dot >= 0) {
                prettyName = className.substring(dot + 1);
            } else {
                prettyName = className;
            }
        }

        for (int i = 0; i < prettyName.length(); i++) {
            char ch = prettyName.charAt(i);
            if (!(ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch >= 'A'
                    && ch <= 'Z' || ch == '_' || ch == '.')) {
                return prettyName.substring(0, i);
            }
        }

        return prettyName;
    }

    /**
     * @since 1.0.3
     */
    public static boolean isPrimitive(String className) {
        return wrapperByPrimitiveMap_.containsKey(getPrimitive(className));
    }

    /**
     * @since 1.0.3
     */
    public static Class<?> getPrimitive(String primitiveName) {
        return primitiveClassByNameMap_.get(primitiveName);
    }

    /**
     * @since 1.0.3
     */
    public static Class<?> toWrapper(Class<?> primitiveClass) {
        return wrapperByPrimitiveMap_.get(primitiveClass);
    }

    public static boolean isWrapper(Class<?> clazz) {
        return primitiveByWrapperMap_.containsKey(clazz);
    }

    /**
     * @since 1.0.3
     */
    public static boolean isWrapper(String className) {
        try {
            return primitiveByWrapperMap_
                    .containsKey(className != null ? ClassUtils
                            .forName(className) : null);
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    /**
     * @since 1.0.3
     */
    public static Class<?> forName(String className)
            throws ClassNotFoundException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ClassUtils.class.getClassLoader();
        }
        return cl.loadClass(className);
    }

    public static Class<?> toPrimitive(Class<?> wrapperClass) {
        return primitiveByWrapperMap_.get(wrapperClass);
    }

    /**
     * @since 1.0.3
     */
    public static Object getDefaultValue(String className) {
        return defaultValueMap_.get(className);
    }

    /**
     * @since 1.0.3
     */
    public static String getPackageName(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        return getPackageName(clazz.getName());
    }

    /**
     * @since 1.0.3
     */
    public static String getPackageName(String className) {
        if (className == null) {
            return null;
        }
        int dot = className.lastIndexOf('.');
        if (dot < 0) {
            return "";
        } else {
            return className.substring(0, dot);
        }
    }

    /**
     * @since 1.0.3
     */
    public static boolean isJavaLang(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        return isJavaLang(clazz.getName());
    }

    /**
     * @since 1.0.3
     */
    public static boolean isJavaLang(String className) {
        if (className == null) {
            return false;
        }

        return className.startsWith(PACKAGEPREFIX_JAVA_LANG)
                && className.indexOf('.', PACKAGEPREFIX_JAVA_LANG.length()) < 0;
    }

    /**
     * @since 1.0.3
     */
    public static String getNormalizedName(String className) {
        if (isJavaLang(className)) {
            return className.substring(PACKAGEPREFIX_JAVA_LANG.length());
        } else {
            return className;
        }
    }

    /**
     * @since 1.0.3
     */
    public static boolean isStandard(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        return isStandard(clazz.getName());
    }

    /**
     * @since 1.0.3
     */
    public static boolean isStandard(String className) {
        if (className == null) {
            return false;
        }

        return className.equals(Void.TYPE.getName())
                || ClassUtils.isPrimitive(className)
                || ClassUtils.isJavaLang(className);
    }

    /**
     * 指定された値が指定された型の変数に代入可能かどうかを返します。
     * 
     * @param value 値。nullを指定することもできます。
     * @param type 型。nullを指定してはいけません。
     * @return 指定された値が指定された型の変数に代入可能かどうか。
     * @since 1.0.3
     */
    public static boolean isCapable(Object value, Class<?> type) {
        if (type == Void.TYPE) {
            return false;
        } else if (value == null) {
            return !type.isPrimitive();
        } else if (type.isPrimitive()) {
            Class<?> clazz = value.getClass();
            if (type == Short.TYPE) {
                return clazz == Short.class || clazz == Byte.class;
            } else if (type == Integer.TYPE) {
                return clazz == Integer.class || clazz == Short.class
                        || clazz == Byte.class || clazz == Character.class;
            } else if (type == Long.TYPE) {
                return clazz == Long.class || clazz == Integer.class
                        || clazz == Short.class || clazz == Byte.class
                        || clazz == Character.class;
            } else if (type == Float.TYPE) {
                return clazz == Float.class || clazz == Long.class
                        || clazz == Integer.class || clazz == Short.class
                        || clazz == Byte.class || clazz == Character.class;
            } else if (type == Double.TYPE) {
                return clazz == Double.class || clazz == Float.class
                        || clazz == Long.class || clazz == Integer.class
                        || clazz == Short.class || clazz == Byte.class
                        || clazz == Character.class;
            } else {
                return clazz == toWrapper(type);
            }
        } else {
            return type.isAssignableFrom(value.getClass());
        }
    }

    public static boolean isArray(String className) {
        if (className == null) {
            return false;
        }
        return className.endsWith(SUFFIX_ARRAY);
    }

    public static String getClassName(String componentName, boolean array) {
        if (componentName == null) {
            return null;
        }
        if (array) {
            return componentName + SUFFIX_ARRAY;
        } else {
            return componentName;
        }
    }
}
