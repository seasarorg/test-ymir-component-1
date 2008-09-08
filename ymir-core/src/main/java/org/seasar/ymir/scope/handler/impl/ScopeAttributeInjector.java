package org.seasar.ymir.scope.handler.impl;

import java.lang.reflect.Method;

import org.seasar.framework.log.Logger;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.scope.AttributeNotFoundRuntimeException;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.util.ClassUtils;

/**
 * スコープから値を取り出してページにインジェクトするためのクラスです。
 * 
 * @author YOKOTA Takehiko
 */
public class ScopeAttributeInjector extends AbstractScopeAttributeHandler {
    private static final Logger logger_ = Logger
            .getLogger(ScopeAttributeInjector.class);

    private Class<?> type_;

    private Class<?> componentType_;

    private boolean required_;

    public ScopeAttributeInjector(String name, Class<?> type, Scope scope,
            Method injectionMethod, boolean injectWhereNull, boolean required,
            String[] enabledActionNames, HotdeployManager hotdeployManager,
            TypeConversionManager typeConversionManager) {
        super(name, scope, injectionMethod, injectWhereNull,
                enabledActionNames, hotdeployManager, typeConversionManager);
        type_ = type;
        componentType_ = ClassUtils.toComponentType(type);
        required_ = required;
    }

    public void injectTo(Object component, String actionName)
            throws AttributeNotFoundRuntimeException {
        if (!isEnabled(actionName)) {
            return;
        }

        Object value = scope_.getAttribute(name_, componentType_);
        if (required_ && value == null) {
            throw new AttributeNotFoundRuntimeException("Attribute (name="
                    + name_ + ", type=" + type_ + ") not found: method="
                    + method_ + ", component=" + component).setName(name_)
                    .setType(type_).setMethod(method_).setComponent(component);
        }
        if (value != null || invokeWhereNull_) {
            value = typeConversionManager_.convert(value, type_);
            boolean removeValue = false;
            try {
                if (value != null && YmirContext.isUnderDevelopment()) {
                    // 開発時はHotdeployのせいで見かけ上型が合わないことがありうる。
                    // そのため開発時で見かけ上型が合わない場合はオブジェクトを再構築する。
                    // なお、value自身がHotdeployClassLoader以外から読まれたコンテナ
                    // クラスのインスタンスで、中身がHotdeployClassLoaderから読まれたクラス
                    // のインスタンスである場合に適切にオブジェクトを再構築できるように、
                    // 無条件にvalueをfit()に渡すようにしている。（[#YMIR-136]）
                    value = hotdeployManager_.fit(value);
                }
                method_.invoke(component, new Object[] { value });
            } catch (IllegalArgumentException ex) {
                // 型が合わなかった場合は値を消すようにする。
                removeValue = true;
                logger_.warn("Can't inject scope attribute: scope=" + scope_
                        + ", attribute name=" + name_ + ", value=" + value
                        + ", write method=" + method_, ex);
            } catch (Throwable t) {
                // Exceptionをスローしつつ値を消すようにする。
                removeValue = true;
                throw new IORuntimeException(
                        "Can't inject scope attribute: scope=" + scope_
                                + ", attribute name=" + name_ + ", value="
                                + value + ", write method=" + method_, t);
            } finally {
                if (removeValue) {
                    scope_.setAttribute(name_, null);
                }
            }
        }
    }

    public void outjectFrom(Object component, String actionName) {
        throw new UnsupportedOperationException();
    }
}
