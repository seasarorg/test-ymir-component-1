package org.seasar.ymir.scope.impl;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.Request;
import org.seasar.ymir.scope.Scope;

/**
 * リクエストパラメータをインジェクトするための仮想的なスコープを表すクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @see FileRequestParameterScope
 * @author YOKOTA Takehiko
 */
public class RequestParameterScope implements Scope {
    protected S2Container container_;

    @Binding(bindingType = BindingType.MUST)
    public void setS2Container(S2Container container) {
        container_ = container;
    }

    public Object getAttribute(String name) {
        Request request = getRequest();
        FormFile[] fileValues = request.getFileParameterValues(name);
        if (fileValues != null) {
            return fileValues;
        } else {
            return request.getParameterValues(name);
        }
    }

    Request getRequest() {
        return (Request) container_.getComponent(Request.class);
    }

    public void setAttribute(String name, Object value) {
        throw new UnsupportedOperationException("Can't set parameter: name="
                + name + ", value=" + value);
    }

    public Iterator<String> getAttributeNames() {
        Request request = getRequest();
        Set<String> list = new LinkedHashSet<String>();
        for (Iterator<String> itr = request.getParameterNames(); itr.hasNext();) {
            list.add(itr.next());
        }
        for (Iterator<String> itr = request.getFileParameterNames(); itr
                .hasNext();) {
            list.add(itr.next());
        }
        return list.iterator();
    }

    public String getName() {
        return RequestParameterScope.class.getName();
    }
}
