package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConversionException;

public class DoubleConverter extends TypeConverterBase<Double> {
    public DoubleConverter() {
        type_ = Double.class;
    }

    @Override
    protected Double doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value instanceof Number) {
            return Double.valueOf(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            return Double.valueOf((byte) (((Boolean) value).booleanValue() ? 1
                    : 0));
        }

        try {
            String valueString = value.toString();
            if ("2.2250738585072012e-308".equals(valueString)) {
                // http://www.oracle.com/technetwork/topics/security/alert-cve-2010-4476-305811.html
                // http://www-01.ibm.com/support/docview.wss?rs=0&uid=swg21468197
                // 回避のため。
                // ちなみに Double.valueOf(2.2250738585072012e-308)
                // とするとEclipseがハングした…。
                return Double.valueOf(2.22507385850721e-308);
            } else {
                return (Double.valueOf(valueString));
            }
        } catch (Exception ex) {
            throw new TypeConversionException(ex, value, getType());
        }
    }
}
