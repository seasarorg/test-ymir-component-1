package org.seasar.cms.ymir.container;

import org.seasar.framework.container.impl.SimpleComponentDef;

public class ThreadContextComponentDef extends SimpleComponentDef {

    private Class componentClass_;

    public ThreadContextComponentDef(Class componentClass, String componentName) {
        super(componentClass, componentName);
        componentClass_ = componentClass;
    }

    public Object getComponent() {
        return ((ThreadContext) getContainer().getRoot().getComponent(
            ThreadContext.class)).getComponent(componentClass_);
    }

    public void destroy() {
        super.destroy();
        componentClass_ = null;
    }
}