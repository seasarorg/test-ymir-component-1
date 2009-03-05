package org.seasar.ymir.zpt;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.ymir.zpt.annotation.IgnoreException;

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
            if (shouldIgnore(obj, ex)) {
                return null;
            } else {
                throw new EvaluationRuntimeException("Can't get property ("
                        + obj.getClass().getName() + "/" + child + ")", ex);
            }
        } catch (NoSuchMethodException ex) {
            if (!shouldIgnore(obj, ex)) {
                if (log_.isDebugEnabled()) {
                    log_.debug("No Such property (" + obj.getClass().getName()
                            + "/" + child + "): treated as null", ex);
                }
            }
            return null;
        } catch (NestedNullException ex) {
            if (!shouldIgnore(obj, ex)) {
                if (log_.isDebugEnabled()) {
                    log_.debug("Certain property is null ("
                            + obj.getClass().getName() + "/" + child
                            + "): treated as null", ex);
                }
            }
            return null;
        } catch (IllegalAccessException ex) {
            if (shouldIgnore(obj, ex)) {
                return null;
            } else {
                throw new EvaluationRuntimeException("Can't get property ("
                        + obj.getClass().getName() + "/" + child + ")", ex);
            }
        } catch (RuntimeException ex) {
            if (shouldIgnore(obj, ex)) {
                return null;
            } else {
                throw ex;
            }
        }
    }

    boolean shouldIgnore(Object obj, Throwable t) {
        IgnoreException annotation = obj.getClass().getAnnotation(
                IgnoreException.class);
        if (annotation == null) {
            return false;
        }
        if (annotation.value().length == 0) {
            return true;
        }
        for (Class<? extends Throwable> ignored : annotation.value()) {
            if (ignored.isAssignableFrom(t.getClass())) {
                return true;
            }
        }
        return false;
    }
}
