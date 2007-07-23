package org.seasar.ymir;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.seasar.framework.log.Logger;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.util.HotdeployUtils;

public class ScopeAttribute {
    private String name_;

    private Scope scope_;

    private Method writeMethod_;

    private Method readMethod_;

    private Set<String> enableActionNameSet_;

    private static final Logger logger_ = Logger
            .getLogger(ScopeAttribute.class);

    public ScopeAttribute(String name, Scope scope, Method writeMethod,
            Method readMethod, String[] enableActionNames) {
        name_ = name;
        scope_ = scope;
        writeMethod_ = writeMethod;
        readMethod_ = readMethod;
        if (enableActionNames.length > 0) {
            enableActionNameSet_ = new HashSet<String>(Arrays
                    .asList(enableActionNames));
        }
    }

    public void injectTo(Object component) {
        Object value = scope_.getAttribute(name_);
        if (value != null) {
            boolean removeValue = false;
            try {
                if (YmirContext.isUnderDevelopment()
                        && !writeMethod_.getParameterTypes()[0]
                                .isAssignableFrom(value.getClass())) {
                    // 開発時はHotdeployのせいで見かけ上型が合わないことがありうる。
                    // そのため開発時で見かけ上型が合わない場合はオブジェクトを再構築する。
                    value = HotdeployUtils.fit(value);
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
        scope_.setAttribute(name_, value);
    }

    public boolean isEnable(String actionName) {
        if (enableActionNameSet_ == null) {
            return true;
        } else {
            return enableActionNameSet_.contains(actionName);
        }
    }
}
