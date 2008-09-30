package org.seasar.ymir.container;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ConstructorAssembler;
import org.seasar.framework.container.PropertyAssembler;
import org.seasar.framework.container.assembler.AbstractAutoBindingDef;
import org.seasar.framework.container.assembler.AssemblerFactory;
import org.seasar.ymir.container.assembler.ExplicitPropertyAssembler;

public class AutoBindingExplicitDef extends AbstractAutoBindingDef {
    private static final String NAME = "explicit";

    public static final AutoBindingExplicitDef INSTANCE = new AutoBindingExplicitDef(
            NAME);

    protected AutoBindingExplicitDef(String name) {
        super(name);
    }

    public ConstructorAssembler createConstructorAssembler(
            ComponentDef componentDef) {
        return AssemblerFactory
                .createDefaultConstructorConstructorAssembler(componentDef);
    }

    public PropertyAssembler createPropertyAssembler(ComponentDef componentDef) {
        return new ExplicitPropertyAssembler(componentDef);
    }
}
