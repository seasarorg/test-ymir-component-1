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

public class YmirTypeConversionManager extends BeanUtilsTypeConversionManager {
    @Override
    protected ConvertUtilsBean prepare(ConvertUtilsBean convertUtilsBean) {
        convertUtilsBean = super.prepare(convertUtilsBean);

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

        return convertUtilsBean;
    }
}
