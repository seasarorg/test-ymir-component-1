package org.seasar.ymir.creator;

import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.creator.ComponentCreatorImpl;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;

public class InterceptorCreator extends ComponentCreatorImpl {
    public static final String SUFFIX = "Interceptor";

    public InterceptorCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setNameSuffix(SUFFIX);
        setInstanceDef(InstanceDefFactory.SINGLETON);
    }

    public ComponentCustomizer getInterceptorCustomizer() {
        return getCustomizer();
    }

    public void setInterceptorCustomizer(ComponentCustomizer customizer) {
        setCustomizer(customizer);
    }
}
