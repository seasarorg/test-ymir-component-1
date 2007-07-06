package org.seasar.ymir.convention;

import org.seasar.cms.pluggable.impl.PluggableNamingConventionImpl;

public class YmirNamingConvention extends PluggableNamingConventionImpl {
    public static final String SUFFIX_EXCEPTIONHANDLER = "Handler";

    public static final String SUFFIX_CONSTRAINT = "Constraint";

    @Override
    public String fromComponentNameToPartOfClassName(String componentName) {
        String partOfClassName = super
                .fromComponentNameToPartOfClassName(componentName);
        if (componentName.startsWith("_")) {
            return "_" + partOfClassName;
        } else {
            return partOfClassName;
        }
    }
}
