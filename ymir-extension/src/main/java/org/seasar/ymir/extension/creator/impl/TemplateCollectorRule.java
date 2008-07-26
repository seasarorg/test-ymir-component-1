package org.seasar.ymir.extension.creator.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.WebappSourceResourceCollector.Rule;

public class TemplateCollectorRule implements Rule<Template> {
    private SourceCreator sourceCreator_;

    public TemplateCollectorRule(SourceCreator sourceCreator) {
        sourceCreator_ = sourceCreator;
    }

    public void add(String path, List<Template> resourceList) {
        Template template = sourceCreator_.getTemplate(path);
        InputStream is = null;
        try {
            is = template.getInputStream();
            if (((char) is.read()) == '<') {
                resourceList.add(template);
            }
        } catch (IOException ex) {
            throw new IORuntimeException("Can't open template: " + path, ex);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
