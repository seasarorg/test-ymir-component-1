package org.seasar.ymir.creator;

import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;

public class LogicCreator extends
        org.seasar.framework.container.creator.LogicCreator {
    public LogicCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setInstanceDef(InstanceDefFactory.SINGLETON);
    }
}
