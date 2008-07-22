package org.seasar.ymir.creator;

import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;

public class ServiceCreator extends
        org.seasar.framework.container.creator.ServiceCreator {
    public ServiceCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setInstanceDef(InstanceDefFactory.SINGLETON);
    }
}
