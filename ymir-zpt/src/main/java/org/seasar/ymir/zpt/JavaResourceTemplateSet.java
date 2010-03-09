package org.seasar.ymir.zpt;

import java.io.InputStream;

import net.skirnir.freyja.TemplateEvaluator;
import net.skirnir.freyja.impl.AbstractPathTemplateSet;

/**
 *@since 1.0.7
 */
public class JavaResourceTemplateSet extends AbstractPathTemplateSet {
    private String rootPackagePath_;

    private static final Long SERIAL_NUMBER = new Long(0L);

    public JavaResourceTemplateSet(String rootPackagePath, String pageEncoding,
            TemplateEvaluator evaluator) {
        super(pageEncoding, evaluator);

        rootPackagePath_ = rootPackagePath + "/";
    }

    public Long getSerialNumber(String templateName) {
        return SERIAL_NUMBER;
    }

    public boolean hasEntry(String templateName) {
        return (getClassLoader().getResource(getResourcePath(templateName)) != null);
    }

    private String getResourcePath(String templateName) {
        String name;
        if (templateName.startsWith("/")) {
            name = templateName.substring(1/*= "/".length() */);
        } else {
            name = templateName;
        }

        return buildPath(rootPackagePath_, name);
    }

    protected String buildPath(String rootPackagePath, String templatePath) {
        return rootPackagePath + templatePath;
    }

    protected boolean resourceExists(String path) {
        return getClassLoader().getResource(path) != null;
    }

    protected InputStream getInputStream(String templateName) {
        return getClassLoader().getResourceAsStream(
                getResourcePath(templateName));
    }

    protected ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }
}
