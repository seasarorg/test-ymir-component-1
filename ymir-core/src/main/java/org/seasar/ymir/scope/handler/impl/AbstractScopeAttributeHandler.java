package org.seasar.ymir.scope.handler.impl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.seasar.framework.log.Logger;
import org.seasar.ymir.PageMetaData;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.scope.Scope;
import org.seasar.ymir.scope.handler.ScopeAttributeHandler;

/**
 * スコープに格納される属性を扱うための抽象クラスです。
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @see PageMetaData
 * @author YOKOTA Takehiko
 */
abstract public class AbstractScopeAttributeHandler implements
        ScopeAttributeHandler {
    protected String name_;

    protected Scope scope_;

    protected Method method_;

    protected Set<String> enabledActionNameSet_;

    protected boolean invokeWhereNull_;

    protected HotdeployManager hotdeployManager_;

    protected TypeConversionManager typeConversionManager_;

    private static final Logger logger_ = Logger
            .getLogger(AbstractScopeAttributeHandler.class);

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param container コンテナ。
     * @param name 属性名。
     * @param scope 属性を格納しているスコープ。
     * @param method Pageオブジェクトに対して属性値をインジェクト／アウトジェクトするためのメソッドを表すMethodオブジェクト。
     * @param invokeWhereNull 属性値がnullであった場合でもPageオブジェクトに対してインジェクト／アウトジェクト操作を行なうかどうか。
     * @param enabledActionNames 属性が有効であるようなアクションの名前。
     * どのアクション呼び出しの時にこの属性に関する操作を行なうかを表します。
     * @param hotdeployManager {@link HotdeployManager}。
     * @param typeConversionManager {@link TypeConversionManager}。
     */
    protected AbstractScopeAttributeHandler(String name, Scope scope,
            Method method, boolean invokeWhereNull,
            String[] enabledActionNames, HotdeployManager hotdeployManager,
            TypeConversionManager typeConversionManager) {
        name_ = name;
        scope_ = scope;
        method_ = method;
        invokeWhereNull_ = invokeWhereNull;
        if (enabledActionNames.length > 0) {
            enabledActionNameSet_ = new HashSet<String>(Arrays
                    .asList(enabledActionNames));
        }
        hotdeployManager_ = hotdeployManager;
        typeConversionManager_ = typeConversionManager;
    }

    public boolean isEnabled(String actionName) {
        if (enabledActionNameSet_ == null) {
            return true;
        } else {
            return enabledActionNameSet_.contains(actionName);
        }
    }
}
