package org.seasar.ymir.scope.impl;

import java.util.ArrayList;
import java.util.Iterator;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.scope.Scope;

/**
 * コンポーネントコンテナのスコープを表すクラスです。
 * <p>このスコープを使うことで、コンポーネントコンテナからコンポーネントを取り出すことができます。
 * </p>
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class ComponentScope implements Scope {
    public Object getAttribute(String name, Class<?> type) {
        S2Container container = getS2Container();
        if (name != null && container.hasComponentDef(name)) {
            if (type == null
                    || type.isAssignableFrom(container.getComponentDef(name)
                            .getComponentClass())) {
                return container.getComponent(name);
            }
        }

        if (type != null && container.hasComponentDef(type)) {
            return container.getComponent(type);
        }

        return null;
    }

    public Iterator<String> getAttributeNames() {
        return new ArrayList<String>().iterator();
    }

    public void setAttribute(String name, Object value) {
    }

    S2Container getS2Container() {
        return YmirContext.getYmir().getApplication().getS2Container();
    }
}
