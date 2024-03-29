package org.seasar.ymir.scope.handler.impl;

import java.lang.annotation.Annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.scope.AttributeNotFoundRuntimeException;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.scope.ScopeManager;
import org.seasar.ymir.scope.handler.ScopeAttributeResolver;

public class ScopeAttributeResolverImpl implements ScopeAttributeResolver {
    private Class<?> type_;

    private Annotation[] hint_;

    private ScopeManager scopeManager_;

    private TypeConversionManager typeConversionManager_;

    private Entry[] entries_ = new Entry[0];

    public ScopeAttributeResolverImpl(Class<?> type, Annotation[] hint,
            ScopeManager scopeManager,
            TypeConversionManager typeConversionManager) {
        type_ = type;
        hint_ = hint;
        scopeManager_ = scopeManager;
        typeConversionManager_ = typeConversionManager;
    }

    public void addEntry(Scope scope, String name, boolean required) {
        entries_ = (Entry[]) ArrayUtil.add(entries_, new Entry(scope, name,
                required));
    }

    public Object getValue() throws AttributeNotFoundRuntimeException {
        for (int i = 0; i < entries_.length; i++) {
            Object value = entries_[i].getValue();
            if (value != null) {
                return value;
            }
        }
        return typeConversionManager_.convert((Object) null, type_, hint_);
    }

    protected class Entry {
        private Scope scope_;

        private String name_;

        private boolean required_;

        public Entry(Scope scope, String name, boolean required) {
            scope_ = scope;
            name_ = name;
            required_ = required;
        }

        public Object getValue() throws AttributeNotFoundRuntimeException {
            return scopeManager_.getAttribute(scope_, name_, type_, hint_,
                    required_, false);
        }
    }
}
