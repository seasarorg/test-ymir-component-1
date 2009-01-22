package org.seasar.ymir.zpt;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.converter.PropertyHandler;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.converter.annotation.TypeConversionHint;

import net.skirnir.freyja.EvaluationRuntimeException;
import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.tales.PathResolver;

public class YmirBeanPathResolver implements PathResolver {
    public static final String ATTR_TYPECONVERSION_HINT = "ymir.typeConversion.hint";

    private static final Log log_ = LogFactory
            .getLog(YmirBeanPathResolver.class);

    private static final String SUBSTRING_PAGE = "Page";

    public boolean accept(TemplateContext context,
            VariableResolver varResolver, Object obj, String child) {
        return (obj != null);
    }

    public Object resolve(TemplateContext context,
            VariableResolver varResolver, Object obj, String child) {
        preserveTypeConversionHint(context, obj, child);

        try {
            return PropertyUtils.getProperty(obj, child);
        } catch (InvocationTargetException ex) {
            throw new EvaluationRuntimeException("Can't get property ("
                    + obj.getClass().getName() + "/" + child + ")", ex);
        } catch (NoSuchMethodException ex) {
            if (log_.isDebugEnabled()) {
                log_.debug("No Such property (" + obj.getClass().getName()
                        + "/" + child + "): treated as null", ex);
            }
            return null;
        } catch (NestedNullException ex) {
            if (log_.isDebugEnabled()) {
                log_.debug("Certain property is null ("
                        + obj.getClass().getName() + "/" + child
                        + "): treated as null", ex);
            }
            return null;
        } catch (IllegalAccessException ex) {
            throw new EvaluationRuntimeException("Can't get property ("
                    + obj.getClass().getName() + "/" + child + ")", ex);
        }
    }

    private void preserveTypeConversionHint(TemplateContext context,
            Object obj, String child) {
        Object hint = null;
        // 効率化のためにPageという文字列が含まれるクラスに限定している。
        // 「末尾に」ではないのは、エンハンスされていると末尾にPageがつかないため。
        if (obj.getClass().getName().indexOf(SUBSTRING_PAGE) >= 0) {
            TypeConversionManager typeConversionManager = getTypeConversionManager();
            PropertyHandler handler = typeConversionManager.getPropertyHandler(
                    obj, child);
            if (handler != null) {
                hint = getAnnotationHandler().getMarkedAnnotations(
                        handler.getWriteMethod(), TypeConversionHint.class);
            }
        }
        context.setAttribute(ATTR_TYPECONVERSION_HINT, hint);
    }

    protected AnnotationHandler getAnnotationHandler() {
        return (AnnotationHandler) YmirContext.getYmir().getApplication()
                .getS2Container().getComponent(AnnotationHandler.class);
    }

    protected TypeConversionManager getTypeConversionManager() {
        return (TypeConversionManager) YmirContext.getYmir().getApplication()
                .getS2Container().getComponent(TypeConversionManager.class);
    }
}
