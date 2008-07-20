package org.seasar.ymir.extension.creator;

import java.util.Map;

public interface SourceGenerator {

    String generateGapSource(ClassDesc classDesc);

    String generateBaseSource(ClassDesc classDesc);

    String generateClassSource(String templateName, ClassDesc classDesc);

    String generateViewSource(String suffix, Map<String, Object> root);

    String generateBodySource(BodyDesc bodyDesc);
}
