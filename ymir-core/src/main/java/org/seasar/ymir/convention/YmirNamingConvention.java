package org.seasar.ymir.convention;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.seasar.cms.pluggable.impl.PluggableNamingConventionImpl;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.StringUtil;
import org.seasar.kvasir.util.PropertyUtils;

public class YmirNamingConvention extends PluggableNamingConventionImpl {
    private static final char PACKAGE_SEPARATOR = '_';

    private static final String PACKAGE_SEPARATOR_STR = "_";

    public static final String SUFFIX_EXCEPTIONHANDLER = "Handler";

    public static final String SUFFIX_CONSTRAINT = "Constraint";

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
