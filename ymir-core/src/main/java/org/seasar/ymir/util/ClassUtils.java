package org.seasar.ymir.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.ymir.Application;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.convention.YmirNamingConvention;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class ClassUtils {
    private static final String[] EMPTY_STRINGS = new String[0];

    private static final String DASH = "...";

    private static ClassPool cp_;

    private static Map<Class<?>, Class<?>> primitiveMap_;

    static {
        cp_ = new ClassPool(null);
        cp_.appendSystemPath();

        Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
        map.put(Boolean.class, Boolean.TYPE);
        map.put(Byte.class, Byte.TYPE);
        map.put(Character.class, Character.TYPE);
        map.put(Double.class, Double.TYPE);
        map.put(Float.class, Float.TYPE);
        map.put(Integer.class, Integer.TYPE);
        map.put(Long.class, Long.TYPE);
        map.put(Short.class, Short.TYPE);
        primitiveMap_ = Collections.unmodifiableMap(map);
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

    public static boolean isWrapper(Class<?> clazz) {
        return primitiveMap_.containsKey(clazz);
    }

    public static Class<?> getPrimitive(Class<?> clazz) {
        return primitiveMap_.get(clazz);
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

        String shorterName = null;
        for (String rootPackageName : getRootPackageNames()) {
            if (className.startsWith(rootPackageName + ".")) {
                shorterName = DASH
                        + className
                                .substring(rootPackageName.length() + 1/*= ".".length() */);
                break;
            }
        }
        if (shorterName == null) {
            int dot = className.lastIndexOf('.');
            if (dot >= 0) {
                shorterName = className.substring(dot + 1);
            } else {
                shorterName = className;
            }
        }

        for (int i = 0; i < shorterName.length(); i++) {
            char ch = shorterName.charAt(i);
            if (!(ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch >= 'A'
                    && ch <= 'Z' || ch == '_' || ch == '.')) {
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
}
