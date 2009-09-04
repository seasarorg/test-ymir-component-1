package org.seasar.ymir.batch.creator;

import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.creator.ComponentCreatorImpl;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;

/**
 * バッチクラスのためのComponentCreatorです。
 * 
 * @author skirnir
 */
public class BatchCreator extends ComponentCreatorImpl {
    private static final String NAME_SUFFIX_BATCH = "Batch";

    public BatchCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setNameSuffix(NAME_SUFFIX_BATCH);
        setInstanceDef(InstanceDefFactory.PROTOTYPE);
        setEnableInterface(false);
        setEnableAbstract(false);
    }

    public ComponentCustomizer getBatchCustomizer() {
        return getCustomizer();
    }

    public void setBatchCustomizer(ComponentCustomizer customizer) {
        setCustomizer(customizer);
    }
}
