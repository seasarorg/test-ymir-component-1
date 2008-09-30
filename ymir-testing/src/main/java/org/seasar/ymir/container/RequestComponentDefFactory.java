package org.seasar.ymir.container;

import org.seasar.cms.pluggable.ThreadContextComponentDef;
import org.seasar.cms.pluggable.ThreadContextComponentDefFactory;
import org.seasar.ymir.Request;

public class RequestComponentDefFactory implements
        ThreadContextComponentDefFactory {

    public Class<?> getComponentClass() {
        return Request.class;
    }

    public String getComponentName() {
        return "ymirRequest";
    }

    public ThreadContextComponentDef newInstance() {
        return new ThreadContextComponentDef(getComponentClass(),
                getComponentName());
    }
}
