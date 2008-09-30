package org.seasar.ymir.container.assembler;

import java.util.HashSet;
import java.util.Set;

import org.seasar.framework.beans.IllegalPropertyRuntimeException;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.assembler.AbstractPropertyAssembler;

public class ExplicitPropertyAssembler extends AbstractPropertyAssembler {
    public ExplicitPropertyAssembler(ComponentDef componentDef) {
        super(componentDef);
    }

    public void assemble(Object component)
            throws IllegalPropertyRuntimeException {
        if (component == null) {
            return;
        }
        ComponentDef cd = getComponentDef();
        Set<String> names = new HashSet<String>();
        int size = cd.getPropertyDefSize();
        for (int i = 0; i < size; ++i) {
            PropertyDef propDef = cd.getPropertyDef(i);
            propDef.getAccessTypeDef().bind(getComponentDef(), propDef,
                    component);
            names.add(propDef.getPropertyName());
        }
        if (cd.isExternalBinding()) {
            bindExternally(getBeanDesc(component), cd, component, names);
        }
    }
}
