package org.seasar.ymir.scope.handler.impl;

import java.lang.reflect.Method;

import org.seasar.framework.log.Logger;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.ymir.PageMetaData;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.scope.Scope;

/**
 * スコープから値を取り出してページにインジェクトするためのクラスです。
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @see PageMetaData
 * @author YOKOTA Takehiko
 */
public class ScopeAttributeInjector extends AbstractScopeAttributeHandler {
    private static final Logger logger_ = Logger
            .getLogger(ScopeAttributeInjector.class);

    public ScopeAttributeInjector(String name, Scope scope,
            Method injectionMethod, boolean injectWhereNull,
            String[] enabledActionNames, HotdeployManager hotdeployManager,
            TypeConversionManager typeConversionManager) {
        super(name, scope, injectionMethod, injectWhereNull,
                enabledActionNames, hotdeployManager, typeConversionManager);
    }

    public void injectTo(Object component) {
        Object value = scope_.getAttribute(name_);
        if (value != null || invokeWhereNull_) {
            value = typeConversionManager_.convert(value, method_
                    .getParameterTypes()[0]);
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

    public void outjectFrom(Object component) {
        throw new UnsupportedOperationException();
    }
}
