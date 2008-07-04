package org.seasar.ymir.extension.creator.impl;

import java.io.File;

import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.TemplateProvider;
import org.seasar.ymir.util.ServletUtils;

public class DefaultTemplateProvider implements TemplateProvider {
    private SourceCreator sourceCreator_;

    public DefaultTemplateProvider(SourceCreator sourceCreator) {
        sourceCreator_ = sourceCreator;
    }

    public Template getTemplate(String path) {
        String normalizedPath = ServletUtils.normalizePath(path);
        return new FileTemplate(normalizedPath, new File(sourceCreator_
                .getWebappSourceRoot(), normalizedPath), sourceCreator_
                .getTemplateEncoding());
    }
}
