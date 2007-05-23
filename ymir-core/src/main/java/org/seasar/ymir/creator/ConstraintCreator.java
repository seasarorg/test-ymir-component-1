package org.seasar.ymir.creator;

import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.creator.ComponentCreatorImpl;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;

public class ConstraintCreator extends ComponentCreatorImpl {

    public ConstraintCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setNameSuffix(YmirNamingConvention.SUFFIX_CONSTRAINT);
        setInstanceDef(InstanceDefFactory.SINGLETON);
        setExternalBinding(false);
    }

    public ComponentCustomizer getConstraintCustomizer() {
        return getCustomizer();
    }

    public void setConstraintCustomizer(ComponentCustomizer customizer) {
        setCustomizer(customizer);
    }
}
