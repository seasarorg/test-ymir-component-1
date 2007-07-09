package org.seasar.ymir.interceptor.impl;

import static org.seasar.ymir.RequestProcessor.ATTR_NOTES;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.Notes;
import org.seasar.ymir.Request;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.annotation.SuppressConstraints;
import org.seasar.ymir.annotation.Validator;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;
import org.seasar.ymir.impl.ConstraintBag;
import org.seasar.ymir.impl.MethodInvokerImpl;
import org.seasar.ymir.impl.VoidMethodInvoker;
import org.seasar.ymir.util.MethodUtils;

/**
 * 制約チェックを行なうためのInterceptorです。
 */
public class CheckConstraintInterceptor extends AbstractYmirProcessInterceptor {
    public static final String ACTION_VALIDATIONFAILED = "_validationFailed";

    public static final String ACTION_PERMISSIONDENIED = "_permissionDenied";

    static final Set<ConstraintType> EMPTY_SUPPRESSTYPESET = EnumSet
            .noneOf(ConstraintType.class);

    private Ymir ymir_;

    public void setYmir(Ymir ymir) {
        ymir_ = ymir;
    }

    @Override
    public MethodInvoker actionInvoking(Object component, Method action,
            Request request, MethodInvoker methodInvoker)
            throws PermissionDeniedException {
        try {
            Notes notes = confirmConstraint(component, action, request);
            if (notes != null) {
                request.setAttribute(ATTR_NOTES, notes);

                // バリデーションエラーが発生した場合は、エラー処理メソッドが存在すればそれを呼び出す。
                // メソッドが存在しなければ何もしない（元のアクションメソッドの呼び出しをスキップする）。
                Method method = MethodUtils.getMethod(component,
                        ACTION_VALIDATIONFAILED, new Class[] { Notes.class });
                if (method != null) {
                    methodInvoker = new MethodInvokerImpl(method,
                            new Object[] { notes });
                } else {
                    method = MethodUtils.getMethod(component,
                            ACTION_VALIDATIONFAILED);
                    if (method != null) {
                        methodInvoker = new MethodInvokerImpl(method,
                                new Object[0]);
                    } else {
                        methodInvoker = VoidMethodInvoker.INSTANCE;
                    }
                }
            }
        } catch (PermissionDeniedException ex) {
            // 権限エラーが発生した場合は、エラー処理メソッドが存在すればそれを呼び出す。
            // メソッドが存在しなければPermissionDeniedExceptionを上に再スローする。
            Method method = MethodUtils.getMethod(component,
                    ACTION_PERMISSIONDENIED,
                    new Class[] { PermissionDeniedException.class });
            if (method != null) {
                methodInvoker = new MethodInvokerImpl(method,
                        new Object[] { ex });
            } else {
                method = MethodUtils.getMethod(component,
                        ACTION_PERMISSIONDENIED);
                if (method != null) {
                    methodInvoker = new MethodInvokerImpl(method, new Object[0]);
                } else {
                    throw ex;
                }
            }
        }

        return methodInvoker;
    }

    Notes confirmConstraint(Object component, Method action, Request request)
            throws PermissionDeniedException {

        boolean validationFailed = false;
        Notes notes = new Notes();
        ConstraintBag[] bag = getConstraintBags(component, action);
        for (int i = 0; i < bag.length; i++) {
            try {
                bag[i].confirm(component, request);
            } catch (PermissionDeniedException ex) {
                throw ex;
            } catch (ValidationFailedException ex) {
                validationFailed = true;
                notes.add(ex.getNotes());
            } catch (ConstraintViolatedException ex) {
                throw new RuntimeException("May logic error", ex);
            }
        }

        // Validatorアノテーションがついているメソッドを実行する。
        Method[] validators = gatherValidators(component);
        for (int i = 0; i < validators.length; i++) {
            try {
                Object invoked = validators[i].invoke(component, new Object[0]);
                if (invoked instanceof Notes) {
                    Notes ns = (Notes) invoked;
                    if (!ns.isEmpty()) {
                        validationFailed = true;
                        notes.add(ns);
                    }
                }
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException("May logic error", ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("May logic error", ex);
            } catch (InvocationTargetException ex) {
                Throwable cause = ex.getCause();
                if (cause instanceof ValidationFailedException) {
                    validationFailed = true;
                    notes.add(((ValidationFailedException) cause).getNotes());
                }
            }
        }

        if (validationFailed) {
            return notes;
        } else {
            return null;
        }
    }

    protected ConstraintBag[] getConstraintBags(Object component, Method action) {
        return getConstraints(component.getClass(), action);
    }

    // PropertyDescriptorのreadMethodは対象外。fieldも対象外。
    ConstraintBag[] getConstraints(Class<?> clazz, Method action) {
        List<ConstraintBag> list = new ArrayList<ConstraintBag>();

        Set<ConstraintType> suppressTypeSet = EnumSet
                .noneOf(ConstraintType.class);
        if (action != null) {
            SuppressConstraints suppress = action
                    .getAnnotation(SuppressConstraints.class);
            if (suppress != null) {
                ConstraintType[] types = suppress.value();
                for (int i = 0; i < types.length; i++) {
                    suppressTypeSet.add(types[i]);
                }
            }
        }
        getConstraint(clazz, list, suppressTypeSet);
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        }
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < pds.length; i++) {
            getConstraint(pds[i].getWriteMethod(), list, suppressTypeSet);
        }
        getConstraint(action, list, EMPTY_SUPPRESSTYPESET);

        return list.toArray(new ConstraintBag[0]);
    }

    @SuppressWarnings("unchecked")
    void getConstraint(AnnotatedElement element, List<ConstraintBag> list,
            Set<ConstraintType> suppressTypeSet) {
        if (element == null) {
            return;
        }
        Annotation[] annotations = element.getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            ConstraintAnnotation constraintAnnotation = annotations[i]
                    .annotationType().getAnnotation(ConstraintAnnotation.class);
            if (constraintAnnotation == null
                    || suppressTypeSet.contains(constraintAnnotation.type())) {
                continue;
            }
            list.add(new ConstraintBag(((Constraint) getS2Container()
                    .getComponent(constraintAnnotation.component())),
                    annotations[i], element));
        }
    }

    Method[] gatherValidators(Object component) {
        List<Method> validatorList = new ArrayList<Method>();
        Method[] methods = component.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isAnnotationPresent(Validator.class)) {
                if (methods[i].getParameterTypes().length > 0) {
                    // 引数を持つメソッドにはValidatorアノテーションはつけられない。
                    throw new RuntimeException(
                            "@Validator must be annotated on a method that has no parameters");
                }
                validatorList.add(methods[i]);
            }
        }
        return validatorList.toArray(new Method[0]);
    }

    protected S2Container getS2Container() {
        return ymir_.getApplication().getS2Container();
    }
}
