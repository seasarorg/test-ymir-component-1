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
import org.seasar.ymir.PropertyHandler;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.impl.SetterPropertyHandler;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.scope.handler.ScopeAttributeHandler;
import org.seasar.ymir.util.BeanUtils;
import org.seasar.ymir.util.ClassUtils;

/**
 * スコープから値を取り出してページにインジェクトするためのクラスです。
 * 
 * @author YOKOTA Takehiko
 */
public class ScopeAttributePopulator implements ScopeAttributeHandler {
    private static final Log log_ = LogFactory
            .getLog(ScopeAttributePopulator.class);

    private Scope scope_;

    private HotdeployManager hotdeployManager_;

    private TypeConversionManager typeConversionManager_;

    private Map<Method, Entry> entryByMethodMap_ = new HashMap<Method, Entry>();

    private Map<String, Entry> entryByNameMap_ = new HashMap<String, Entry>();

    public ScopeAttributePopulator(Scope scope,
            HotdeployManager hotdeployManager,
            TypeConversionManager typeConversionManager) {
        scope_ = scope;
        hotdeployManager_ = hotdeployManager;
        typeConversionManager_ = typeConversionManager;
    }

    public void addEntry(Method method, String[] enabledActionNames) {
        entryByMethodMap_.put(method, new Entry(method, enabledActionNames,
                true));
    }

    public void addEntry(String name, Method method, String[] enabledActionNames) {
        entryByNameMap_.put(name, new Entry(method, enabledActionNames, false));
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

    public void outjectFrom(Object component, String actionName) {
        throw new UnsupportedOperationException();
    }

    protected class Entry {
        private Method method_;

        private Set<String> enabledActionNameSet_;

        private boolean passive_;

        public Entry(Method method, String[] enabledActionNames, boolean passive) {
            method_ = method;
            if (enabledActionNames.length > 0) {
                enabledActionNameSet_ = new HashSet<String>(Arrays
                        .asList(enabledActionNames));
            }
            passive_ = passive;
        }

        public void injectTo(Object component, String name) {
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

            Object value = scope_.getAttribute(name, ClassUtils
                    .toComponentType(handler.getPropertyType()));
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

        public boolean isEnabled(String actionName) {
            if (enabledActionNameSet_ == null) {
                return true;
            } else {
                return enabledActionNameSet_.contains(actionName);
            }
        }
    }
}
