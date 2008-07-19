package org.seasar.ymir.extension.creator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ClassType {
    PAGE("Page", false), DTO("Dto", false), DAO("Dao", true), DXO("Dxo", true), BEAN(
            "Bean", true), CONVERTER("Converter", true);

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
}
