package org.seasar.ymir.scaffold.zpt;

import org.seasar.ymir.zpt.JavaResourceTemplateSet;

import net.skirnir.freyja.TemplateEvaluator;

/**
 *@since 1.0.7
 */
public class TraversingJavaResourceTemplateSet extends JavaResourceTemplateSet {
    public TraversingJavaResourceTemplateSet(String rootPackagePath,
            String pageEncoding, TemplateEvaluator evaluator) {
        super(rootPackagePath, pageEncoding, evaluator);
    }

    protected String buildPath(String rootPackagePath, String templatePath) {
        String fullPath = rootPackagePath + templatePath;

        String path = fullPath;
        String name = templatePath;
        while (true) {
            if (resourceExists(path)) {
                return path;
            } else {
                int slash = name.indexOf('/');
                if (slash < 0) {
                    return fullPath;
                } else {
                    name = name.substring(slash + 1);
                    path = rootPackagePath + name;
                }
            }
        }
    }
}
