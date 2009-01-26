package org.seasar.ymir.zpt;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.skirnir.freyja.EvaluationRuntimeException;
import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.tales.PathResolver;

public class YmirBeanPathResolver implements PathResolver {
    private static final Log log_ = LogFactory
            .getLog(YmirBeanPathResolver.class);

    public boolean accept(TemplateContext context,
            VariableResolver varResolver, Object obj, String child) {
        return (obj != null);
    }

    public Object resolve(TemplateContext context,
            VariableResolver varResolver, Object obj, String child) {
        YmirUtils.preserveTypeConversionHint(context, obj, child);

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
}
