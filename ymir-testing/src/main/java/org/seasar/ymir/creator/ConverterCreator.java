package org.seasar.ymir.creator;

import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;

public class ConverterCreator extends
        org.seasar.framework.container.creator.ConverterCreator {
    public ConverterCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setInstanceDef(InstanceDefFactory.SINGLETON);
    }
}
