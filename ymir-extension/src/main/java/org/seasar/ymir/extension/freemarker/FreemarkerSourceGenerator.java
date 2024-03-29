package org.seasar.ymir.extension.freemarker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.ymir.extension.creator.BodyDesc;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.SourceGenerator;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;

public class FreemarkerSourceGenerator implements SourceGenerator {
    private SourceCreator sourceCreator_;

    @Binding(bindingType = BindingType.MUST)
    public void setSourceCreator(SourceCreator sourceCreator) {
        sourceCreator_ = sourceCreator;
    }

    public String generateGapSource(ClassDesc classDesc) {
        if (classDesc == null) {
            return null;
        }

        String templateName = classDesc.getType().getSuffix() + ".java";

        return generateClassSource(templateName, classDesc);
    }

    public String generateBaseSource(ClassDesc classDesc) {
        if (classDesc == null) {
            return null;
        }

        String templateName = classDesc.getType().getSuffix() + "Base.java";

        return generateClassSource(templateName, classDesc);
    }

    public String generateClassSource(String templateName, ClassDesc classDesc) {
        return generateSource(templateName, classDesc
                .getSourceGeneratorParameter());
    }

    public String generateTemplateSource(String suffix, Map<String, Object> root) {
        return generateSource("Template" + suffix, root);
    }

    public String generateBodySource(BodyDesc bodyDesc) {
        if (bodyDesc == null) {
            return null;
        }
        return generateSource("Body." + bodyDesc.getKey(), bodyDesc.getRoot());
    }

    String generateSource(String templateName, Map<String, Object> root) {
        Configuration cfg = new Configuration();
        cfg.setEncoding(Locale.getDefault(), TEMPLATE_ENCODING);
        cfg.setTemplateLoader(new ClassTemplateLoader(getClass(), "template"));
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        root = new HashMap<String, Object>(root);
        root.put("application", sourceCreator_.getApplication());
        root.put("fieldSpecialPrefix", sourceCreator_.getSourceCreatorSetting()
                .getFieldSpecialPrefix());
        root.put("fieldPrefix", sourceCreator_.getSourceCreatorSetting()
                .getFieldPrefix());
        root.put("fieldSuffix", sourceCreator_.getSourceCreatorSetting()
                .getFieldSuffix());

        StringWriter sw = new StringWriter();
        try {
            cfg.getTemplate(templateName + ".ftl").process(root, sw);
        } catch (TemplateException ex) {
            throw new RuntimeException(ex);
        } catch (FileNotFoundException ex) {
            // テンプレートがない場合は空の結果を返す。
            return "";
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
        return sw.toString();
    }
}
