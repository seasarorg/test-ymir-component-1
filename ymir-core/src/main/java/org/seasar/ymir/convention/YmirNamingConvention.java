package org.seasar.ymir.convention;

import org.seasar.cms.pluggable.impl.PluggableNamingConventionImpl;
import org.seasar.kvasir.util.PropertyUtils;

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

    @Override
    public void addIgnorePackageName(String ignorePackageName) {
        String[] ignorePackageNames = PropertyUtils.toLines(ignorePackageName);
        String[] rootPackageNames = getRootPackageNames();
        if (rootPackageNames == null || rootPackageNames.length == 0) {
            throw new RuntimeException(
                    "Must be set rootPackageName before adding ignorePackageName");
        }

        for (int i = 0; i < ignorePackageNames.length; i++) {
            String absoluteName;
            if (ignorePackageNames[i].startsWith(".")) {
                absoluteName = rootPackageNames[0] + ignorePackageNames[i];
            } else {
                absoluteName = ignorePackageNames[i];
            }
            super.addIgnorePackageName(absoluteName);
        }
    }
}
