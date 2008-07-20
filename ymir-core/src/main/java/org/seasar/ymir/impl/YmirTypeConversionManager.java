package org.seasar.ymir.impl;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.ClassConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FileConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;
import org.apache.commons.beanutils.converters.URLConverter;
import org.seasar.ymir.converter.impl.DateConverter;
import org.seasar.ymir.converter.impl.StringConverter;

public class YmirTypeConversionManager extends BeanUtilsTypeConversionManager {
    private static final String TRUE_NUMBER = "1";

    private static final String FALSE_NUMBER = "0";

    @Override
    protected ConvertUtilsBean prepare(ConvertUtilsBean convertUtilsBean) {
        convertUtilsBean = super.prepare(convertUtilsBean);

        convertUtilsBean.register(new StringConverter(), String.class);
        convertUtilsBean.register(new BigDecimalConverter(null),
                BigDecimal.class);
        convertUtilsBean.register(new BigIntegerConverter(null),
                BigInteger.class);
        convertUtilsBean.register(new BooleanConverter(null), Boolean.class);
        convertUtilsBean.register(new ByteConverter(null), Byte.class);
        convertUtilsBean
                .register(new CharacterConverter(null), Character.class);
        convertUtilsBean.register(new ClassConverter(null), Class.class);
        convertUtilsBean.register(new DoubleConverter(null), Double.class);
        convertUtilsBean.register(new FloatConverter(null), Float.class);
        convertUtilsBean.register(new IntegerConverter(null), Integer.class);
        convertUtilsBean.register(new LongConverter(null), Long.class);
        convertUtilsBean.register(new ShortConverter(null), Short.class);
        convertUtilsBean.register(new SqlDateConverter(null), Date.class);
        convertUtilsBean.register(new SqlTimeConverter(null), Time.class);
        convertUtilsBean.register(new SqlTimestampConverter(null),
                Timestamp.class);
        convertUtilsBean.register(new FileConverter(null), File.class);
        convertUtilsBean.register(new URLConverter(null), URL.class);
        convertUtilsBean
                .register(new DateConverter(null), java.util.Date.class);

        return convertUtilsBean;
    }

    @Override
    public <T> T convert(String value, Class<T> type) {
        return super.convert(adjust(value, type), type);
    }

    @Override
    public <T> T[] convert(String[] values, Class<T> type) {
        for (int i = 0; i < values.length; i++) {
            values[i] = adjust(values[i], type);
        }
        return super.convert(values, type);
    }

    String adjust(String value, Class<?> type) {
        if (isNumericType(type)) {
            if ("true".equals(value)) {
                return TRUE_NUMBER;
            } else if ("false".equals(value)) {
                return FALSE_NUMBER;
            }
        } else if (type == Boolean.class) {
            if (TRUE_NUMBER.equals(value)) {
                return String.valueOf(true);
            } else if (FALSE_NUMBER.equals(value)) {
                return String.valueOf(false);
            }
        }
        return value;
    }

    boolean isNumericType(Class<?> type) {
        return Number.class.isAssignableFrom(type) || type == Byte.TYPE
                || type == Short.TYPE || type == Integer.TYPE
                || type == Long.TYPE || type == Float.TYPE
                || type == Double.TYPE;
    }
}
