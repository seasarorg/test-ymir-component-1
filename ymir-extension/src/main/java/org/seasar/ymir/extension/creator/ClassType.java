package org.seasar.ymir.extension.creator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ClassType {
    BEAN("", true), PAGE("Page", false), DTO("Dto", false), DAO("Dao", true), DXO(
            "Dxo", true), CONVERTER("Converter", true), ;

    public static final String SUFFIX_BASE = "Base";

    private String suffix_;

    private boolean subordinate_;

    private static final Map<String, ClassType> enumMap_;

    private ClassType(String suffix, boolean subordinate) {
        suffix_ = suffix;
        subordinate_ = subordinate;
    }

    static {
        Map<String, ClassType> enumMap = new HashMap<String, ClassType>();
        for (ClassType enm : values()) {
            enumMap.put(enm.getSuffix(), enm);
        }
        enumMap_ = Collections.unmodifiableMap(enumMap);
    }

    public String getSuffix() {
        return suffix_;
    }

    public boolean isSubordinate() {
        return subordinate_;
    }

    public static ClassType enumOf(String suffix) {
        return enumMap_.get(suffix);
    }

    public static ClassType typeOfClass(Class<?> clazz) {
        String className = null;
        if (clazz != null) {
            className = clazz.getName();
        }
        return typeOfClass(className);
    }

    public static ClassType typeOfClass(String className) {
        if (className == null) {
            return null;
        }

        String baseName;
        if (className.endsWith(SUFFIX_BASE)) {
            baseName = className.substring(0, className.length()
                    - SUFFIX_BASE.length());
        } else {
            baseName = className;
        }

        for (ClassType classType : values()) {
            if (classType == BEAN) {
                continue;
            }
            if (baseName.endsWith(classType.getSuffix())) {
                return classType;
            }
        }
        return BEAN;
    }
}
