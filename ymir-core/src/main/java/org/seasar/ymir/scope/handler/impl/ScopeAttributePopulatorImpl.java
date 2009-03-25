package org.seasar.ymir.scope.handler.impl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.converter.PropertyHandler;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.converter.annotation.TypeConversionHint;
import org.seasar.ymir.converter.impl.SetterPropertyHandler;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.handler.ScopeAttributePopulator;
import org.seasar.ymir.util.BeanUtils;
import org.seasar.ymir.util.ClassUtils;

public class ScopeAttributePopulatorImpl implements ScopeAttributePopulator {
    private Scope scope_;

    private AnnotationHandler annotationHandler_;

    private ScopeManager scopeManager_;

    private TypeConversionManager typeConversionManager_;

    private Map<Method, Entry> entryByMethodMap_ = new HashMap<Method, Entry>();

    private Map<String, Entry> entryByNameMap_ = new HashMap<String, Entry>();

    private static final Log log_ = LogFactory
            .getLog(ScopeAttributePopulatorImpl.class);

    public ScopeAttributePopulatorImpl(Scope scope,
            AnnotationHandler annotationHandler, ScopeManager scopeManager,
            TypeConversionManager typeConversionManager) {
        scope_ = scope;
        annotationHandler_ = annotationHandler;
        scopeManager_ = scopeManager;
        typeConversionManager_ = typeConversionManager;
    }

    public void addEntry(Method method, boolean populateWhereNull,
            String[] enabledActionNames) {
        entryByMethodMap_.put(method, new Entry(method, populateWhereNull,
                enabledActionNames, true));
    }

    public void addEntry(String name, Method method, boolean populateWhereNull,
            String[] enabledActionNames) {
        entryByNameMap_.put(name, new Entry(method, populateWhereNull,
                enabledActionNames, false));
    }

    public void populateTo(Object component, String actionName) {
        for (Iterator<String> itr = scope_.getAttributeNames(); itr.hasNext();) {
            String name = itr.next();
            Entry entry = getEntry(component, name);
            if (entry == null) {
                continue;
            }
            if (!entry.isEnabled(actionName)) {
                continue;
            }

            entry.populateTo(component, name);
        }
    }

    Entry getEntry(Object component, String name) {
        Entry entry = entryByNameMap_.get(name);
        if (entry != null) {
            return entry;
        }

        String segment = BeanUtils.getFirstSegment(name);
        PropertyHandler handler = typeConversionManager_.getPropertyHandler(
                component, segment);
        if (handler == null) {
            return null;
        }

        Method method;
        if (name.length() > segment.length()) {
            // ネストしている。
            method = handler.getReadMethod();
        } else {
            // ネストしていない。
            method = handler.getWriteMethod();
        }
        if (method == null) {
            return null;
        }

        return entryByMethodMap_.get(method);
    }

    protected class Entry {
        private Method method_;

        private boolean invokeWhereNull_;

        private Set<String> enabledActionNameSet_;

        private boolean passive_;

        public Entry(Method method, boolean invokeWhereNull,
                String[] enabledActionNames, boolean passive) {
            method_ = method;
            invokeWhereNull_ = invokeWhereNull;
            if (enabledActionNames.length > 0) {
                enabledActionNameSet_ = new HashSet<String>(Arrays
                        .asList(enabledActionNames));
            }
            passive_ = passive;
        }

        public void populateTo(Object component, String name) {
            PropertyHandler handler = null;
            if (passive_) {
                handler = typeConversionManager_.getPropertyHandler(component,
                        name);
            } else {
                handler = new SetterPropertyHandler(component, method_);
            }
            if (handler == null) {
                return;
            }

            Object value = scopeManager_.getAttribute(scope_, name, handler
                    .getPropertyType(), annotationHandler_
                    .getMarkedAnnotations(handler.getWriteMethod(),
                            TypeConversionHint.class), false, true);

            if (value != null || invokeWhereNull_) {
                if (log_.isDebugEnabled()) {
                    log_.debug(ClassUtils.getPrettyName(scope_) + " -> "
                            + ClassUtils.getPrettyName(component)
                            + ": property=" + name + ", value=" + value);
                }
                boolean removeValue = false;
                try {
                    handler.setProperty(value);
                } catch (Throwable t) {
                    // populateの時はExceptionをスローしない。
                    // そこがinjectとの違いのひとつ。
                    removeValue = true;
                    if (log_.isDebugEnabled()) {
                        log_.debug("Can't populate scope attribute: scope="
                                + scope_ + ", attribute name=" + name
                                + ", value=" + value + ", write method="
                                + method_, t);
                    }
                } finally {
                    if (removeValue) {
                        scope_.setAttribute(name, null);
                    }
                }
            }
        }

        public boolean isEnabled(String actionName) {
            if (enabledActionNameSet_ == null) {
                return true;
            } else {
                return enabledActionNameSet_.contains(actionName);
            }
        }
    }
}
