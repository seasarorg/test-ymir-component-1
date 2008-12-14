package org.seasar.ymir.converter.impl;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.converter.PropertyHandler;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.converter.TypeConverter;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.hotdeploy.impl.AbstractHotdeployEventListener;

public class TypeConversionManagerImpl implements TypeConversionManager {
    private static final String TRUE_NUMBER = "1";

    private static final String FALSE_NUMBER = "0";

    private HotdeployManager hotdeployManager_;

    private final Map<Class<?>, TypeConverter<?>> typeConverterMap_ = new HashMap<Class<?>, TypeConverter<?>>();

    private final PropertyUtilsBean propertyUtilsBean_;

    private static final Log log_ = LogFactory
            .getLog(TypeConversionManagerImpl.class);

    public TypeConversionManagerImpl() {
        propertyUtilsBean_ = prepare(newPropertyUtilsBean());
    }

    protected PropertyUtilsBean newPropertyUtilsBean() {
        return new PropertyUtilsBean();
    }

    protected PropertyUtilsBean prepare(PropertyUtilsBean propertyUtilsBean) {
        return propertyUtilsBean;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setHotdeployManager(HotdeployManager hotdeployManager) {
        hotdeployManager_ = hotdeployManager;

        hotdeployManager_
                .addEventListener(new AbstractHotdeployEventListener() {
                    @Override
                    public void stop() {
                        propertyUtilsBean_.clearDescriptors();
                    }
                });
    }

    @Binding(value = "@org.seasar.ymir.util.ContainerUtils@findAllComponents(container, @org.seasar.ymir.converter.TypeConverter@class)", bindingType = BindingType.MUST)
    public void setTypeConverters(TypeConverter<?>[] typeConverters) {
        for (int i = 0; i < typeConverters.length; i++) {
            register(typeConverters[i]);
        }
    }

    public void register(TypeConverter<?> typeConverter) {
        Class<?> type = typeConverter.getType();
        typeConverterMap_.put(type, typeConverter);
    }

    public <T> T convert(Object value, Class<T> type) {
        return convert(value, type, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T convert(Object value, Class<T> type, Annotation[] hint) {
        if (hint == null) {
            hint = new Annotation[0];
        }
        if (value == null) {
            return convertStringTo(null, type, hint);
        }

        Class<?> clazz = value.getClass();
        boolean isArray = clazz.isArray();
        boolean typeIsArray = type.isArray();
        Class<?> typeComponentType = typeIsArray ? type.getComponentType()
                : type;
        if (isArray) {
            if (typeIsArray) {
                Object[] converted = (Object[]) Array.newInstance(
                        typeComponentType, Array.getLength(value));
                for (int i = 0; i < converted.length; i++) {
                    converted[i] = convertComponent(Array.get(value, i),
                            typeComponentType, hint);
                }
                return (T) converted;
            } else if (Array.getLength(value) > 0) {
                return (T) convertComponent(Array.get(value, 0),
                        typeComponentType, hint);
            } else {
                return (T) convertComponent(null, typeComponentType, hint);
            }
        } else {
            if (typeIsArray) {
                Object[] converted = (Object[]) Array.newInstance(
                        typeComponentType, 1);
                converted[0] = convertComponent(value, typeComponentType, hint);
                return (T) converted;
            } else {
                return (T) convertComponent(value, typeComponentType, hint);
            }
        }
    }

    @SuppressWarnings("unchecked")
    <T> T convertComponent(Object value, Class<T> componentType,
            Annotation[] hint) {
        if (value == null || value instanceof String) {
            return convertStringTo((String) value, componentType, hint);
        } else if (componentType.isAssignableFrom(value.getClass())) {
            return (T) value;
        } else {
            return convertStringTo(convertToString(value, hint), componentType,
                    hint);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T convertStringTo(String value, Class<T> type,
            Annotation[] hint) {
        TypeConverter<T> converter = getTypeConverter(type);
        if (converter == null) {
            return null;
        } else {
            return converter.convert(value, hint);
        }
    }

    protected String adjust(String value, Class<?> type) {
        if (isNumericType(type)) {
            if ("true".equals(value)) {
                return TRUE_NUMBER;
            } else if ("false".equals(value)) {
                return FALSE_NUMBER;
            }
        }
        return value;
    }

    protected boolean isNumericType(Class<?> type) {
        return Number.class.isAssignableFrom(type) || type == Byte.TYPE
                || type == Short.TYPE || type == Integer.TYPE
                || type == Long.TYPE || type == Float.TYPE
                || type == Double.TYPE;
    }

    @SuppressWarnings("unchecked")
    protected String convertToString(Object value, Annotation[] hint) {
        TypeConverter<Object> converter = getTypeConverter((Class<Object>) value
                .getClass());
        if (converter == null) {
            TypeConverter<String> stringConverter = getTypeConverter(String.class);
            if (stringConverter == null) {
                return value != null ? value.toString() : null;
            } else {
                return stringConverter.convert(value, hint);
            }
        } else {
            return converter.convertToString(value, hint);
        }
    }

    @SuppressWarnings("unchecked")
    <T> TypeConverter<T> getTypeConverter(Class<T> type) {
        return (TypeConverter<T>) typeConverterMap_.get(type);
    }

    public PropertyHandler getPropertyHandler(Object bean, String name) {
        PropertyDescriptor pd = null;
        try {
            pd = propertyUtilsBean_.getPropertyDescriptor(bean, name);
        } catch (IllegalArgumentException ex) {
            if (log_.isDebugEnabled()) {
                log_.debug("Can't get PropertyDescriptor: beanClass="
                        + bean.getClass() + ", name=" + name, ex);
            }
        } catch (IllegalAccessException ex) {
            if (log_.isDebugEnabled()) {
                log_.debug("Can't get PropertyDescriptor: beanClass="
                        + bean.getClass() + ", name=" + name, ex);
            }
        } catch (InvocationTargetException ex) {
            if (log_.isDebugEnabled()) {
                log_.debug("Can't get PropertyDescriptor: beanClass="
                        + bean.getClass() + ", name=" + name, ex);
            }
        } catch (NoSuchMethodException ex) {
            if (log_.isDebugEnabled()) {
                log_.debug("Can't get PropertyDescriptor: beanClass="
                        + bean.getClass() + ", name=" + name, ex);
            }
        }

        if (pd == null) {
            return null;
        } else {
            return new BeanUtilsPropertyHandler(propertyUtilsBean_, pd, bean,
                    name);
        }
    }
}
