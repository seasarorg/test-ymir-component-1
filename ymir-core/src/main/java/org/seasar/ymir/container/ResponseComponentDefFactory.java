package org.seasar.ymir.container;

import org.seasar.cms.pluggable.ThreadContextComponentDef;
import org.seasar.cms.pluggable.ThreadContextComponentDefFactory;
import org.seasar.ymir.Response;

public class ResponseComponentDefFactory implements
        ThreadContextComponentDefFactory {

    public Class<?> getComponentClass() {
        return Response.class;
    }

    public String getComponentName() {
        return "ymirResponse";
    }

    public ThreadContextComponentDef newInstance() {
        return new ThreadContextComponentDef(getComponentClass(),
                getComponentName());
    }
}
