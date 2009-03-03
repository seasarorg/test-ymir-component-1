package org.seasar.ymir.creator;

import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.creator.ComponentCreatorImpl;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.ymir.container.AutoBindingExplicitDef;
import org.seasar.ymir.convention.YmirNamingConvention;

public class ExceptionHandlerCreator extends ComponentCreatorImpl {
    public ExceptionHandlerCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setNameSuffix(YmirNamingConvention.SUFFIX_EXCEPTIONHANDLER);
        setInstanceDef(InstanceDefFactory.PROTOTYPE);
        setAutoBindingDef(AutoBindingExplicitDef.INSTANCE);
    }

    public ComponentCustomizer getExceptionHandlerCustomizer() {
        return getCustomizer();
    }

    @Binding(bindingType = BindingType.MAY)
    public void setExceptionHandlerCustomizer(ComponentCustomizer customizer) {
        setCustomizer(customizer);
    }
}
