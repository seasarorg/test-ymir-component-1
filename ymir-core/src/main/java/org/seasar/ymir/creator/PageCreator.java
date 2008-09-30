package org.seasar.ymir.creator;

import org.seasar.framework.convention.NamingConvention;
import org.seasar.ymir.container.AutoBindingExplicitDef;

public class PageCreator extends
        org.seasar.framework.container.creator.PageCreator {
    public PageCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setExternalBinding(false);
        setAutoBindingDef(AutoBindingExplicitDef.INSTANCE);
    }
}
