package org.seasar.ymir.util;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class ClassUtils {
    private ClassUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstanceFromAbstractClass(Class<T> clazz) {
        ClassPool cp = ClassPool.getDefault();
        CtClass ctClass;
        try {
            ctClass = cp.get(clazz.getName());
        } catch (NotFoundException ex) {
            throw new RuntimeException("Can't happen!", ex);
        }

        CtClass cc = cp.makeClass(clazz.getName() + "$$"
                + new Object().hashCode() + "$$", ctClass);
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
}
