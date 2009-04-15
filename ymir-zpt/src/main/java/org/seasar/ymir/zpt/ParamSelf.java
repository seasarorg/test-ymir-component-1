package org.seasar.ymir.zpt;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.ymir.zpt.util.YmirUtils;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.zpt.tales.Null;

class ParamSelf {
    private HttpServletRequest request_;

    private Object self_;

    private static final Log log_ = LogFactory.getLog(ParamSelf.class);

    public ParamSelf(HttpServletRequest request, Object self) {
        request_ = request;
        self_ = self;
    }

    public Object get(TemplateContext context, String name) {
        Object value = request_.getParameter(name);
        if (value == null) {
            if (self_ != null) {
                YmirUtils.preserveTypeConversionHint(context, self_, name);
                try {
                    value = PropertyUtils.getProperty(self_, name);
                } catch (Throwable t) {
                    if (log_.isDebugEnabled()) {
                        log_.debug("Can't get Property: self=" + self_
                                + ", name=" + name, t);
                    }
                }
            }
        }
        if (value == null) {
            value = Null.instance;
        }
        return value;
    }
}
