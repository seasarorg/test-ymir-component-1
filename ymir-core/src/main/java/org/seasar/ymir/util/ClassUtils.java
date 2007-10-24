package org.seasar.ymir.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class ClassUtils {
    private static ClassPool cp_;

    static {
        cp_ = new ClassPool(null);
        cp_.appendSystemPath();
    }

    private ClassUtils() {
    }

    @SuppressWarnings("unchecked")
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
            return (T) cc.toClass().newInstance();
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
            if (!isCovariantOverriddenMethod(methods[i], clazz)) {
                methodList.add(methods[i]);
            }
        }
        return methodList.toArray(new Method[0]);
    }

    static boolean isCovariantOverriddenMethod(Method method, Class<?> clazz) {
        // getDeclaredMedhod()で引いてきたMethodオブジェクトの返り値が同じでなければ
        // 共変戻り値の被オーバライドメソッド。
        // 「対象メソッドが祖先クラスにあり、かつ対象クラスに同じ名前・引数のメソッドがあれば」というルール
        // ではブリッジメソッドをうまく判定できない。

        try {
            Method declaredMethod = clazz.getDeclaredMethod(method.getName(),
                    method.getParameterTypes());
            return (declaredMethod.getReturnType() != method.getReturnType());
        } catch (NoSuchMethodException ex) {
            return false;
        }
    }
}
