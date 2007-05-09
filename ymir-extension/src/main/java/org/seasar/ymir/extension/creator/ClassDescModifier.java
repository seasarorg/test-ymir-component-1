package org.seasar.ymir.extension.creator;

import java.util.Map;

public interface ClassDescModifier {
    void modify(Map<String, ClassDesc> classDescMap, PathMetaData pathMetaData);
}
