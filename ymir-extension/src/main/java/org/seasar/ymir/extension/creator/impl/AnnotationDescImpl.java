package org.seasar.ymir.extension.creator.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.util.MethodUtils;

public class AnnotationDescImpl implements AnnotationDesc {
    private String name_;

    private String body_;

    private Set<String> touchedClassNameSet_ = new HashSet<String>();

    public AnnotationDescImpl(Annotation annotation) {
        analyze(annotation);
    }

    public AnnotationDescImpl(String name) {
        this(name, "");
    }

    public AnnotationDescImpl(String name, String body) {
        name_ = name;
        body_ = body;
    }

    void analyze(Annotation annotation) {
        Class<? extends Annotation> type = annotation.annotationType();
        name_ = type.getName();

        Method[] methods = type.getDeclaredMethods();
        Set<Method> methodSet = new TreeSet<Method>(new Comparator<Method>() {
            public int compare(Method o1, Method o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (int i = 0; i < methods.length; i++) {
            String attribute = methods[i].getName();
            if (AnnotationsMetaData.INSTANCE.isDefaultValue(annotation,
                    attribute, MethodUtils.invoke(methods[i], annotation))) {
                continue;
            }
            methodSet.add(methods[i]);
        }

        StringBuilder sb = new StringBuilder();
        if (!methodSet.isEmpty()) {
            boolean simple = methodSet.size() == 1
                    && "value".equals(methodSet.iterator().next().getName());

            sb.append("(");
            String delim = "";
            for (Method method : methodSet) {
                sb.append(delim);
                delim = ", ";
                append(sb, method.getName(), method.getReturnType(),
                        MethodUtils.invoke(method, annotation), simple);
            }
            sb.append(")");
        }
        body_ = sb.toString();
    }

    void append(StringBuilder sb, String attributeName, Class<?> type,
            Object value, boolean omitAttributeName) {
        if (!omitAttributeName) {
            sb.append(attributeName).append(" = ");
        }
        if (type.isArray()) {
            Class<?> componentType = type.getComponentType();
            int length = Array.getLength(value);
            if (length == 1) {
                append(sb, componentType, Array.get(value, 0));
            } else {
                sb.append("{");
                String delim = "";
                for (int i = 0; i < length; i++) {
                    sb.append(delim);
                    delim = ", ";
                    append(sb, componentType, Array.get(value, i));
                }
                sb.append("}");
            }
        } else {
            append(sb, type, value);
        }
    }

    void append(StringBuilder sb, Class<?> type, Object value) {
        if (type == Class.class) {
            sb.append(((Class<?>) value).getName()).append(".class");
        } else if (Annotation.class.isAssignableFrom(type)) {
            sb.append(new AnnotationDescImpl((Annotation) value).getAsString());
        } else if (type == String.class) {
            append(sb, (String) value);
        } else if (type == Character.TYPE) {
            append(sb, ((Character) value).charValue());
        } else if (Enum.class.isAssignableFrom(type)) {
            sb.append(type.getName()).append(".").append(value);
        } else {
            sb.append(value);
        }
    }

    StringBuilder append(StringBuilder sb, char ch) {
        appendQuoted(sb.append('\''), ch).append('\'');
        return sb;
    }

    StringBuilder appendQuoted(StringBuilder sb, char ch) {
        if (ch == '"' || ch == '\'' || ch == '\\') {
            sb.append("\\").append(ch);
        } else if (ch == '\b') {
            sb.append("\\b");
        } else if (ch == '\t') {
            sb.append("\\t");
        } else if (ch == '\n') {
            sb.append("\\n");
        } else if (ch == '\f') {
            sb.append("\\f");
        } else if (ch == '\r') {
            sb.append("\\r");
        } else if (ch == '\0') {
            sb.append("\\0");
        } else if (ch < 32) {
            addPadding(sb.append("\\u"), Integer.toString(ch, 16), 4);
        } else {
            sb.append(ch);
        }
        return sb;
    }

    StringBuilder append(StringBuilder sb, String string) {
        sb.append('"');
        for (int i = 0; i < string.length(); i++) {
            appendQuoted(sb, string.charAt(i));
        }
        sb.append('"');
        return sb;
    }

    StringBuilder addPadding(StringBuilder sb, String string, int length) {
        int n = length - string.length();
        for (int i = 0; i < n; i++) {
            sb.append('0');
        }
        sb.append(string);
        return sb;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String toString() {
        return getAsString();
    }

    public String getAsString() {
        return "@" + name_ + body_;
    }

    public String getAsShortString() {
        touchedClassNameSet_.add(name_);
        return "@" + ClassUtils.getShortName(name_) + body_;
    }

    public String getName() {
        return name_;
    }

    public String getBody() {
        return body_;
    }

    public String getShortBody() {
        return getBody();
    }

    public void setBody(String body) {
        body_ = body;
    }

    public void addDependingClassNamesTo(Set<String> set) {
        set.add(name_);
    }

    public void setTouchedClassNameSet(Set<String> set) {
        touchedClassNameSet_ = set;
    }
}
