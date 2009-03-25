package org.seasar.ymir.convention;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.cms.pluggable.impl.PluggableNamingConventionImpl;
import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.creator.ComponentCreatorImpl;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.StringUtil;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.IllegalClientCodeRuntimeException;

public class YmirNamingConvention extends PluggableNamingConventionImpl {
    private static final char PACKAGE_SEPARATOR = '_';

    private static final String PACKAGE_SEPARATOR_STR = "_";

    public static final String SUFFIX_EXCEPTIONHANDLER = "Handler";

    public static final String SUFFIX_CONSTRAINT = "Constraint";

    public static final String APPKEY_NAMINGCONVENTION_HOTDEPLOYABLEONLYPACKAGEFORCREATOR = "namingConvention.hotdeployableOnlyPackageForCreator";

    private boolean hotdeployableOnlyPackageForCreator_;

    private Set<String> creatorPackageNameSet_ = new HashSet<String>();

    private String[] targetPackageNames_ = new String[0];

    private static final Log log_ = LogFactory
            .getLog(YmirNamingConvention.class);

    public void setHotdeployableOnlyPackageForCreator(
            boolean hotdeployableOnlyPackageForCreator) {
        hotdeployableOnlyPackageForCreator_ = hotdeployableOnlyPackageForCreator;
    }

    public void setCreators(ComponentCreator[] creators) {
        creatorPackageNameSet_.clear();
        for (ComponentCreator creator : creators) {
            if (!(creator instanceof ComponentCreatorImpl)) {
                log_.warn("Can't get nameSuffix from ComponentCreator: "
                        + creator.getClass());
            }
            creatorPackageNameSet_
                    .add(fromSuffixToPackageName(((ComponentCreatorImpl) creator)
                            .getNameSuffix()));
        }
        adjustTargetPackageNames();
    }

    @Override
    public void addRootPackageName(String rootPackageName) {
        for (String name : PropertyUtils.toLines(rootPackageName)) {
            super.addRootPackageName(name);
        }
        adjustTargetPackageNames();
    }

    @Override
    public void setSubApplicationRootPackageName(
            String subApplicationRootPackageName) {
        super.setSubApplicationRootPackageName(subApplicationRootPackageName);
        adjustTargetPackageNames();
    }

    void adjustTargetPackageNames() {
        List<String> targetPackageNameList = new ArrayList<String>();
        for (String rootPackageName : getRootPackageNames()) {
            targetPackageNameList.add(rootPackageName + "."
                    + getSubApplicationRootPackageName() + ".");
            for (String creatorPackageName : creatorPackageNameSet_) {
                targetPackageNameList.add(rootPackageName + "."
                        + creatorPackageName + ".");
            }

        }
        targetPackageNames_ = targetPackageNameList.toArray(new String[0]);
    }

    public boolean isHotdeployableOnlyPackageForCreator() {
        return hotdeployableOnlyPackageForCreator_;
    }

    @Override
    public String fromComponentNameToPartOfClassName(String componentName) {
        if (componentName == null) {
            throw new EmptyRuntimeException("componentName");
        }
        String[] names = split(componentName);
        StringBuffer buf = new StringBuffer(50);
        for (int i = 0; i < names.length; ++i) {
            if (i == names.length - 1) {
                buf.append(StringUtil.capitalize(names[i]));
            } else {
                buf.append(names[i]).append(".");
            }
        }
        return buf.toString();
    }

    String[] split(String str) {
        List<String> list = new ArrayList<String>();
        int prefixLength = 0;
        boolean separatorDetected = true;
        for (StringTokenizer st = new StringTokenizer(str,
                PACKAGE_SEPARATOR_STR, true); st.hasMoreTokens();) {
            String tkn = st.nextToken();
            if (separatorDetected) {
                if (PACKAGE_SEPARATOR_STR.equals(tkn)) {
                    prefixLength++;
                } else {
                    separatorDetected = false;
                    if (prefixLength > 0) {
                        StringBuilder sb = new StringBuilder(prefixLength
                                + tkn.length());
                        for (int i = 0; i < prefixLength; i++) {
                            sb.append(PACKAGE_SEPARATOR);
                        }
                        sb.append(tkn);
                        tkn = sb.toString();
                        prefixLength = 0;
                    }
                    list.add(tkn);
                }
            } else {
                separatorDetected = true;
            }
        }
        if (prefixLength > 0) {
            StringBuilder sb = new StringBuilder(prefixLength);
            for (int i = 0; i < prefixLength; i++) {
                sb.append(PACKAGE_SEPARATOR);
            }
            list.add(sb.toString());
        }
        return list.toArray(new String[0]);
    }

    @Override
    public void addIgnorePackageName(String ignorePackageName) {
        String[] ignorePackageNames = PropertyUtils.toLines(ignorePackageName);
        String[] rootPackageNames = getRootPackageNames();
        if (rootPackageNames == null || rootPackageNames.length == 0) {
            throw new IllegalClientCodeRuntimeException(
                    "Must be set rootPackageName before adding ignorePackageName");
        }

        for (String name : ignorePackageNames) {
            String absoluteName;
            if (name.startsWith(".")) {
                absoluteName = rootPackageNames[0] + name;
            } else {
                absoluteName = name;
            }
            super.addIgnorePackageName(absoluteName);
        }
    }

    @Override
    public boolean isTargetClassName(String className) {
        if (!hotdeployableOnlyPackageForCreator_) {
            return super.isTargetClassName(className);
        }

        for (int i = 0; i < targetPackageNames_.length; ++i) {
            if (className.startsWith(targetPackageNames_[i])) {
                return true;
            }
        }
        return false;
    }

    public String[] getTargetPackageNames() {
        return targetPackageNames_;
    }
}
