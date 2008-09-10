package org.seasar.ymir.scope.handler.impl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.ymir.scope.Scope;

/**
 * スコープに格納される属性を扱うための抽象クラスです。
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
abstract public class AbstractScopeAttributeHandler {
    protected String name_;

    protected Scope scope_;

    protected Method method_;

    protected Set<String> enabledActionNameSet_;

    protected boolean invokeWhereNull_;

    private static final Log log_ = LogFactory
            .getLog(AbstractScopeAttributeHandler.class);

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
     */
    protected AbstractScopeAttributeHandler(String name, Scope scope,
            Method method, boolean invokeWhereNull, String[] enabledActionNames) {
        name_ = name;
        scope_ = scope;
        method_ = method;
        invokeWhereNull_ = invokeWhereNull;
        if (enabledActionNames.length > 0) {
            enabledActionNameSet_ = new HashSet<String>(Arrays
                    .asList(enabledActionNames));
        }
    }

    protected boolean isEnabled(String actionName) {
        if (enabledActionNameSet_ == null) {
            return true;
        } else {
            return enabledActionNameSet_.contains(actionName);
        }
    }
}
