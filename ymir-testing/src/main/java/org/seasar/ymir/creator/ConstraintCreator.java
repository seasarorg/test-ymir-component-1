package org.seasar.ymir.creator;

import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.creator.ComponentCreatorImpl;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.ymir.convention.YmirNamingConvention;

public class ConstraintCreator extends ComponentCreatorImpl {
    public ConstraintCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setNameSuffix(YmirNamingConvention.SUFFIX_CONSTRAINT);
        setInstanceDef(InstanceDefFactory.SINGLETON);
    }

    public ComponentCustomizer getConstraintCustomizer() {
        return getCustomizer();
    }

    @Binding(bindingType = BindingType.MAY)
    public void setConstraintCustomizer(ComponentCustomizer customizer) {
        setCustomizer(customizer);
    }
}
