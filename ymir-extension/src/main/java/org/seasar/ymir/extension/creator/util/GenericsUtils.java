package org.seasar.ymir.extension.creator.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;

import org.seasar.ymir.extension.creator.util.DescUtils;
import org.seasar.ymir.extension.creator.util.type.Token;
import org.seasar.ymir.extension.creator.util.type.TokenVisitor;
import org.seasar.ymir.extension.creator.util.type.TypeToken;
import org.seasar.ymir.util.ClassUtils;

public class GenericsUtils {
    private static final String SUFFIX_ARRAY = "[]";

    private GenericsUtils() {
    }

    public static String getGenericPropertyTypeName(PropertyDescriptor pd) {
        return getTypeName(getGenericPropertyType(pd));
    }

    public static Type getGenericPropertyType(PropertyDescriptor pd) {
        if (pd == null) {
            return null;
        }

        Method method = pd.getReadMethod();
        if (method != null) {
            return method.getGenericReturnType();
        }
        method = pd.getWriteMethod();
        if (method != null) {
            return method.getGenericParameterTypes()[0];
        }
        return pd.getPropertyType();
    }

    public static String getNonGenericClassName(String typeName) {
        if (typeName == null) {
            return null;
        }

        return new TypeToken(typeName).getBaseName();
    }

    public static String getComponentPropertyTypeName(PropertyDescriptor pd) {
        if (Collection.class.isAssignableFrom(pd.getPropertyType())) {
            return new TypeToken(GenericsUtils.getGenericPropertyTypeName(pd))
                    .getTypes()[0].getBaseName();
        } else {
            return getComponentName(getNonGenericClassName(GenericsUtils
                    .getGenericPropertyTypeName(pd)));
        }
    }

    public static String getComponentName(String typeName) {
        if (typeName == null) {
            return null;
        }
        if (typeName.endsWith(SUFFIX_ARRAY)) {
            return typeName.substring(0, typeName.length()
                    - SUFFIX_ARRAY.length());
        } else {
            return typeName;
        }
    }

    public static String getComponentClassName(String typeName) {
        if (typeName == null) {
            return null;
        }
        TypeToken token = new TypeToken(typeName);
        if (token.isArray()) {
            return token.getComponentName();
        } else {
            try {
                if (Collection.class.isAssignableFrom(ClassUtils.forName(token
                        .getComponentName()))) {
                    return token.getTypes()[0].getBaseName();
                }
            } catch (ClassNotFoundException ignore) {
            }
            return token.getBaseName();
        }
    }

    public static String toString(Type type) {
        if (type == null) {
            return null;
        }
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            StringBuffer sb = new StringBuffer();
            while (clazz.isArray()) {
                clazz = clazz.getComponentType();
                sb.append(SUFFIX_ARRAY);
            }
            return clazz.getName() + sb.toString();
        } else {
            return type.toString();
        }
    }

    public static String getTypeName(Type type) {
        if (type == null) {
            return null;
        }
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            StringBuffer sb = new StringBuffer();
            while (clazz.isArray()) {
                clazz = clazz.getComponentType();
                sb.append(SUFFIX_ARRAY);
            }
            return clazz.getName() + sb.toString();
        } else if (type instanceof TypeVariable<?>) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) type;
            Type[] bounds = typeVariable.getBounds();
            if (bounds.length > 0) {
                return getTypeName(bounds[0]);
            } else {
                return Object.class.getName();
            }
        } else if (type instanceof GenericArrayType) {
            return getTypeName(((GenericArrayType) type)
                    .getGenericComponentType())
                    + SUFFIX_ARRAY;
        } else {
            return type.toString();
        }
    }

    public static Type getUpperBoundType(Type type) {
        if (type == null) {
            return null;
        }
        if (type instanceof Class<?>) {
            return null;
        } else if (type instanceof GenericArrayType) {
            return getUpperBoundType(((GenericArrayType) type)
                    .getGenericComponentType());
        } else if (type instanceof ParameterizedType) {
            return getUpperBoundType(((ParameterizedType) type)
                    .getActualTypeArguments()[0]);
        } else if (type instanceof TypeVariable<?>) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) type;
            Type[] bounds = typeVariable.getBounds();
            if (bounds.length > 0) {
                return bounds[0];
            } else {
                return Object.class;
            }
        } else if (type instanceof WildcardType) {
            return ((WildcardType) type).getUpperBounds()[0];
        } else {
            return null;
        }
    }

    public static String getNormalizedTypeName(String typeName) {
        if (typeName == null) {
            return null;
        }

        TypeToken typeToken = new TypeToken(typeName);
        typeToken.accept(new TokenVisitor<Object>() {
            public Object visit(Token acceptor) {
                String componentName = acceptor.getComponentName();
                StringBuilder sb = new StringBuilder();
                sb.append(ClassUtils.getNormalizedName(componentName));
                if (acceptor.isArray()) {
                    sb.append(SUFFIX_ARRAY);
                }
                acceptor.setBaseName(sb.toString());
                return null;
            }
        });
        return typeToken.getAsString();
    }

    public static String getShortTypeName(String typeName) {
        if (typeName == null) {
            return null;
        }

        TypeToken typeToken = new TypeToken(typeName);
        typeToken.accept(new TokenVisitor<Object>() {
            public Object visit(Token acceptor) {
                String componentName = acceptor.getComponentName();
                StringBuilder sb = new StringBuilder();
                sb.append(ClassUtils.getShortName(componentName));
                if (acceptor.isArray()) {
                    sb.append(SUFFIX_ARRAY);
                }
                acceptor.setBaseName(sb.toString());
                return null;
            }
        });
        return typeToken.getAsString();
    }

    public static String getFullyQualifiedTypeName(String typeName) {
        if (typeName == null) {
            return null;
        }

        TypeToken typeToken = new TypeToken(typeName);
        typeToken.accept(new TokenVisitor<Object>() {
            public Object visit(Token acceptor) {
                Class<?> componentClass = DescUtils.findClass(acceptor
                        .getComponentName());
                if (componentClass != null) {
                    acceptor.setBaseName(componentClass.getName()
                            + (acceptor.isArray() ? SUFFIX_ARRAY : ""));
                }
                return null;
            }
        });
        return typeToken.getAsString();
    }
}
