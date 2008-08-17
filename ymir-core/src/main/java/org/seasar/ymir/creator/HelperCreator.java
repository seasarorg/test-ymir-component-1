package org.seasar.ymir.creator;

import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;

public class HelperCreator extends
        org.seasar.framework.container.creator.HelperCreator {
    public HelperCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setInstanceDef(InstanceDefFactory.SINGLETON);
    }
}
