package org.seasar.ymir.scope.handler;

import org.seasar.ymir.scope.AttributeNotFoundRuntimeException;

public interface ScopeAttributeResolver {
    Object getValue() throws AttributeNotFoundRuntimeException;
}
