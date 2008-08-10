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
import org.seasar.ymir.ComponentMetaData;
import org.seasar.ymir.PropertyHandler;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.scope.handler.ScopeAttributeHandler;
import org.seasar.ymir.util.BeanUtils;

/**
 * スコープから値を取り出してページにインジェクトするためのクラスです。
 * 
 * @see ComponentMetaData
 * @author YOKOTA Takehiko
 */
public class ScopeAttributePopulator implements ScopeAttributeHandler {
    private static final Log log_ = LogFactory
            .getLog(ScopeAttributePopulator.class);

    private Scope scope_;

    private HotdeployManager hotdeployManager_;

    private TypeConversionManager typeConversionManager_;

    private Map<Method, Entry> entryMap_ = new HashMap<Method, Entry>();

    public ScopeAttributePopulator(Scope scope,
            HotdeployManager hotdeployManager,
            TypeConversionManager typeConversionManager) {
        scope_ = scope;
        hotdeployManager_ = hotdeployManager;
        typeConversionManager_ = typeConversionManager;
    }

    public void addEntry(Method method, boolean injectWhereNull,
            String[] enabledActionNames) {
        entryMap_.put(method, new Entry(method, injectWhereNull,
                enabledActionNames));
    }

    public void injectTo(Object component, String actionName) {
        for (Iterator<String> itr = scope_.getAttributeNames(); itr.hasNext();) {
            String name = itr.next();
            Entry entry = getEntry(component, name);
            if (entry == null) {
                continue;
            }
            if (!entry.isEnabled(actionName)) {
                continue;
            }

            entry.injectTo(component, name);
        }
    }

    Entry getEntry(Object component, String name) {
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

        return entryMap_.get(method);
    }

    public void outjectFrom(Object component, String actionName) {
        throw new UnsupportedOperationException();
    }

    protected class Entry {
        private Method method_;

        private boolean injectWhereNull_;

        private Set<String> enabledActionNameSet_;

        public Entry(Method method, boolean injectWhereNull,
                String[] enabledActionNames) {
            method_ = method;
            injectWhereNull_ = injectWhereNull;
            if (enabledActionNames.length > 0) {
                enabledActionNameSet_ = new HashSet<String>(Arrays
                        .asList(enabledActionNames));
            }
        }

        public void injectTo(Object component, String name) {
            PropertyHandler handler = typeConversionManager_
                    .getPropertyHandler(component, name);
            if (handler == null) {
                return;
            }

            Object value = scope_.getAttribute(name);
            if (value != null || injectWhereNull_) {
                value = typeConversionManager_.convert(value, handler
                        .getPropertyType());
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
                    handler.setProperty(value);
                } catch (IllegalArgumentException ex) {
                    // 型が合わなかった場合は値を消すようにする。
                    removeValue = true;
                    log_.warn("Can't populate scope attribute: scope=" + scope_
                            + ", attribute name=" + name + ", value=" + value
                            + ", target method=" + method_, ex);
                } catch (Throwable t) {
                    // Exceptionをスローしつつ値を消すようにする。
                    removeValue = true;
                    throw new IORuntimeException(
                            "Can't inject scope attribute: scope=" + scope_
                                    + ", attribute name=" + name + ", value="
                                    + value + ", write method=" + method_, t);
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
