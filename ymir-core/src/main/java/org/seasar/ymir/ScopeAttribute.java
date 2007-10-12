package org.seasar.ymir;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.log.Logger;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.scope.Scope;

public class ScopeAttribute {
    private S2Container container_;

    private String name_;

    private Scope scope_;

    private Method writeMethod_;

    private Method readMethod_;

    private Set<String> enableActionNameSet_;

    private boolean injectWhereNull_;

    private boolean outjectWhereNull_;

    private HotdeployManager hotdeployManager_;

    private static final Logger logger_ = Logger
            .getLogger(ScopeAttribute.class);

    public ScopeAttribute(S2Container container, String name, Scope scope,
            Method writeMethod, boolean injectWhereNull, Method readMethod,
            boolean outjectWhereNull, String[] enableActionNames) {
        container_ = container;
        name_ = name;
        scope_ = scope;
        writeMethod_ = writeMethod;
        injectWhereNull_ = injectWhereNull;
        readMethod_ = readMethod;
        outjectWhereNull_ = outjectWhereNull;
        if (enableActionNames.length > 0) {
            enableActionNameSet_ = new HashSet<String>(Arrays
                    .asList(enableActionNames));
        }
        hotdeployManager_ = (HotdeployManager) container_
                .getComponent(HotdeployManager.class);
    }

    public void injectTo(Object component) {
        Object value = scope_.getAttribute(name_);
        if (value != null || injectWhereNull_) {
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
                writeMethod_.invoke(component, new Object[] { value });
            } catch (IllegalArgumentException ex) {
                // 型が合わなかった場合は値を消すようにする。
                removeValue = true;
                logger_.warn("Can't inject scope attribute: scope=" + scope_
                        + ", attribute name=" + name_ + ", value=" + value
                        + ", write method=" + writeMethod_, ex);
            } catch (Throwable t) {
                // Exceptionをスローしつつ値を消すようにする。
                removeValue = true;
                throw new IORuntimeException(
                        "Can't inject scope attribute: scope=" + scope_
                                + ", attribute name=" + name_ + ", value="
                                + value + ", write method=" + writeMethod_, t);
            } finally {
                if (removeValue) {
                    scope_.setAttribute(name_, null);
                }
            }
        }
    }

    public void outjectFrom(Object component) {
        Object value;
        try {
            value = readMethod_.invoke(component, new Object[0]);
        } catch (Throwable t) {
            throw new IORuntimeException(
                    "Can't outject scope attribute: scope=" + scope_
                            + ", attribute name=" + name_ + ", read method="
                            + readMethod_, t);
        }
        if (value != null || outjectWhereNull_) {
            scope_.setAttribute(name_, value);
        }
    }

    public boolean isEnable(String actionName) {
        if (enableActionNameSet_ == null) {
            return true;
        } else {
            return enableActionNameSet_.contains(actionName);
        }
    }
}
