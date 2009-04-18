package org.seasar.ymir.extension.zpt;

import java.text.DateFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.zpt.FormatTypePrefixHandler;

import net.skirnir.freyja.TemplateContext;

public class AnalyzerFormatTypePrefixHandler extends FormatTypePrefixHandler {
    @Override
    protected void adjustFormatAndParameter(TemplateContext context,
            MessageFormat mf, Object[] parameters, Format[] formats, int index) {
        if (parameters[index] instanceof DescWrapper) {
            PropertyDesc propertyDesc = ((DescWrapper) parameters[index])
                    .getPropertyDesc();
            if (propertyDesc != null
                    && !propertyDesc.getTypeDesc().isExplicit()) {
                Class<?> clazz = ((AnalyzerContext) context).getSourceCreator()
                        .getClass(
                                propertyDesc.getTypeDesc()
                                        .getComponentClassDesc().getName());
                if (formats[index] instanceof DateFormat && !isDate(clazz)) {
                    propertyDesc.getTypeDesc().setComponentClassDesc(Date.class);
                } else if (formats[index] instanceof NumberFormat
                        && !isNumber(clazz)) {
                    propertyDesc.getTypeDesc().setComponentClassDesc(Integer.class);
                }
            }

        }
        super.adjustFormatAndParameter(context, mf, parameters, formats, index);
    }

    private boolean isNumber(Class<?> clazz) {
        if (clazz == null) {
            return false;
        } else if (Number.class.isAssignableFrom(clazz)) {
            return true;
        } else if (clazz == Byte.TYPE || clazz == Short.TYPE
                || clazz == Integer.TYPE || clazz == Long.TYPE
                || clazz == Float.TYPE || clazz == Double.TYPE) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDate(Class<?> clazz) {
        if (clazz == null) {
            return false;
        } else if (Date.class.isAssignableFrom(clazz)) {
            return true;
        } else if (Calendar.class.isAssignableFrom(clazz)) {
            return true;
        } else if (isNumber(clazz)) {
            return true;
        } else {
            return false;
        }
    }
}
