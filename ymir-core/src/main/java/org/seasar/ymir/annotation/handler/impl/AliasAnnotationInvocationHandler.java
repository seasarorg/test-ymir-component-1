package org.seasar.ymir.annotation.handler.impl;

import static org.seasar.ymir.annotation.handler.AnnotationElements.getPropertyValue;
import static org.seasar.ymir.annotation.handler.impl.AliasAnnotationElement.PROP_ALIAS;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.annotation.ElementAlias;

public class AliasAnnotationInvocationHandler implements InvocationHandler {
    private static final String PROP_ANNOTATIONTYPE = "annotationType";

    private Annotation aliasAnnotation_;

    private Annotation originalAnnotation_;

    private Map<String, String> elementRealNameToAliasMap_ = new HashMap<String, String>();

    public AliasAnnotationInvocationHandler(Annotation aliasAnnotation,
            Annotation originalAnnotation) {
        aliasAnnotation_ = aliasAnnotation;
        originalAnnotation_ = originalAnnotation;

        for (Method method : aliasAnnotation_.annotationType().getMethods()) {
            String name = method.getName();
            ElementAlias elementAlias = method
                    .getAnnotation(ElementAlias.class);
            if (elementAlias != null) {
                name = elementAlias.value();

                if (method.getName().equals(PROP_ALIAS)) {
                    throw new IllegalClientCodeRuntimeException(
                            "@ElementAlias annotation cannot annotate '"
                                    + PROP_ALIAS
                                    + "' element: "
                                    + aliasAnnotation.annotationType()
                                            .getName());
                }
            }

            if (elementRealNameToAliasMap_.put(name, method.getName()) != null) {
                throw new IllegalClientCodeRuntimeException(
                        "Duplicate name (for element '"
                                + elementAlias.value()
                                + "') specified. Please confirm @ElementAlias annotations: "
                                + aliasAnnotation_.annotationType().getName());
            }
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(originalAnnotation_, args);
        }
        String name = method.getName();
        if (name.equals(PROP_ANNOTATIONTYPE)) {
            return originalAnnotation_.annotationType();
        } else if (name.equals(PROP_ALIAS)) {
            return getPropertyValue(originalAnnotation_, name);
        }
        String alias = elementRealNameToAliasMap_.get(name);
        if (alias != null) {
            return getPropertyValue(aliasAnnotation_, alias);
        } else {
            return getPropertyValue(originalAnnotation_, name);
        }
    }
}